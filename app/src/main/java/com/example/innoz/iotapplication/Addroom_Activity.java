package com.example.innoz.iotapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by Ahn on 2017-12-19.
 */

public class Addroom_Activity extends AppCompatActivity {
    // Debugging
    private static final String TAG = "Addroom_Activity";

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int ADDROOM_FINISH = 1;

    // Layout
    private Button btn_Connect;
    private Button btn_accept;
    private Button btn_test;
    private TextView txt_Result;
    private EditText txt_Roomname;
    private BluetoothService btService = null;
    public String key = null;

    //Database
    private SQLiteService dbHelper = null;



    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    ///////////////위치 권한, 블루투스 권한 확인 및 요청

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        setContentView(R.layout.addroom_activity);

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("서비스를 이용하기 위해 위치 권한이 필요합니다.")
                .setDeniedMessage("권한이 없을 경우 서비스를 이용할 수 없습니다. \n [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        /** Main Layout **/
        btn_Connect = (Button) findViewById(R.id.btn_connect);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        txt_Result = (TextView) findViewById(R.id.txt_result);
        btn_test = (Button) findViewById(R.id.btn_test);
        txt_Roomname = (EditText)findViewById(R.id.txt_RoomName);

        btn_Connect.setOnClickListener(viewOnClickListener);
        btn_accept.setOnClickListener(viewOnClickListener);
        btn_test.setOnClickListener(viewOnClickListener);
        // BluetoothService Ŭ���� ����
        if(btService == null) {
            btService = new BluetoothService(this, mHandler);
        }
        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(Addroom_Activity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(Addroom_Activity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            finish();
        }
    };


    View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.btn_connect:
                    if (btService.getDeviceState()) {
                        btService.enableBluetooth();
                    } else {
                        finish();
                    }
                    break;

                case R.id.btn_accept:
                    dbHelper.delete_all();
//                    dbHelper.insert("20:17:01:05:58:33","test",0,100);
                    finish();
//                    if(txt_Roomname.getText().toString().length() != 0 && key != null) {
//                        //SQL DB에 기기 주소 및 방 이름 입력하기.
//                        Log.d(TAG,"txt room name :" + txt_Roomname.getText().toString());
//                        dbHelper.delete_all();
//                        dbHelper.insert("20:17:01:05:58:33","test",0,100);
//
////                        Intent i = new Intent();
////                        i.putExtra("address", key).putExtra("room",txt_Roomname.getText());
////                        setResult(ADDROOM_FINISH, i);
//                        finish();
//                    }
//                    else
//                    {
//                     Toast.makeText(Addroom_Activity.this,"기기 선택 및 이름 지정되지 않음",Toast.LENGTH_SHORT).show();
//                    }
                    break;

                case R.id.btn_test:
                    Log.d(TAG,"in addroom TestBUTTON");
                    String test = "t";
                    btService.write(test.getBytes());
                    break;

                default:
                    Log.d("default", String.valueOf(id) + "is clicked");
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    key = data.getStringExtra("device_address");
                    txt_Result.setText(key);
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
