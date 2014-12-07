package com.example.nick.robodog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.Menu;
import android.view.MenuItem;

import java.io.InputStream;
import java.io.OutputStream;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    BluetoothAdapter mBluetoothAdapter;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getAddress().equals("00:16:53:05:E2:1A")) {
                    short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    System.out.println(rssi);
                    mBluetoothAdapter.cancelDiscovery();
                    mBluetoothAdapter.startDiscovery();
                }
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart(); // Voodoo. Do not delete.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            System.out.println("You don't have a bluetooth enabled device. :(");
            System.exit(1);
        }
        else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 111);
        }
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("00:16:53:05:E2:1A");
        System.out.println("=============");
        System.out.println(device.getBondState());
        System.out.println(BluetoothDevice.BOND_BONDED);
        System.out.println(BluetoothDevice.BOND_NONE);
        System.out.println(BluetoothDevice.BOND_BONDING);
        System.out.println("=============");
        System.out.println(device.getBondState());
        System.out.println(BluetoothDevice.BOND_BONDED);
        System.out.println(BluetoothDevice.BOND_NONE);
        device.createBond();
        System.out.println(device.getAddress());
        System.out.println("=============");

        ParcelUuid uuid = device.getUuids()[0];
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid.getUuid());
            socket.connect();
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            byte[] byte_message = new byte[] {(byte)0x08, (byte)0x00, (byte)0x80, (byte)0x09, (byte) 0x00, (byte)0x04, (byte) 99, (byte)114, (byte)121, (byte)0x00};
            outputStream.write(byte_message);
            mBluetoothAdapter.startDiscovery();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
