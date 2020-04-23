# Bluetooth-Connection-Library

A library to manage connection between two devices using bluetooth

<h2>Add the dependencies to your gradle</h2>
<b>Step 1.</b> Add the JitPack repository to your root build file

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	} 
```
<b>Step 2</b>. Add the dependency to your app level gradle build file
```gradle
dependencies {
	        implementation 'com.github.thecoderpb:Bluetooth-Connection-Library:<version>'
	}
```
<strong>Current \<version\> is <i>1.2.0</i></strong>

<h2>Usage</h2>
Create an instance of the class BluetoothConnectionManager.

```java
BluetoothConnectionManager btConnManger = new BluetoothConnectionManager(Context context,BluetoothAdapter adapter)
```

<h2>Class Methods</h2>

```
1. getAdapter()                  |              returns bluetoothAdapter
2. isEnabled()                   |              returns a boolean checking if BT is enabled or not
3. getPairedDevices()            |              returns a list of bonded BT devices List<BluetoothDevice>
4. getSocket()                   |              returns connected BluetoothSocket
5. disconnect()                  |              closes connection
```

<h4>The connect method</h4>
<p>Establish communication with server</p>

```java
1. connect(Bluetooth device, OnBluetoothConnect listener )
2. connect(String deviceAddress, OnBluetoothConnect listener)
3. connect(byte[] byteAddress, OnBluetoothConnect listener )
4. connect(BluetoothSocket socket, OnBluetoothConnect listener )
```
<h4>The accept method</h4>
<p>Setup a server to listen for communications</p>

```java
initConnectionAccept(OnBluetoothConnect listener)
```
<h2>Interface Methods</h2>

```java
public interface OnBluetoothConnect {

    void connectedStream(BluetoothMessageService service);

    void onReceive(String message);
}
```
<h2>Message Service Class</h2>
<p>Once the device obtains the socket, it opens connection through the socket and this class helps in sending data to the remote device</p>
<p><b>NOTE:</b> Do NOT instantiate the class and directly send message using the sendMessage() method. Use the service obtained from interface call onRecieve. (Refer Sample Code Block)</p>

```java
BluetoothMessageService service;
...
@Override
public void connectedStream(BluetoothMessageService service) {
        this.service = service;
		//OR
	service.sendMessage("Message"); //Sends data to target device
    }
```
	
<h2>Static Calls</h2>

```java
BluetoothConnectionManager.getDeviceStatus(); //retrives device status as client or server
```

Default return value is -1

<b>FLAGS</b>
```java
BluetoothConnectionManager.CLIENT // 0
BluetoothConnectionManager.SERVER // 1
```

<h2>Sample Code</h2>

```java
public class MainActivity extends AppCompatActivity implements OnBluetoothConnect, View.OnClickListener {

    BluetoothConnectionManager btManager;
    BluetoothMessageService service;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btManager = new BluetoothConnectionManager(this, BluetoothAdapter.getDefaultAdapter());
	Button button = findViewById(R.id.button);
	button.setOnClickListener(this);
        if (btManager.isEnabled())
            btManager.initConnectionAccept(this);
        else Log.i(TAG, "Enable bt");
        for (BluetoothDevice device : btManager.getPairedDevices()) {
            Log.i(TAG, device.getAddress() + " " + device.getName());
            if (device.getAddress().equals("0C:E0:DC:2E:80:53")) //Enter your device address
                btManager.connect(device, this);
        }

    }

    @Override
    public void connectedStream(BluetoothMessageService service) {
        this.service = service; // get the message service
    }

    @Override
    public void onReceive(String message) {
        Log.i(TAG, "message sent from device " + message);
    }

    @Override
    public void onClick(View v) {
        if(BluetoothConnectionManager.getDeviceStatus() == BluetoothConnectionManager.CLIENT) //only client will be able to send message
        	service.sendMessage(msg);
    }
}
```

<h2>NOTICE</h2>

```
Copyright (c) 2020 thecoderpb

/*
* ----------------------------------------------------------------------------
* "THE BEER-WARE LICENSE" (Revision 42):
* thecoderpb wrote this file.  As long as you retain this notice you
* can do whatever you want with this stuff. If we meet some day, and you think
* this stuff is worth it, you can buy me a beer in return.   
* ----------------------------------------------------------------------------
*/

```

