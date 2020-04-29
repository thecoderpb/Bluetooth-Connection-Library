package com.example.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pratik.bluetoothconnectmanager.BluetoothConnectionManager;
import com.pratik.bluetoothconnectmanager.BluetoothMessageService;
import com.pratik.bluetoothconnectmanager.OnBluetoothConnect;

public class MainActivity extends AppCompatActivity implements OnBluetoothConnect, View.OnClickListener {

    BluetoothConnectionManager btManager;
    BluetoothMessageService service;
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        button.setOnClickListener(this);

        btManager = new BluetoothConnectionManager(this, BluetoothAdapter.getDefaultAdapter());

        if (btManager.isEnabled())
            btManager.initConnectionAccept(this);
        else Log.i("asdf", "Enable bt");
        for (BluetoothDevice device : btManager.getPairedDevices()) {
            Log.i("asdf", device.getAddress() + " " + device.getName());
            if (device.getAddress().equals("0C:E0:DC:2E:80:53")) //Enter your device address
                btManager.connect(device, this);
        }

    }


    @Override
    public void onReceive(String message) {
        textView.setText(message);
        Log.i("asdf", "message sent from client " + message);
    }

    @Override
    public void onClick(View v) {
        String msg = editText.getText().toString();
        service.sendMessage(msg);
    }
}
