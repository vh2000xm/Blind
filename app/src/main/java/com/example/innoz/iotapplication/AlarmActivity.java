package com.example.innoz.iotapplication;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ahn on 2018-01-06.
 */



public class AlarmActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener {


    // 알람 메니저
    private AlarmManager mManager;
    // 설정 일시
    private GregorianCalendar mCalendar;
    //        //일자 설정 클래스
//        private DatePicker mDate;
    //시작 설정 클래스
    private TimePicker mTime;

    /*
 * 통지 관련 맴버 변수
 */
    private NotificationManager mNotification;

    private ImageButton btn_alram_25per;
    private ImageButton btn_alram_50per;
    private ImageButton btn_alram_75per;
    private Button OK;
    private Button cancel;
    private String TAG = "AlarmActivity";
    private int Selected_per;

    private SQLiteService dbHelper = null;

    private String room_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //통지 매니저를 취득
        mNotification = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        //알람 매니저를 취득
        mManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //현재 시각을 취득
        mCalendar = new GregorianCalendar();
        Log.i("HelloAlarmActivity",mCalendar.getTime().toString());



        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }
        room_name = getIntent().getExtras().getString("roomname");

        //버튼의 리스너를 등록
        setContentView(R.layout.activity_alarm);
        OK = (Button)findViewById(R.id.btn_alram_set);
        OK.setOnClickListener (viewOnClickListener);
        cancel = findViewById(R.id.btn_alram_calcel);
        cancel.setOnClickListener(new Button.OnClickListener() {public void onClick(View v) {resetAlarm();}});


        mTime = (TimePicker)findViewById(R.id.time_picker);
        mTime.setOnTimeChangedListener(this);

        btn_alram_25per = (ImageButton)findViewById(R.id.btn_arlam_25per);
        btn_alram_25per.setOnClickListener(viewOnClickListener);


    }

    private void resetAlarm() {
        mManager.cancel(pendingIntent());
    }

    private void setAlarm() {
        mManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent());
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
    }

    //알람의 설정 시각에 발생하는 인텐트 작성
    private PendingIntent pendingIntent() {
        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        return pi;

    }

    View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_alram_set:
                    Log.d(TAG,"OK Button Clicked!!!");

                    finish();
                    ///// 인자값 넘기기(주소, 방이름) 다이얼 만들기
                    break;

                case R.id.btn_arlam_25per:
                    Selected_per = 25;
                    Toast.makeText(AlarmActivity.this,"25% 선택됨",Toast.LENGTH_SHORT).show();
                    ///// 인자값 넘기기(주소, 방이름) 다이얼 만들기
                    break;
            }
        }
    };



    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
//        mCalendar.set (year, monthOfYear, dayOfMonth, mTime.getCurrentHour(), mTime.getCurrentMinute());
        mCalendar.set(Calendar.HOUR_OF_DAY,i);
        mCalendar.set(Calendar.MINUTE,i1);
        Log.i("HelloAlarmActivity", "Hour"+i+"min"+i1);
    }

}




