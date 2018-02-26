package com.example.innoz.iotapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by Ahn on 2018-02-15.
 */

public class User_Setting_Activity extends Activity {



    private String TAG = "USER_SETTING_ACTIVITY";
    /**
     * Layout
     **/
    NumberPicker nPicker;
    Button OK;
    Button CANCLE;

    /**
     * Etc Service
     **/
    private SQLiteService dbHelper = null;

    /**
     * Internal User_Setting
     **/

    private final static int USER_SETTING_WELL = 20;
    private String room_name;
    private int nPick_num=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_user_setting);

        nPicker=(NumberPicker) findViewById(R.id.npicker);
        String[] nums = new String[21];
        for(int i=0; i<21; i++)
            nums[i] = Integer.toString(i*5);

        nPicker.setMinValue(0);
        nPicker.setMaxValue(20);
        nPicker.setWrapSelectorWheel(false);
        nPicker.setDisplayedValues(nums);
        nPicker.setValue(0);
        nPicker.setVisibility(View.VISIBLE);
        nPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        nPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d("usersetting","NumberPicker Pressed!"+newVal);
                nPick_num = newVal;
            }
        });
        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }

        room_name = getIntent().getExtras().getString("roomname");

        OK = (Button)findViewById(R.id.btn_user_set_OK);

        OK.setOnClickListener(viewOnClickListener);
    }

    View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_user_set_OK:
                    Log.d(TAG,"Picker_NUM : "+nPick_num*5);
                    dbHelper.update_User_set_val(room_name,nPick_num*5);
                    setResult(USER_SETTING_WELL);
                    finish();
                    break;
            }
        }
    };


}
