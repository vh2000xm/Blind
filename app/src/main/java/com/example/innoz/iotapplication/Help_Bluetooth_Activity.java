package com.example.innoz.iotapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Ahn on 2017-12-28.
 */

public class Help_Bluetooth_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlidingView sv = new SlidingView(this);
        View v1 = View.inflate(this, R.layout.help_bluetooth_v1, null);
        View v2 = View.inflate(this, R.layout.help_bluetooth_v2, null);
        sv.addView(v1);
        sv.addView(v2);
        setContentView(sv);

    }
}
