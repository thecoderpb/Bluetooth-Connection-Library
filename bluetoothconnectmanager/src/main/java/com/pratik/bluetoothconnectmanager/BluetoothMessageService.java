package com.pratik.bluetoothconnectmanager;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.pratik.bluetoothconnectmanager.BluetoothConnectionManager.TAG;


public class BluetoothMessageService {

    private ConnectedThread thread;

    void connectService(BluetoothSocket socket,OnBluetoothConnect listener) {

        thread = new ConnectedThread(socket,listener);
        thread.start();
    }

    public void sendMessage(String message){

        message += "\0";
        byte[] msg = message.getBytes();
        thread.write(msg,message);

    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer;
        private OnBluetoothConnect mListener;

        public ConnectedThread(BluetoothSocket socket,OnBluetoothConnect listener) {
            mmSocket = socket;
            mListener = listener;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
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

                    String message = new String(mmBuffer);
                    String[] split = message.split("\0");

                    mListener.onReceive(split[0]);

                    // Send the obtained bytes to the UI activity.


                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes, String msg) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.

                Log.i("asdf", "message sent");
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.

            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}