package com.pratik.bluetoothconnectmanager;

import java.io.InputStream;
import java.io.OutputStream;

public interface OnBluetoothConnect {

    void connectedStream(BluetoothMessageService service);

    void onReceive(String message);
}
