package com.example.innoz.iotapplication;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * Created by Ahn on 2018-02-15.
 */

public class User_Setting_Activity extends Activity {

    NumberPicker nPicker;


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
                Log.d("usersetting","NumberPicker Pressed!"+nPicker.getValue());

            }
        });
    }


}
