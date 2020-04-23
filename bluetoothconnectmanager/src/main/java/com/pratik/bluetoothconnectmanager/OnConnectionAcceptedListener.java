package com.pratik.bluetoothconnectmanager;

import android.bluetooth.BluetoothSocket;

import androidx.annotation.NonNull;

interface OnConnectionAcceptedListener {

    void OnBluetoothServerAccept(@NonNull BluetoothSocket socket, @NonNull OnBluetoothConnect listener);
}
