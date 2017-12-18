package com.example.innoz.iotapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {



    ImageButton livingroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        livingroom = (ImageButton)findViewById(R.id.btn_livingroom);
        livingroom.setOnClickListener(viewOnClickListener);

    }

    View.OnClickListener viewOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_livingroom:
                    startActivity(new Intent(MainActivity.this, DetailActivity.class));
                    break;

                default:
                    Log.d("default", String.valueOf(id) + "is clicked");
            }
        }

    };
}
