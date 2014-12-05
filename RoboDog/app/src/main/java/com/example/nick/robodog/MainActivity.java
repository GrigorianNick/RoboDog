package com.example.nick.robodog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("creating");
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart(); // Voodoo. Do not delete.d
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
        //device.createBond();
        //System.out.println(device.getAddress());
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
