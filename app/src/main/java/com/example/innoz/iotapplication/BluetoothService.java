package com.example.innoz.iotapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.view.ViewPager;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Ahn on 2017-12-27.
 */

public class BluetoothService extends Activity{
    // Debugging
    private static final String TAG = "BluetoothService";

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // RFCOMM Protocol
    private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter btAdapter;
    private Activity mActivity;
    private Handler mHandler;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private int mState;
    private static final int STATE_NONE = 0; // we're doing nothing
    private static final int STATE_LISTEN = 1; // now listening for incoming
    private static final int STATE_CONNECTING = 2; // now initiating an outgoing
    private static final int STATE_CONNECTED = 3; // now connected to a remote
    // device
    public BluetoothService(Activity ac, Handler h) {
        mActivity = ac;
        mHandler = h;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Check the Bluetooth support
     *
     * @return boolean
     */
    public boolean getDeviceState() {
        Log.i(TAG, "Check the Bluetooth support");
        if (btAdapter == null) {
            Log.d(TAG, "Bluetooth is not available");
            return false;
        } else {
            Log.d(TAG, "Bluetooth is available");
            return true;
        }
    }

    /**
     * Check the enabled Bluetooth
     */
    public void enableBluetooth() {
        Log.i(TAG, "Check the enabled Bluetooth");
        if (btAdapter.isEnabled()) {
            Log.d(TAG, "Bluetooth Enable Now");
            scanDevice();
        } else {
            Log.d(TAG, "Bluetooth Enable Request");
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
        }
    }

    /**
     * Available device search
     */
    public void scanDevice() {
        Log.d(TAG, "Scan Device");
        Intent serverIntent = new Intent(mActivity, DeviceListActivity.class);
        mActivity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    /**
     * after scanning and get device info
     *
     * @param data
     */
    public void getDeviceInfo(Intent data) {
        // Get the device MAC address
        String address = data.getExtras().getString(
                DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        // BluetoothDevice device = btAdapter.getRemoteDevice(address);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        Log.d(TAG, "Get Device Info \n" + "address : " + address);
        connect(device);
    }

    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }


    public synchronized int getState() {
        return mState;
    }

    public synchronized void start() {
        Log.d(TAG, "start");
        // Cancel any thread attempting to make a connection
        if (mConnectThread == null) {
            //nothing.
        } else {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {

        } else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    // ConnectThread �ʱ�ȭ device�� ��� ���� ����
    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread == null) {

            } else {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {

        } else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);

        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    // ConnectedThread �ʱ�ȭ
    public synchronized void connected(BluetoothSocket socket,
                                       BluetoothDevice device) {
        Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread == null) {

        } else {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {

        } else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }

    // ��� thread stop
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_NONE);
    }


    public void write(byte[] out) { // Create temporary object
        ConnectedThread r; // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED)
                return;
            r = mConnectedThread;
            r.write(out);
        } // Perform the write unsynchronized r.write(out); }
    }

    // ���� ����������
    private void connectionFailed() {
        setState(STATE_LISTEN);
    }

    // ������ �Ҿ��� ��
    private void connectionLost() {
        setState(STATE_LISTEN);

    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

			/*
			 * / // Get a BluetoothSocket to connect with the given
			 * BluetoothDevice try { // MY_UUID is the app's UUID string, also
			 * used by the server // code tmp =
			 * device.createRfcommSocketToServiceRecord(MY_UUID);
			 *
			 * try { Method m = device.getClass().getMethod(
			 * "createInsecureRfcommSocket", new Class[] { int.class }); try {
			 * tmp = (BluetoothSocket) m.invoke(device, 15); } catch
			 * (IllegalArgumentException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } catch (IllegalAccessException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } catch
			 * (InvocationTargetException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); }
			 *
			 * } catch (NoSuchMethodException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); } } catch (IOException e) { } /
			 */

            // ����̽� ������ �� BluetoothSocket ����
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");
            btAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
                Log.d(TAG, "Connect Success");

            } catch (IOException e) {
                connectionFailed();
                Log.d(TAG, "Connect Fail");
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG,"unable to close() socket during connection failure",e2);
                }
                BluetoothService.this.start();
                return;
            }
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer
         *            The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                Log.d(TAG,"in bluetooth Write");
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public String listenforMessage(){
            String result = "";
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            try {
                int bytesRead = -1;
                while(true) {
                    bytesRead = mmInStream.read(buffer);
                    if (bytesRead != -1) {
                        while ( (bytesRead == bufferSize) && (buffer[bufferSize-1] != 0)) {
                            result = result + new String(buffer, 0, bytesRead);
                            bytesRead = mmInStream.read(buffer);
                        }
                        result = result + new String(buffer, 0, bytesRead -1);
                    }
                }
            } catch (IOException e) {}
            return result;
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }
}



