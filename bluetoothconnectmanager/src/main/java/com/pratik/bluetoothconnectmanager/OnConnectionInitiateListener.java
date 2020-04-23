package com.pratik.bluetoothconnectmanager;

import android.bluetooth.BluetoothSocket;

import androidx.annotation.NonNull;

public interface OnConnectionInitiateListener {

    void OnBluetoothClientConnect(@NonNull BluetoothSocket socket ,@NonNull OnBluetoothConnect listener);

}
