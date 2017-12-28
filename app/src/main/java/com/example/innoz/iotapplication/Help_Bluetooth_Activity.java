package com.example.innoz.iotapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Ahn on 2017-12-28.
 */

public class Help_Bluetooth_Activity extends Activity {

    Button Bluetooth_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SlidingView sv = new SlidingView(this);
        View v1 = View.inflate(this, R.layout.help_bluetooth_v1, null);
        View v2 = View.inflate(this, R.layout.help_bluetooth_v2, null);
        sv.addView(v1);
        sv.addView(v2);
        setContentView(sv);
        Bluetooth_ok = (Button)findViewById(R.id.bluetooth_ok);
        Bluetooth_ok.setOnClickListener(viewOnClickListener);
    }

    View.OnClickListener viewOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.bluetooth_ok:
                    startActivity(new Intent(Help_Bluetooth_Activity.this, Addroom_Activity.class));
                    finish();
                    break;
                default:
                    Log.d("default", String.valueOf(id) + "is clicked");
            }
        }
    };
}
