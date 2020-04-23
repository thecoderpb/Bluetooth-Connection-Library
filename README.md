# Bluetooth-Connection-Library

A library to manage connection between two devices using bluetooth

<h2>Add the dependencies to your gradle</h2>
<b>Step 1.</b> Add the JitPack repository to your build file

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	} 
```
<b>Step 2</b>. Add the dependency
```
dependencies {
	        implementation 'com.github.thecoderpb:Bluetooth-Connection-Library:<version>'
	}
```
Current \<version\> is 1.0

<h2>Usage</h2>
Create an instance of the class BluetoothConnectionManager.

```
BluetoothConnectionManager btConnManger = new BluetoothConnectionManager(Context context,BluetoothAdapter adapter)
```

<h2>Class Methods</h2>

```
* getAdapter()                  |              returns bluetoothAdapter
* isEnabled()                   |              returns a boolean checking if BT is enabled or not
* getPairedDevices()            |              returns a list of bonded BT devices List<BluetoothDevice>
* getSocket()                   |              returns connected BluetoothSocket
* disconnect()                  |              closes connection
```

<h4>The connect method</h4>
<p>Establish communication with server</p>

```
1. connect(Bluetooth device, OnBluetoothConnect listener )<br>
2. connect(String deviceAddress, OnBluetoothConnect listener)<br>
3. connect(byte[] byteAddress, OnBluetoothConnect listener )<br>
4. connect(BluetoothSocket socket, OnBluetoothConnect listener )<br>
```
<h4>The accept method</h4>
<p>Setup a server to listen for communications</p>

```
initConnectionAccept(OnBluetoothConnect listener)
```
<h2>Interface Methods</h2>

```
public interface OnBluetoothConnect {

    void connectedStream(BluetoothMessageService service);

    void onReceive(String message);
}
```

<h2>NOTICE</h2>
<p>
Copyright 2020 thecoderpb

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</p>
