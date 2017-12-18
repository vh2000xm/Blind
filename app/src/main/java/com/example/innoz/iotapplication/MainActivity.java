package com.example.innoz.iotapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {



    ImageButton addroom;
    ListView listview = null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addroom = (ImageButton)findViewById(R.id.btn_addroom);
        addroom.setOnClickListener(viewOnClickListener);

    }

    View.OnClickListener viewOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_addroom:
                    startActivity(new Intent(MainActivity.this, DetailActivity.class));
                    break;
                case R.id.btn_sidemenu:
                    // 사이드 메뉴 추가하기
                    break;

                default:
                    Log.d("default", String.valueOf(id) + "is clicked");
            }
        }
    };
}
