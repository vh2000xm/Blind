package com.example.innoz.iotapplication.help_bluetooth;

/**
 * Created by Ahn on 2018-01-18.
 */
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.innoz.iotapplication.R;

public class help_v4_Fragment extends Fragment {
    public help_v4_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.help_bluetooth_v4, container, false);
        return layout;
    }
}
