package com.example.innoz.iotapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Hyell on 2018-01-17.
 */

public class custum_btn extends LinearLayout {

    ImageView symbol;
    TextView text;

    public custum_btn(Context context) {

        super(context);
        initView();

    }

    public custum_btn(Context context, AttributeSet attrs) {

        super(context, attrs);

        initView();
        getAttrs(attrs);

    }

    public custum_btn(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);

    }


    private void initView() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_button, this, false);
        addView(v);

        symbol = (ImageView) findViewById(R.id.symbol);

        text = (TextView) findViewById(R.id.text);

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoginButton);

        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LoginButton, defStyle, 0);
        setTypeArray(typedArray);

    }


    private void setTypeArray(TypedArray typedArray) {

        int symbol_resID = typedArray.getResourceId(R.styleable.LoginButton_symbol, R.drawable.livingroom);
        symbol.setImageResource(symbol_resID);

        String text_string = typedArray.getString(R.styleable.LoginButton_text);
        text.setText(text_string);

        typedArray.recycle();

    }



    void setSymbol(int symbol_resID) {
        symbol.setImageResource(symbol_resID);
    }


    void setText(String text_string) {
        text.setText(text_string);
    }

    void setText(int text_resID) {
        text.setText(text_resID);
    }

}


