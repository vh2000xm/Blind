package com.example.innoz.iotapplication.help_bluetooth;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.innoz.iotapplication.R;

/**
 * Created by Ahn on 2018-01-18.
 */



public class help_v1_Fragment extends Fragment {
    public help_v1_Fragment()
    {

    }

    Button btn_name;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ConstraintLayout layout = (ConstraintLayout)inflater.inflate(R.layout.help_bluetooth_v1,container, false);

        return layout;
    }
}
