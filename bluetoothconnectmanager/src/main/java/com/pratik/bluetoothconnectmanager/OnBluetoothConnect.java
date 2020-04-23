package com.pratik.bluetoothconnectmanager;

public interface OnBluetoothConnect {

    void connectedStream(BluetoothMessageService service);

    void onReceive(String message);


}
