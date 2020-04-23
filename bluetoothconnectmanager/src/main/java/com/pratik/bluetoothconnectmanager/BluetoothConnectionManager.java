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

public class BluetoothConnectionManager implements OnConnectionAcceptedListener, OnConnectionInitiateListener {

    private Context mContext;
    private BluetoothAdapter mAdapter = null;
    private BluetoothSocket mSocket = null;
    private ConnectThread cThread = null;
    private AcceptThread aThread = null;

    public static final String TAG = "com.pratik.btlibrarY";

    public BluetoothConnectionManager(Context mContext, BluetoothAdapter adapter) {
        this.mContext = mContext;
        mAdapter = adapter;
    }

    public BluetoothAdapter getAdapter() {
        return mAdapter;
    }

    public boolean isEnabled() {
        return mAdapter.isEnabled();
    }

    public void initConnectionAccept(OnBluetoothConnect listener) {

        aThread = new AcceptThread(getAdapter(), this, listener);
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

    public void connect(@NonNull BluetoothSocket socket, @NonNull OnBluetoothConnect listener) {
        mSocket = socket;
        BluetoothMessageService service = new BluetoothMessageService();
        service.connectService(socket,listener);



    }

    private void getConnectionSocket(@NonNull BluetoothDevice device, @NonNull OnBluetoothConnect listener) {

        cThread = new ConnectThread(device, mAdapter, this, listener);
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


    @Override
    public void OnBluetoothServerAccept(@NonNull BluetoothSocket socket, @NonNull OnBluetoothConnect listener) {
        mSocket = socket;
        if (aThread.isAlive())
            aThread.cancel();
        BluetoothMessageService service = new BluetoothMessageService();
        service.connectService(socket,listener);

    }


    @Override
    public void OnBluetoothClientConnect(@NonNull BluetoothSocket socket, @NonNull OnBluetoothConnect listener) {
        mSocket = socket;
        if (cThread.isAlive())
            cThread.cancel();
        BluetoothMessageService service = new BluetoothMessageService();
        service.connectService(socket,listener);
    }
}
