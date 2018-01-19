package com.example.innoz.iotapplication;

import android.content.Intent;
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

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

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
    /**
     * Bluetooth Movement
     **/
    private static final String REQUEST_DOWN = "DO";
    private static final String REQUEST_UP = "UP";
    private static final String REQUEST_FULL_UP = "FU";
    private static final String REQUEST_FULL_DOWN = "FD";


    /**
     * Internal Movement Value
     **/
    private int current_val =0;

    /**
     * Layout
     **/
     private TextView smalltext;
    private TextView txt_current_value;
    public CircularProgressBar progressbar;
    private ImageButton btn_bot_bar;
    private ImageButton btn_up_arrow;
    private ImageButton btn_down_arrow;
    private ImageButton btn_full_up;
    private ImageButton btn_full_down;

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
        if (btService == null) {
            btService = new BluetoothService(this, mHandler);
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
        Blutooth_address = getIntent().getExtras().getString("address");

        //Layout Setting
        smalltext.setText(getIntent().getExtras().getString("small_text"));
        progress_setting(current_val);

        btService.getDeviceInfo(getIntent()); // 블루투스 주소값 받아와서 연결하기.
        // MainActivity 에서 값 받아와서 smalltext 값 변경하기.
        btn_bot_bar.setOnClickListener(viewOnClickListener);
        btn_up_arrow.setOnClickListener(viewOnClickListener);
        btn_down_arrow.setOnClickListener(viewOnClickListener);
        btn_full_up.setOnClickListener(viewOnClickListener);
        btn_full_down.setOnClickListener(viewOnClickListener);
        //블루투스 연결 체크
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
                    current_val += 10;
                    progress_setting(current_val);
                    BT_Send(REQUEST_UP,3);
                    break;

                case R.id.btn_down_arrow:
                    current_val -= 10;
                    if(current_val < 0)
                        current_val =0;
                    else {
                        progress_setting(current_val);
                        BT_Send(REQUEST_DOWN, 3);
                    }
                    break;

                case R.id.btn_full_open:
                    current_val = 100;
                    progress_setting(current_val);
                    BT_Send(REQUEST_FULL_UP,10);
                    break;

                case R.id.btn_full_close:
                    current_val = 0;
                    progress_setting(current_val);
                    BT_Send(REQUEST_FULL_DOWN,10);
                    break;

                default:
                    Log.d(TAG, "눌리면안되는 곳이 눌리고야 말았다.");
                    break;
            }
        }
    };

    public void BT_Send(String send_data, int time)
    {
        String direction = send_data;
        int running_time = time;
        String Send_value = direction +":"+ running_time + "|";
        btService.write(Send_value.getBytes());
    }

    public void progress_setting(int current_val)
    {
        progressbar.setProgressWithAnimation(current_val);
        txt_current_value.setText("  "+current_val+"%");
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        // Get the Camera instance as the activity achieves full user focus
        Log.d(TAG, "detail Resume");
        if (btService.getState() == 0) {
            btService.getDeviceInfo(getIntent());
        }
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
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "detail Stop");
        //btService.stop();
    }


}