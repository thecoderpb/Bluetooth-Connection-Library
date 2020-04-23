package com.example.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pratik.bluetoothconnectmanager.BluetoothConnectionManager;
import com.pratik.bluetoothconnectmanager.BluetoothMessageService;
import com.pratik.bluetoothconnectmanager.OnBluetoothConnect;

public class MainActivity extends AppCompatActivity implements OnBluetoothConnect {

   BluetoothMessageService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothConnectionManager btManager = new BluetoothConnectionManager(this,BluetoothAdapter.getDefaultAdapter());

        if(btManager.isEnabled())
            btManager.initConnectionAccept(this);
        else Log.i("asdf","Enable bt");
        btManager.connect("Bluetooth Device Address",this);



    }


    @Override
    public void connectedStream(BluetoothMessageService service) {
        this.service = service;
        service.sendMessage("Hi");
    }

    @Override
    public void onReceive(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
