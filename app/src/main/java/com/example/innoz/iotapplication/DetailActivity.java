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

        Log.d(TAG,getIntent().getExtras().getString("small_text"));


        //Layout Init
        progressbar = (CircularProgressBar)findViewById(R.id.progressBar);
        smalltext = (TextView)findViewById(R.id.small_text);

        //Layout Setting
        smalltext.setText(getIntent().getExtras().getString("small_text"));
        progressbar.setProgressWithAnimation(30);
        // MainActivity 에서 값 받아와서 smalltext 값 변경하기.
        // 블루투스 주소값 받아와서 연결하기.

        if(btService == null) {
            btService = new BluetoothService(this, mHandler);
        }

    }
}
