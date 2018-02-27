package com.example.innoz.iotapplication;

import android.app.IntentService;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.ServiceConfigurationError;

/**
 * Created by Ahn on 2018-02-27.
 */

public class Alram_Service extends Service {

    /**
     * Bluetooth Service
     **/
    private BluetoothService btService = null;
    private String Blutooth_address = null;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private String BT_MSG = null;
    private static BroadcastReceiver mReceiver = null;
    private static String Bluetooth_Stat_action = "com.example.innoz.BLUETOOTH_STAT";

    /**
     * Bluetooth Movement
     **/
    private static final String REQUEST_DOWN = "DO";
    private static final String REQUEST_UP = "UP";
    private final static int BLUETOOTH_NOT_CONNECTED = 18;
    private final static int BLUETOOTH_WELL_CONNECTED = 19;

    /**
     * Internal Movement Value
     **/

    private SQLiteService dbHelper = null;
    private int current_val = 0;
    private int max_vlaue = 0;
    private String room_name = null;

    private int will_move =0;

    String TAG = "Alram_Service";



    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.d("test", "서비스의 onCreate");
    }


    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("test", "서비스의 onStartCommand");

        will_move = intent.getExtras().getInt("percent");
        room_name = intent.getExtras().getString("roomname");

        Log.d(TAG,"PERCENT : "+will_move);

        registerReceiver_fun(intent);

        if (btService == null) {
            btService = new BluetoothService(getApplicationContext(),mHandler);
        }
        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }
        btService.getDeviceInfo( dbHelper.get_dev_address(room_name)); // 블루투스 주소값 받아와서 연결하기.
        String temp = dbHelper.get_max_value(room_name);
        max_vlaue = Integer.parseInt(temp);
        temp = dbHelper.get_current_value(room_name);
        current_val = Integer.parseInt(temp);

        return START_STICKY;
    }

    private void registerReceiver_fun(Intent intent) {
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TAG, "action :" + action);
//                if (Bluetooth_Stat_action.equals(action)) {
                if (Bluetooth_Stat_action.equals(action)) {
                    if (intent.getExtras().getBoolean("stat")) {
                        switch (will_move) {
                            case 25:
                                if (current_val > (float) max_vlaue * 0.25) {
                                    BT_Send(REQUEST_UP, (int) (current_val - (float) max_vlaue * 0.25));
                                    current_val = (int) ((float) max_vlaue * 0.25);
                                    DB_Set(room_name, current_val);
                                } else if (current_val < (float) max_vlaue * 0.25) {
                                    BT_Send(REQUEST_DOWN, (int) ((float) max_vlaue * 0.25 - current_val));
                                    current_val = (int) ((float) max_vlaue * 0.25);
                                    DB_Set(room_name, current_val);
                                }
                                Log.d(TAG, " Device Is Move!");
                                break;
                        }
                        Log.d(TAG, " Device Is Connected!");
                    }
                }
            }
        };
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(Bluetooth_Stat_action);
        registerReceiver(mReceiver, filter3);
        Log.d(TAG, "register Receiver");
    }

    public void DB_Set(String room_name, int current_val) {
        String room = room_name;
        int value = current_val;
        dbHelper.update_current_val(room, value);
    }
    public void BT_Send(String send_data, int time) {
        String direction = send_data;
        int running_time = time;
        String Send_value = direction + ":" + running_time + "|";
        Log.d(TAG,"BT Send" + Send_value);
        btService.write(Send_value.getBytes());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        btService.stop();
        btService = null;
        // 서비스가 종료될 때 실행
        Log.d(TAG, "서비스의 onDestroy");
    }



}
