package com.example.innoz.iotapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

/**
 * Created by Ahn on 2017-12-28.
 */

public class Help_Bluetooth_Activity extends Activity {

    private static final String TAG = "Help_Bluetooth_Activity";


    /**Bluetooth**/
    private BluetoothService btService = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int ADDROOM_FINISH = 1;


    /**Data Base**/
    private SQLiteService dbHelper = null;

    // Layout
    Button btn_bluetooth;
    public String key = null;


    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Bluetooth Permision Check**/
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("서비스를 이용하기 위해 위치 권한이 필요합니다.")
                .setDeniedMessage("권한이 없을 경우 서비스를 이용할 수 없습니다. \n [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();



        /** Sliding Layout **/
        SlidingView sv = new SlidingView(this);
        View v1 = View.inflate(this, R.layout.help_bluetooth_v1, null);
        View v2 = View.inflate(this, R.layout.help_bluetooth_v2, null);
        sv.addView(v1);
        sv.addView(v2);
        setContentView(sv);


        /** Main Layout **/
        btn_bluetooth = (Button)findViewById(R.id.bluetooth_detect);
        btn_bluetooth.setOnClickListener(viewOnClickListener);

        /** BT Service **/
        if(btService == null) {
            btService = new BluetoothService(this, mHandler);
        }

        /** DB Service **/
        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }

    }

    /** BT Permission Check **/
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(Help_Bluetooth_Activity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(Help_Bluetooth_Activity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    /** btn Click listener **/
    View.OnClickListener viewOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.bluetooth_detect:
                    if (btService.getDeviceState()) {
                        btService.enableBluetooth();
                    } else {
                        finish();
                    }
                    break;

                default:
                    Log.d("default", String.valueOf(id) + "is clicked");
            }
        }
    };


    /** After BT Choice**/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    key = data.getStringExtra("device_address");
                    //txt_Result.setText(key);
                    btService.getDeviceInfo(data);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    btService.scanDevice();
                } else {
                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
    }
}
