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

    private final static int ALRAM_SETTING_WELL = 21;
    //시작 설정 클래스
    private TimePicker mTime;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private String Blutooth_address = null;


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
    private boolean per_selected =false;

    private int Selected_HOUR;
    private int Selected_MIN;

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

        Blutooth_address = getIntent().getExtras().getString("address");

        //버튼의 리스너를 등록
        setContentView(R.layout.activity_alarm);



        OK = (Button)findViewById(R.id.btn_alram_set);
        OK.setOnClickListener (viewOnClickListener);
        cancel = findViewById(R.id.btn_alram_calcel);
        cancel.setOnClickListener(viewOnClickListener);


        mTime = (TimePicker)findViewById(R.id.time_picker);
        mTime.setOnTimeChangedListener(this);

        btn_alram_25per = (ImageButton)findViewById(R.id.btn_arlam_25per);
        btn_alram_25per.setOnClickListener(viewOnClickListener);


    }

    public class AlarmHATT {
        private Context context;

        public AlarmHATT(Context context) {
            this.context = context;
        }

        public void Alarm() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(AlarmActivity.this, Alram_BroadCast.class);
            intent.putExtra("roomname",room_name);
            intent.putExtra("percent",Selected_per);
            intent.putExtra("address", Blutooth_address);

            PendingIntent sender = PendingIntent.getBroadcast(AlarmActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            //알람시간 calendar에 set해주기

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), Selected_HOUR, Selected_MIN, 0);

            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }


    private void setAlarm() {
        new AlarmHATT(getApplicationContext()).Alarm();
    }

    View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_alram_set:
                    Log.d(TAG,"OK Button Clicked!!!");
                    if(per_selected)
                    {
                        dbHelper.update_alram(room_name,String.valueOf(Selected_per)+'|'+String.valueOf(Selected_HOUR)+'|'+String.valueOf(Selected_MIN));
                        Log.d(TAG,"DB Value :"+room_name+String.valueOf(Selected_per)+'|'+String.valueOf(Selected_HOUR)+'|'+String.valueOf(Selected_MIN));
                        setResult(ALRAM_SETTING_WELL);
                        setAlarm();
                        finish();
                    }
                    ///// 인자값 넘기기(주소, 방이름) 다이얼 만들기
                    break;

                case R.id.btn_arlam_25per:
                    Selected_per = 25;
                    per_selected = true;
                    Toast.makeText(AlarmActivity.this,"25% 선택됨",Toast.LENGTH_SHORT).show();
                    ///// 인자값 넘기기(주소, 방이름) 다이얼 만들기
                    break;

                case R.id.btn_alram_calcel:
                    setResult(0);
                    finish();
                    break;
            }
        }
    };



    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
//        mCalendar.set (year, monthOfYear, dayOfMonth, mTime.getCurrentHour(), mTime.getCurrentMinute());
        Selected_HOUR = i;
        Selected_MIN = i1;
        Log.i("HelloAlarmActivity", "Hour"+i+"min"+i1);
    }

}




