package com.example.innoz.iotapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

/**
 * Created by Ahn on 2017-12-19.
 */

public class DetailActivity extends AppCompatActivity {

    static final String TAG = "DetailActivity";
    private TextView smalltext;
    private BluetoothService btService = null;
    private String Blutooth_address= null;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";



    //Layout
    public CircularProgressBar progressbar;

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
        Log.d(TAG, getIntent().getExtras().getString(EXTRA_DEVICE_ADDRESS));

        //Layout Init
        progressbar = (CircularProgressBar) findViewById(R.id.progressBar);
        smalltext = (TextView) findViewById(R.id.small_text);
        Blutooth_address = getIntent().getExtras().getString("address");

        //Layout Setting
        smalltext.setText(getIntent().getExtras().getString("small_text"));
        progressbar.setProgressWithAnimation(30);

        btService.getDeviceInfo(getIntent()); // 블루투스 주소값 받아와서 연결하기.
        // MainActivity 에서 값 받아와서 smalltext 값 변경하기.

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        // Get the Camera instance as the activity achieves full user focus
        Log.d(TAG,"detail Resume");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG,"detail Pause");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG,"detail Destroy");
        //btService.stop();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.d(TAG,"detail Stop");
        btService.stop();
    }


}
