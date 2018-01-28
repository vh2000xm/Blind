package com.example.innoz.iotapplication;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ahn on 2017-12-19.
 */

public class DetailActivity extends AppCompatActivity {

    static final String TAG = "DetailActivity";


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
    private static final String REQUEST_FULL_UP = "FU";
    private static final String REQUEST_FULL_DOWN = "FD";
    private final static int BLUETOOTH_NOT_CONNECTED = 18;
    private final static int BLUETOOTH_WELL_CONNECTED = 19;


    /**
     * Internal Movement Value
     **/

    private SQLiteService dbHelper = null;
    private int current_val = 0;
    private int max_vlaue = 0;
    private String room_name = null;


    /**
     * Layout
     **/
    private int progress = 0;
    private TextView smalltext;
    private TextView txt_current_value;
    public CircularProgressBar progressbar;
    private ImageButton btn_bot_bar;
    private ImageButton btn_up_arrow;
    private ImageButton btn_down_arrow;
    private ImageButton btn_full_up;
    private ImageButton btn_full_down;
    private ImageButton btn_25per;
    private ImageButton btn_50per;
    private ImageButton btn_75per;

    private ProgressDialog pd;
    private int loading_count = 0;
    public Timer timer = null;

    final TimerTask blind_time = new TimerTask() {
        @Override
        public void run() {
            Log.e("로딩 카운터:", String.valueOf(loading_count));
            loading_count++;
            if (loading_count > 10) {
                unnormal_exit();
            }
        }
    };


    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);


        // 타이머 시작
//        timer = new Timer();
//        timer.schedule(blind_time, 0, 1000);


//        pd = ProgressDialog.show(DetailActivity.this, "로딩중", "블루투스 연결 중입니다...");

        if (btService == null) {
            btService = new BluetoothService(this, mHandler);
        }
        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }


        Log.d(TAG, getIntent().getExtras().getString("small_text"));
//        Log.d(TAG, getIntent().getExtras().getString(EXTRA_DEVICE_ADDRESS));

        //Layout Init
        progressbar = (CircularProgressBar) findViewById(R.id.progressBar);
        smalltext = (TextView) findViewById(R.id.small_text);
        txt_current_value = (TextView) findViewById(R.id.txt_current_val);
        btn_bot_bar = (ImageButton) findViewById(R.id.btn_bot_alram_bar);
        btn_up_arrow = (ImageButton) findViewById(R.id.btn_up_arrow);
        btn_down_arrow = (ImageButton) findViewById(R.id.btn_down_arrow);
        btn_full_up = (ImageButton) findViewById(R.id.btn_full_open);
        btn_full_down = (ImageButton) findViewById(R.id.btn_full_close);
        btn_25per = (ImageButton) findViewById(R.id.btn_25per);
        btn_50per = (ImageButton) findViewById(R.id.btn_50per);
        btn_75per = (ImageButton) findViewById(R.id.btn_75per);

        Blutooth_address = getIntent().getExtras().getString("address");

        //Layout Setting
        current_val = getIntent().getExtras().getInt("current_val");
        max_vlaue = getIntent().getExtras().getInt("max_value");
        smalltext.setText(getIntent().getExtras().getString("small_text"));
        if (current_val != 0) {
            progress = (int) ((float) ((float) current_val / (float) max_vlaue) * 100);
            progress_setting((int) ((float) ((float) current_val / (float) max_vlaue) * 100));
        } else {
            progress = 0;
            progress_setting(0);
        }
        room_name = getIntent().getExtras().getString("small_text");

        btn_bot_bar.setOnClickListener(viewOnClickListener);
        btn_up_arrow.setOnClickListener(viewOnClickListener);
        btn_down_arrow.setOnClickListener(viewOnClickListener);
        btn_full_up.setOnClickListener(viewOnClickListener);
        btn_full_down.setOnClickListener(viewOnClickListener);
        btn_25per.setOnClickListener(viewOnClickListener);
        btn_50per.setOnClickListener(viewOnClickListener);
        btn_75per.setOnClickListener(viewOnClickListener);
    }

    private void registerReceiver_fun() {
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TAG, "action :" + action);
                if (Bluetooth_Stat_action.equals(action)) {
                    if (intent.getExtras().getBoolean("stat")) {
                        timer.cancel();
                        pd.dismiss();
                        Log.d("TEST", " Device Is Connected!");
                    } else if (intent.getExtras().getBoolean("stat")) {
                        Log.d("TEST", " Device Is Connection Fail!");
                        unnormal_exit();
                    }
                }
            }
        };
        IntentFilter filter3 = new IntentFilter();
        filter3.addAction(Bluetooth_Stat_action);
        registerReceiver(mReceiver, filter3);
        Log.d(TAG, "register Receiver");
    }

    private void unnormal_exit()
    {
        pd.dismiss();
        timer.cancel();
        Intent exit_intent = new Intent();
        setResult(DetailActivity.BLUETOOTH_NOT_CONNECTED);
        finish();
    }


    View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_bot_alram_bar:
                    startActivity(new Intent(DetailActivity.this, AlarmActivity.class));
                    break;

                case R.id.btn_up_arrow:
                    current_val += max_vlaue / 10;
                    if (current_val > max_vlaue) {
                        current_val = max_vlaue;
                        progress_setting(100);
                    } else {
                        progress += 10;
                        progress_setting(progress);
                        BT_Send(REQUEST_UP, max_vlaue / 10);
                        DB_Set(room_name, current_val);
                    }
                    break;

                case R.id.btn_down_arrow:
                    current_val -= max_vlaue / 10;
                    progress -= 10;
                    if (current_val < 0) {
                        current_val = 0;
                        progress = 0;
                        progress_setting(0);
                    } else {
                        progress_setting(progress);
                        BT_Send(REQUEST_DOWN, max_vlaue / 10);
                        DB_Set(room_name, current_val);
                    }
                    break;

                case R.id.btn_full_open:
                    progress = 100;
                    progress_setting(progress);
                    BT_Send(REQUEST_FULL_UP, (max_vlaue - current_val));
                    current_val = max_vlaue;
                    DB_Set(room_name, max_vlaue);

                    break;

                case R.id.btn_full_close:
                    progress = 0;
                    progress_setting(progress);
                    BT_Send(REQUEST_FULL_DOWN, (current_val));
                    current_val = 0;
                    DB_Set(room_name, current_val);
                    break;

                case R.id.btn_25per:
                    if (current_val < (float) max_vlaue * 0.25) {
                        progress = 25;
                        progress_setting(progress);
                        BT_Send(REQUEST_UP, (int) ((float) max_vlaue * 0.25 - current_val));
                        current_val = (int) ((float) max_vlaue * 0.25);
                        DB_Set(room_name, current_val);
                    } else if (current_val > (float) max_vlaue * 0.25) {
                        progress = 25;
                        progress_setting(progress);
                        BT_Send(REQUEST_DOWN, (int) (current_val - (float) max_vlaue * 0.25));
                        current_val = (int) ((float) max_vlaue * 0.25);
                        DB_Set(room_name, current_val);
                    }
                    break;

                case R.id.btn_50per:
                    if (current_val < (float) max_vlaue * 0.5) {
                        progress = 50;
                        progress_setting(progress);
                        BT_Send(REQUEST_UP, (int) ((float) max_vlaue * 0.5 - current_val));
                        current_val = (int) ((float) max_vlaue * 0.5);
                        DB_Set(room_name, current_val);
                    } else if (current_val > (float) max_vlaue * 0.5) {
                        progress = 50;
                        progress_setting(progress);
                        BT_Send(REQUEST_DOWN, (int) (current_val - (float) max_vlaue * 0.5));
                        current_val = (int) ((float) max_vlaue * 0.5);
                        DB_Set(room_name, current_val);
                    }
                    break;

                case R.id.btn_75per:
                    if (current_val < (float) max_vlaue * 0.75) {
                        progress = 75;
                        progress_setting(progress);
                        BT_Send(REQUEST_UP, (int) ((float) max_vlaue * 0.75 - current_val));
                        current_val = (int) ((float) max_vlaue * 0.75);
                        DB_Set(room_name, current_val);
                    } else if (current_val > (float) max_vlaue * 0.75) {
                        progress = 75;
                        progress_setting(progress);
                        BT_Send(REQUEST_DOWN, (int) (current_val - (float) max_vlaue * 0.75));
                        current_val = (int) ((float) max_vlaue * 0.75);
                        DB_Set(room_name, current_val);
                    }
                    break;

                default:
                    Log.d(TAG, "눌리면안되는 곳이 눌리고야 말았다.");
                    break;
            }
        }
    };

    public void BT_Send(String send_data, int time) {
        String direction = send_data;
        int running_time = time;
        String Send_value = direction + ":" + running_time + "|";
        btService.write(Send_value.getBytes());
    }

    public void DB_Set(String room_name, int current_val) {
        String room = room_name;
        int value = current_val;
        dbHelper.update_current_val(room, value);
    }


    public void progress_setting(int current_val) {
        progressbar.setProgressWithAnimation(current_val);
        txt_current_value.setText("  " + current_val + "%");
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        // Get the Camera instance as the activity achieves full user focus
        registerReceiver_fun();
        pd = ProgressDialog.show(DetailActivity.this, "로딩중", "블루투스 연결 중입니다...");
        timer = new Timer();
        timer.schedule(blind_time, 0, 1000);
        btService.getDeviceInfo(getIntent()); // 블루투스 주소값 받아와서 연결하기.
        Log.d(TAG, "detail Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "detail Pause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "detail Destroy");
        btService.stop();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "detail Stop");
        btService.stop();
    }


}