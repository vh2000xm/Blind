package com.example.innoz.iotapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
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
     * Layout
     **/
    ViewPager vp = null;
    EditText Edit_Room_Name;

//    Button btn_bluetooth;
//    Button btn_Next;
//    Button btn_test_start;
//    Button btn_test_stop;
//    Button btn_test_finish;
//    Button btn_help_finish;
//    EditText Edit_Room_Name;

    /**
     * User_Data
     **/
    public String key = null;
    public String Room_Name = null;


    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_bluetooth_main);
        vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new PagerAdapterClass(getApplicationContext()));
        vp.setCurrentItem(0);


        /** Bluetooth Permision Check**/
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("서비스를 이용하기 위해 위치 권한이 필요합니다.")
                .setDeniedMessage("권한이 없을 경우 서비스를 이용할 수 없습니다. \n [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

//        /** Main Layout **/
//        btn_bluetooth = (Button) findViewById(R.id.bluetooth_detect);
//
//        btn_Next = (Button) findViewById(R.id.btn_NextStep);
//        btn_test_start = (Button) findViewById(R.id.btn_testStart);
//        btn_test_stop = (Button) findViewById(R.id.btn_testStop);
//        btn_test_finish = (Button) findViewById(R.id.btn_testFinish);
//        btn_help_finish = (Button) findViewById(R.id.btn_helpFinish);
//
//
//        /** 버튼 메인에서 연동되는지 확인해보기 **/
//
//        /** Listener **/
//        btn_bluetooth.setOnClickListener(viewOnClickListener);
//        btn_name.setOnClickListener(viewOnClickListener);
//        btn_Next.setOnClickListener(viewOnClickListener);
//        btn_test_start.setOnClickListener(viewOnClickListener);
//        btn_test_stop.setOnClickListener(viewOnClickListener);
//        btn_test_finish.setOnClickListener(viewOnClickListener);
//        btn_help_finish.setOnClickListener(viewOnClickListener);

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
            //Toast.makeText(Help_Bluetooth_Activity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //Toast.makeText(Help_Bluetooth_Activity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            finish();
        }
    };


    private View.OnClickListener mPagerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_nameok:

                    if(Edit_Room_Name.getText().toString().length() == 0)
                    {
                        Toast.makeText(Help_Bluetooth_Activity.this,"방이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Room_Name = Edit_Room_Name.getText().toString();
                        Log.d(TAG,Room_Name);
                    }
                    break;

                case R.id.bluetooth_detect:
                    if (btService.getDeviceState()) {
                        btService.enableBluetooth();
                    } else {
                        finish();
                    }
                    break;
            }
        }
    };

    /**
     * PagerAdapter
     */
    private class PagerAdapterClass extends PagerAdapter {

        private LayoutInflater mInflater;

        public PagerAdapterClass(Context c){
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object instantiateItem(View pager, int position) {
            View v = null;

            switch (position) {
                case 0:
                    v = mInflater.inflate(R.layout.help_bluetooth_v1, null);
                    v.findViewById(R.id.edit_txt_Room_Name);
                    v.findViewById(R.id.btn_nameok).setOnClickListener(mPagerListener);
                    break;

                case 1:
                    v = mInflater.inflate(R.layout.help_bluetooth_v2, null);
                    v.findViewById(R.id.bluetooth_detect).setOnClickListener(mPagerListener);
                    break;

                case 2:
                    v = mInflater.inflate(R.layout.help_bluetooth_v3, null);
                    v.findViewById(R.id.btn_NextStep).setOnClickListener(mPagerListener);
                    break;

                case 3:
                    v = mInflater.inflate(R.layout.help_bluetooth_v4, null);
                    v.findViewById(R.id.btn_testStart).setOnClickListener(mPagerListener);
                    v.findViewById(R.id.btn_testStop).setOnClickListener(mPagerListener);
                    v.findViewById(R.id.btn_testFinish).setOnClickListener(mPagerListener);
                    break;

                case 4:
                    v = mInflater.inflate(R.layout.help_bluetooth_v5, null);
                    v.findViewById(R.id.btn_helpFinish).setOnClickListener(mPagerListener);
                    break;
            }

                ((ViewPager) pager).addView(v, 0);
                return v;
        }

        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager)pager).removeView((View)view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
        @Override public Parcelable saveState() { return null; }
        @Override public void startUpdate(View arg0) {}
        @Override public void finishUpdate(View arg0) {}
    }

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
