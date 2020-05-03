package com.pratik.bluetoothconnectmanager;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import static com.pratik.bluetoothconnectmanager.BluetoothConnectionManager.TAG;

/**
 * Created by thecoderpb on 24/04/20.
 */

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class BluetoothMessageService {

    private ConnectedThread thread;
    private Handler handler;
    private final int MSG = 1;
    static int CONNECTION_OPEN = 0;
    private boolean OBJ_FLAG = false;

    void connectService(BluetoothSocket socket,OnBluetoothConnect listener) {

        thread = new ConnectedThread(socket,listener);
        thread.start();
        listener.connectedStream(this);
    }

    private void readMessage(final OnBluetoothConnect mListener){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == MSG){
                    if(OBJ_FLAG){
                        mListener.onReceive("",msg.obj);
                    }else {
                        String message = new String((byte[]) msg.obj);
                        String[] split = message.split("\0");
                        Log.i(TAG,"msg " + split[0]);
                        Object obj = split[0];
                        mListener.onReceive(split[0],obj);
                    }

                }
            }
        };
    }

    public void sendMessage(@NonNull String message) {

        message += "\0";
        byte[] msg = message.getBytes();
        if(CONNECTION_OPEN == 1){
            OBJ_FLAG = false;
            thread.write(msg);
        }


    }

    public void sendMessage(@NonNull Object object){

        // Sent object must be serializable
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            byte[] msg = bos.toByteArray();
            if(CONNECTION_OPEN == 1){
                OBJ_FLAG = true;
                thread.write(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final OnBluetoothConnect mListener;
        private byte[] mmBuffer;


        ConnectedThread(BluetoothSocket socket,OnBluetoothConnect listener) {
            mmSocket = socket;
            mListener = listener;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                CONNECTION_OPEN = 1;
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
                CONNECTION_OPEN = 1;
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[2048];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    Log.i(TAG, "message received");

                    readMessage(mListener);
                    Message readMsg = handler.obtainMessage(MSG, numBytes, -1, mmBuffer);
                    readMsg.sendToTarget();
                    // Send the obtained bytes to the UI activity.

                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.

                Log.i(TAG, "message sent");
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.

            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
                CONNECTION_OPEN = 0;
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}