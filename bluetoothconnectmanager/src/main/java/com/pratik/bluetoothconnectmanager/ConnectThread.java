package com.pratik.bluetoothconnectmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.UUID;

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

public class ConnectThread extends Thread {

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    private BluetoothAdapter btAdapter;
    private OnConnectionInitiateListener mListener;
    private OnBluetoothConnect mmListener;

    public ConnectThread(@NonNull BluetoothDevice device, BluetoothAdapter adapter, OnConnectionInitiateListener listener,OnBluetoothConnect cListener) {

        BluetoothSocket tmp = null;

        btAdapter = adapter;
        mmDevice = device;
        mListener = listener;
        mmListener = cListener;

        try {

            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(MyUUIDs.getUUIDs()));


        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    @Override
    public void run() {

        btAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            Log.i(TAG, "connecting");
            mmSocket.connect();
            Log.i(TAG, "connected");

        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }

        }


        //manageConnectedSocket(mmSocket);
        mListener.OnBluetoothClientConnect(mmSocket,mmListener);
    }




    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }


}
