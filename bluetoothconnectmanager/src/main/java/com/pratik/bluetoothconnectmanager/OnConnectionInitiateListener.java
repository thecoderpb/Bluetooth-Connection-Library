package com.pratik.bluetoothconnectmanager;

import android.bluetooth.BluetoothSocket;

import androidx.annotation.NonNull;

interface OnConnectionInitiateListener {

    void OnBluetoothClientConnect(@NonNull BluetoothSocket socket ,@NonNull OnBluetoothConnect listener);

}
