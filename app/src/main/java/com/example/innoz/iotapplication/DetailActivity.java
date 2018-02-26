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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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
     * Internal User_Setting
     **/

    private final static int USER_SETTING_WELL = 20;


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
    private ImageButton btn_user_setting;
    private ImageButton back_arrow;
    private ImageButton btn_user_setting_menu;
    private PopupMenu p;
    private String str_room_name;

    private ProgressDialog pd;
    private int loading_count = 0;
    public Timer timer = null;



    private class blind_time extends TimerTask{
        public void run()
        {
            Log.e("로딩 카운터:", String.valueOf(loading_count));
            loading_count++;
            if (loading_count > 10) {
                unnormal_exit();
            }

        }
    }


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
        btn_user_setting = (ImageButton) findViewById(R.id.btn_user_setting);
        back_arrow = (ImageButton) findViewById(R.id.back_arrow);
        btn_user_setting_menu = (ImageButton) findViewById(R.id.btn_Detail_User_Setting);

        Blutooth_address = getIntent().getExtras().getString("address");

        //Layout Setting
        current_val = getIntent().getExtras().getInt("current_val");
        max_vlaue = getIntent().getExtras().getInt("max_value");

        room_name = getIntent().getExtras().getString("small_text");
        smalltext.setText(room_name);
        if (current_val != 0) {
            progress = (int) ((float) ((float) current_val / (float) max_vlaue) * 100);
            progress_setting((int) ((float) ((float) current_val / (float) max_vlaue) * 100));
        } else {
            progress = 0;
            progress_setting(0);
        }


        btn_bot_bar.setOnClickListener(viewOnClickListener);
        btn_up_arrow.setOnClickListener(viewOnClickListener);
        btn_down_arrow.setOnClickListener(viewOnClickListener);
        btn_full_up.setOnClickListener(viewOnClickListener);
        btn_full_down.setOnClickListener(viewOnClickListener);
        btn_25per.setOnClickListener(viewOnClickListener);
        btn_50per.setOnClickListener(viewOnClickListener);
        btn_75per.setOnClickListener(viewOnClickListener);
        back_arrow.setOnClickListener(viewOnClickListener);
        btn_user_setting_menu.setOnClickListener(viewOnClickListener);
        btn_user_setting.setOnClickListener(viewOnClickListener);
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
                        timer = null;
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

    PopupMenu.OnMenuItemClickListener MenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            startActivityForResult(new Intent(DetailActivity.this, User_Setting_Activity.class).putExtra("roomname",room_name),0);
            return false;
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case USER_SETTING_WELL:
                Toast.makeText(DetailActivity.this, "유저세팅이 정상적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_bot_alram_bar:
                    startActivityForResult(new Intent(DetailActivity.this, AlarmActivity.class).putExtra("roomname",room_name),0);
                    ///// 인자값 넘기기(주소, 방이름) 다이얼 만들기
                    break;

                case R.id.btn_down_arrow:
                    current_val += max_vlaue / 10;
                    if (current_val > max_vlaue) {
                        current_val = max_vlaue;
                        progress_setting(100);
                    } else {
                        progress += 10;
                        progress_setting(progress);
                        BT_Send(REQUEST_DOWN, max_vlaue / 10);
                        DB_Set(room_name, current_val);
                    }
                    break;

                case R.id.btn_up_arrow:
                    current_val -= max_vlaue / 10;
                    progress -= 10;
                    if (current_val < 0) {
                        current_val = 0;
                        progress = 0;
                        progress_setting(0);
                    } else {
                        progress_setting(progress);
                        BT_Send(REQUEST_UP, max_vlaue / 10);
                        DB_Set(room_name, current_val);
                    }
                    break;

                case R.id.btn_full_close:
                    if(!(current_val >= max_vlaue)) {
                        progress = 100;
                        progress_setting(progress);
                        BT_Send(REQUEST_DOWN, (max_vlaue - current_val));
                        current_val = max_vlaue;
                        DB_Set(room_name, max_vlaue);
                    }
                    break;

                case R.id.btn_full_open:
                    progress = 0;
                    progress_setting(progress);
                    BT_Send(REQUEST_UP, (current_val));
                    current_val = 0;
                    DB_Set(room_name, current_val);
                    break;

                case R.id.btn_25per:
                    if (current_val > (float) max_vlaue * 0.25) {
                        progress = 25;
                        progress_setting(progress);
                        BT_Send(REQUEST_UP, (int) (current_val - (float) max_vlaue * 0.25));
                        current_val = (int) ((float) max_vlaue * 0.25);
                        DB_Set(room_name, current_val);
                    } else if (current_val < (float) max_vlaue * 0.25) {
                        progress = 25;
                        progress_setting(progress);
                        BT_Send(REQUEST_DOWN, (int) ((float) max_vlaue * 0.25 - current_val));
                        current_val = (int) ((float) max_vlaue * 0.25);
                        DB_Set(room_name, current_val);
                    }
                    break;

                case R.id.btn_50per:
                    if (current_val > (float) max_vlaue * 0.5) {
                        progress = 50;
                        progress_setting(progress);
                        BT_Send(REQUEST_UP, (int) (current_val - (float) max_vlaue * 0.5));
                        current_val = (int) ((float) max_vlaue * 0.5);
                        DB_Set(room_name, current_val);
                    } else if (current_val < (float) max_vlaue * 0.5) {
                        progress = 50;
                        progress_setting(progress);
                        BT_Send(REQUEST_DOWN, (int) ((float) max_vlaue * 0.5 - current_val));
                        current_val = (int) ((float) max_vlaue * 0.5);
                        DB_Set(room_name, current_val);
                    }
                    break;

                case R.id.btn_75per:
                    if (current_val > (float) max_vlaue * 0.75) {
                        progress = 75;
                        progress_setting(progress);
                        BT_Send(REQUEST_UP, (int) (current_val - (float) max_vlaue * 0.75));
                        current_val = (int) ((float) max_vlaue * 0.75);
                        DB_Set(room_name, current_val);
                    } else if (current_val < (float) max_vlaue * 0.75) {
                        progress = 75;
                        progress_setting(progress);
                        BT_Send(REQUEST_DOWN, (int) ((float) max_vlaue * 0.75 - current_val));
                        current_val = (int) ((float) max_vlaue * 0.75);
                        DB_Set(room_name, current_val);
                    }
                    break;

                case R.id.btn_user_setting:
                    String User_setting_val = dbHelper.get_user_setting(room_name);
                    int int_User_setting = Integer.parseInt(User_setting_val);
                    Log.d(TAG,"USER_SETTING_VALUE :"+User_setting_val);

                    if(int_User_setting > 0) {

                        if (current_val > (float) max_vlaue * (float) int_User_setting / 100) {
                            progress = int_User_setting;
                            progress_setting(progress);
                            BT_Send(REQUEST_UP, (int) (current_val - (float) max_vlaue * (float) int_User_setting / 100));
                            current_val = (int) ((float) max_vlaue * (float) int_User_setting / 100);
                            DB_Set(room_name, current_val);
                        } else if (current_val < (float) max_vlaue * (float) int_User_setting / 100) {
                            progress = int_User_setting;
                            progress_setting(progress);
                            BT_Send(REQUEST_DOWN, (int) ((float) max_vlaue * (float) int_User_setting / 100 - current_val));
                            current_val = (int) ((float) max_vlaue * (float) int_User_setting / 100);
                            DB_Set(room_name, current_val);
                        }
                    }
                    else
                    {
                        Toast.makeText(DetailActivity.this,"유저세팅값이 설정되지 않았습니다",Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.back_arrow:
                    btService.stop();
                    setResult(BLUETOOTH_WELL_CONNECTED);
                    finish();
                    break;


                case R.id.btn_Detail_User_Setting:
                    p = new PopupMenu(getApplicationContext(),v);
                    getMenuInflater().inflate(R.menu.detail_menu, p.getMenu());
                    Log.d(TAG,"On User_Setting Pressed!");
                    p.setOnMenuItemClickListener(MenuItemClickListener);
                    p.show(); // 메뉴를 띄우기
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
        Log.d(TAG,"BT Send" + Send_value);
        btService.write(Send_value.getBytes());
    }

    public void DB_Set(String room_name, int current_val) {
        String room = room_name;
        int value = current_val;
        dbHelper.update_current_val(room, value);
    }


    public void progress_setting(int current_val) {
        progressbar.setProgressWithAnimation(current_val,4000);
        txt_current_value.setText("  " + current_val + "%");
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        registerReceiver_fun();
        pd = ProgressDialog.show(DetailActivity.this, "로딩중", "블루투스 연결 중입니다...");
        loading_count =0;
        timer = new Timer();
        blind_time timetesk = new blind_time();
        timer.schedule(timetesk, 0, 1000);
        btService.getDeviceInfo(getIntent()); // 블루투스 주소값 받아와서 연결하기.
        Log.d(TAG, "detail Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        btService.stop();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
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