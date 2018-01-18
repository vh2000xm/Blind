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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
    private static int current_page =0;


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
    public int tmp_counter =0;
    public int blind_counter = 0;
    public Timer timer = null;


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
        vp.setCurrentItem(current_page);


        /** Bluetooth Permision Check**/
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("서비스를 이용하기 위해 위치 권한이 필요합니다.")
                .setDeniedMessage("권한이 없을 경우 서비스를 이용할 수 없습니다. \n [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

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
                    EditText Room_name = (EditText)findViewById(R.id.edit_txt_Room_Name);
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); // 키보드 설정
                    imm.showSoftInput(Room_name, 0); // Edit text 눌렀을때 키보드 나오기
                    if(Room_name.getText().toString().length() == 0)
                    {
                        Toast.makeText(Help_Bluetooth_Activity.this," 블라인드 이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Room_Name = Room_name.getText().toString();
                        Log.d(TAG,Room_Name);
                        imm.hideSoftInputFromWindow(Room_name.getWindowToken(),0); // 키보드 내리기
                        current_page++;
                        vp.setCurrentItem(current_page);
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
                    vp.setCurrentItem(3);
                    break;

                case R.id.btn_testStart:
                    final TimerTask blind_time = new TimerTask() {
                        @Override
                        public void run() {
                            Log.e("카운터:", String.valueOf(tmp_counter));
                            tmp_counter++;
                        }
                    };
                    // 타이머 시작
                    timer = new Timer();
                    timer.schedule(blind_time, 0, 1000);
                    break;

                case R.id.btn_testStop:
                    // 타이머 끝
                    timer.cancel();
                    blind_counter = tmp_counter;
                    tmp_counter =0;
                    break;

                case R.id.btn_testFinish:
                    vp.setCurrentItem(4);
                    break;

                case R.id.btn_helpFinish:
                    // DB 에 데이터 넣기, 이것저것 데이터 검증하기.

                    if (dbHelper == null) {
                        dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
                    }
                    break;

                default:
                    Log.d("HelpBlueTooth", String.valueOf(id) + "is clicked");
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

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        // Get the Camera instance as the activity achieves full user focus
        Log.d(TAG, "help Bluetooth Resume");
//        current_page =0;
//        vp.setCurrentItem(current_page);
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
                    Log.d(TAG, "Bluetooth key "+key);
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
