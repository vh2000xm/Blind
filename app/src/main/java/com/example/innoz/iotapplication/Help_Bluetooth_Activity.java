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


    /**
     * Bluetooth
     **/
    private BluetoothService btService = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int ADDROOM_FINISH = 1;


    /**
     * Data Base
     **/
    private SQLiteService dbHelper = null;

    /**
     * Data Base
     **/
    Button btn_bluetooth;
    Button btn_name;
    Button btn_Next;
    Button btn_test_start;
    Button btn_test_stop;
    Button btn_test_finish;
    Button btn_help_finish;
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

        View v[] = new View[5];
        v[0] = View.inflate(this, R.layout.help_bluetooth_v1, null);
        v[1] = View.inflate(this, R.layout.help_bluetooth_v2, null);
        v[2] = View.inflate(this, R.layout.help_bluetooth_v3, null);
        v[3] = View.inflate(this, R.layout.help_bluetooth_v4, null);
        v[4] = View.inflate(this, R.layout.help_bluetooth_v5, null);

        for (int i = 0; i < 5; ++i)
            sv.addView(v[i]);

        setContentView(sv);


        /** Main Layout **/
        btn_bluetooth = (Button) findViewById(R.id.bluetooth_detect);
        btn_name = (Button) findViewById(R.id.btn_nameok);
        btn_Next = (Button) findViewById(R.id.btn_NextStep);
        btn_test_start = (Button) findViewById(R.id.btn_testStart);
        btn_test_stop = (Button) findViewById(R.id.btn_testStop);
        btn_test_finish = (Button) findViewById(R.id.btn_testFinish);
        btn_help_finish = (Button) findViewById(R.id.btn_helpFinish);

        /** Listener **/
        btn_bluetooth.setOnClickListener(viewOnClickListener);
        btn_name.setOnClickListener(viewOnClickListener);
        btn_Next.setOnClickListener(viewOnClickListener);
        btn_test_start.setOnClickListener(viewOnClickListener);
        btn_test_stop.setOnClickListener(viewOnClickListener);
        btn_test_finish.setOnClickListener(viewOnClickListener);
        btn_help_finish.setOnClickListener(viewOnClickListener);

        /** BT Service **/
        if (btService == null) {
            btService = new BluetoothService(this, mHandler);
        }

        /** DB Service **/
        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }

    }

    /**
     * BT Permission Check
     **/
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

    /**
     * btn Click listener
     **/
    View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
/**버튼 누르면 다음장 자동으로 넘어가게  SlidingView 클래스 수정할것.**/
                case R.id.btn_nameok:
                    if (btService.getDeviceState()) {
                        btService.enableBluetooth();
                    } else {
                        finish();
                    }
                    break;

                case R.id.bluetooth_detect:
                    if (btService.getDeviceState()) {
                        btService.enableBluetooth();
                    } else {
                        finish();
                    }
                    break;

                case R.id.btn_NextStep:
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


    /**
     * After BT Choice
     **/
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
