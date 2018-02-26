package com.example.innoz.iotapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.NumberPicker;

/**
 * Created by Ahn on 2018-01-06.
 */

public class AlarmActivity  extends Activity {


    ImageButton btn_alram_25per;
    String TAG = "AlarmActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_alarm);

        btn_alram_25per = (ImageButton)findViewById(R.id.btn_arlam_25per);
        btn_alram_25per.setOnClickListener(viewOnClickListener);


    }

    View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_arlam_25per:
                    Log.d(TAG,"25per Clicked!!!");
                    ///// 인자값 넘기기(주소, 방이름) 다이얼 만들기
                    break;
            }
        }
    };

}
