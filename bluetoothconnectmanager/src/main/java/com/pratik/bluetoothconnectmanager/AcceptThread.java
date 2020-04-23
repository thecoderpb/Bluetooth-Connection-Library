package com.pratik.bluetoothconnectmanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static android.provider.CalendarContract.Calendars.NAME;
import static com.pratik.bluetoothconnectmanager.BluetoothConnectionManager.TAG;

public class AcceptThread extends Thread {

    private final BluetoothServerSocket mmServerSocket;
    private OnConnectionAcceptedListener mListener;
    private OnBluetoothConnect mmListener;

    public AcceptThread(BluetoothAdapter adapter, OnConnectionAcceptedListener listener,OnBluetoothConnect cListener) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        mListener = listener;
        mmListener = cListener;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = adapter.listenUsingRfcommWithServiceRecord(NAME, UUID.fromString("00001000-0000-1000-8000-00805f9b34fb"));
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }


    @Override
    public void run() {

        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                Log.i(TAG,"listening");
                socket = mmServerSocket.accept();
                Log.i(TAG,"listened");
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.

                mListener.OnBluetoothServerAccept(socket,mmListener);

                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


            }
        }

    }



    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }


}
