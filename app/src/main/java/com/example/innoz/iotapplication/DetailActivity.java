package com.example.innoz.iotapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

/**
 * Created by Ahn on 2017-12-19.
 */

public class DetailActivity extends AppCompatActivity {


    private BluetoothService btService = null;

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

        if(btService == null) {
            btService = new BluetoothService(this, mHandler);
        }

    }
}
