package com.pratik.bluetoothconnectmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

/*
    git tag -a 1.1 -m "v1.1"
    git push origin 1.1
*/

public class BluetoothConnectionManager {

    public static final int CLIENT = 0;
    public static final int SERVER = 1;
    private static int STATUS = -1;

    private BluetoothAdapter mAdapter;
    private BluetoothSocket mSocket = null;
    private ConnectThread cThread = null;
    private AcceptThread aThread = null;

    static final String TAG = "com.pratik.btlibrarY";

    public static int getDeviceStatus(){
        return STATUS;
    }

    public BluetoothConnectionManager(Context mContext, BluetoothAdapter adapter) {
        mAdapter = adapter;
    }

    public BluetoothAdapter getAdapter() {
        return mAdapter;
    }

    public boolean isEnabled() {
        return mAdapter.isEnabled();
    }

    public void initConnectionAccept(OnBluetoothConnect listener) {

        aThread = new AcceptThread(getAdapter(), new OnConnectionAcceptedListener() {
            @Override
            public void OnBluetoothServerAccept(@NonNull BluetoothSocket socket, @NonNull OnBluetoothConnect listener) {
                Log.i(TAG,"server connected");
                STATUS = 1;
                mSocket = socket;
                BluetoothMessageService service = new BluetoothMessageService();
                service.connectService(socket,listener);
                listener.connectedStream(service);
            }
        }, listener);
        aThread.start();

    }


    public void connect(@NonNull BluetoothDevice device, @NonNull OnBluetoothConnect listener) {

        getConnectionSocket(device, listener);
    }

    public void connect(@NonNull String deviceAddress, @NonNull OnBluetoothConnect listener) {

        BluetoothDevice device = null;
        try {
            device = mAdapter.getRemoteDevice(deviceAddress);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (device != null)
            getConnectionSocket(device, listener);

    }

    public void connect(@NonNull byte[] byteAddress, @NonNull OnBluetoothConnect listener) {

        BluetoothDevice device = null;
        try {
            device = mAdapter.getRemoteDevice(byteAddress);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (device != null)
            getConnectionSocket(device, listener);

    }

    public void connect(@NonNull BluetoothSocket socket,@NonNull OnBluetoothConnect listener) {
        mSocket = socket;
        BluetoothMessageService service = new BluetoothMessageService();
        service.connectService(socket,listener);



    }

    private void getConnectionSocket(@NonNull BluetoothDevice device, @NonNull OnBluetoothConnect listener) {

        cThread = new ConnectThread(device, mAdapter, new OnConnectionInitiateListener() {
            @Override
            public void OnBluetoothClientConnect(@NonNull BluetoothSocket socket, @NonNull OnBluetoothConnect listener) {
                Log.i(TAG,"client connected");
                STATUS = 0;
                mSocket = socket;
                BluetoothMessageService service = new BluetoothMessageService();
                service.connectService(socket,listener);
                listener.connectedStream(service);
            }
        }, listener);
        cThread.start();

    }


    public List<BluetoothDevice> getPairedDevices() {

        List<BluetoothDevice> devices = null;
        if (mAdapter != null) {
            devices = new ArrayList<>(mAdapter.getBondedDevices());
        }
        return devices;
    }

    public BluetoothSocket getSocket() {
        assert mSocket != null;
        return mSocket;
    }

    public void disconnect() {
        if (getSocket() != null) {
            try {
                getSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if (cThread != null) {
            cThread.cancel();
        }
        if (aThread != null) {
            aThread.cancel();
        }
        Log.i(TAG, "disconnected");
    }


}
