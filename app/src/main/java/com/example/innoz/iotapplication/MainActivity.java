package com.example.innoz.iotapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ImageButton btn_sidemenu, btn_smallroom, btn_kitchen, btn_addroom, btn_mainroom;
    View drawerView;
    public String TAG = "MainActivity";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private SQLiteService dbHelper = null;
    public String button_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }
        Log.d(TAG, "db value : " + dbHelper.getResult());

        button_num = dbHelper.select_last();

        Log.d(TAG, "Button _Num :" + button_num);
        Set_Layout(Integer.parseInt(button_num));


        // db 값 읽어서 동적생성

//        btn_smallroom = (ImageButton) findViewById(R.id.btn_room1);
//        btn_sidemenu = (ImageButton) findViewById(R.id.btn_sidemenu);
//        btn_kitchen = (ImageButton) findViewById(R.id.btn_kitchen);
//        btn_mainroom = (ImageButton) findViewById(R.id.btn_livingroom);
//        btn_addroom = (ImageButton) findViewById(R.id.btn_addroom);
//
//
//        btn_smallroom.setOnClickListener(viewOnClickListener);
//        btn_sidemenu.setOnClickListener(viewOnClickListener);
//        btn_kitchen.setOnClickListener(viewOnClickListener);
//        btn_mainroom.setOnClickListener(viewOnClickListener);
//        btn_addroom.setOnClickListener(viewOnClickListener);


        Button buttonCloseDrawer = (Button) findViewById(R.id.closedrawer);
//        buttonCloseDrawer.setOnClickListener(viewOnClickListener);


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Main Resume");
        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }
        Log.d(TAG, "db value : " + dbHelper.getResult());
//        dbcheck(dbHelper);

//        String device_address;
//        String room_name;
//        // Shared Preferences 값 불러오기
//        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
//        device_address = pref.getString("address", "");
//        room_name = pref.getString("room_name", "");
//        /// shred preference check
//        if (device_address != null && room_name != null) {
//            Log.d(TAG, "device address : " + device_address);
//            Log.d(TAG, "Room_Name : " + room_name);
//        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
//
//            int id = v.getId();
//            switch (id) {
//                case R.id.btn_sidemenu:
//                    mDrawerLayout.openDrawer(drawerView);
//                    break;
//                case R.id.closedrawer:
//                    mDrawerLayout.closeDrawers();
//                    break;
//
//                case R.id.btn_kitchen:
//                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("small_text", getString(R.string.kichen)).putExtra(EXTRA_DEVICE_ADDRESS, "20:17:01:05:58:33"));
//                    break;
//
//                case R.id.btn_room1:
//                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("small_text", getString(R.string.small_room)));
//                    break;
//
//                case R.id.btn_livingroom:
//                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("small_text", getString(R.string.living_room)).putExtra(EXTRA_DEVICE_ADDRESS, "20:17:01:05:58:33"));
//                    break;
//
//                case R.id.btn_addroom:
//                    startActivity(new Intent(MainActivity.this, Help_Bluetooth_Activity.class));
//                    //startActivityForResult(new Intent(MainActivity.this, Addroom_Activity.class), 0);
//                    break;
//
//                default:
//                    Log.d("MainActivity", String.valueOf(id) + "is clicked");
//            }
//        }
//    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {

            case 1: // Accept Button Clicked in addroom activity..
                String device_address = null;
                String Room_name = null;
                device_address = data.getStringExtra("address");
                Room_name = data.getExtras().getString("room");
                if (device_address != null && Room_name != null) {
                    Log.d(TAG, device_address);
                    Log.d(TAG, Room_name);
                    ////////////////////////////
                    // ADDROOM Acrivity에서 데이터값 받아서 shared preference 저장.
                    ////////////////////////////
                } else {
                    Toast.makeText(this, " 기기가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    void Set_Layout(int button_num) {
        boolean lest_btn = false;
        int btn_cnt = 2;
        int line = btn_cnt / 2;
        boolean btn_per_line = false;
        TableLayout tableLayout = (TableLayout) findViewById(R.id.layout_table);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        TableRow row[] = new TableRow[(btn_cnt / 2) + 1];
        ImageButton btn[] = new ImageButton[btn_cnt + 1];
        FrameLayout frame[] = new FrameLayout[btn_cnt + 1];
        TextView txt[] = new TextView[btn_cnt + 1];

        if (btn_cnt % 2 == 1) {
            lest_btn = true;
        }

        for (int tr = 0; tr < line; tr++) {
            row[tr] = new TableRow(this);
            row[tr].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    0,
                    1));
            row[tr].setGravity(Gravity.CENTER);


            for (int td = 0; td < 2; td++) {
                frame[td] = new FrameLayout(this);
//                frame[td].setLayoutParams(new FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.WRAP_CONTENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                ));
                frame[td].setForegroundGravity(Gravity.CENTER);
//                frame[td].setLayoutParams(new TableRow.LayoutParams(400,390,TableRow.LayoutParams.WRAP_CONTENT));
                frame[td].setVisibility(View.VISIBLE);
                frame[td].setPadding(10, 10, 10, 10);

                btn[td] = new ImageButton(this);
                btn[td].setBackgroundResource(R.drawable.sample_room);
//                btn[td].setLayoutParams(new TableRow.LayoutParams(
//                        0,
//                        TableRow.LayoutParams.WRAP_CONTENT,
//                        1));
                btn[td].setLayoutParams(new TableRow.LayoutParams(380, 370, TableRow.LayoutParams.WRAP_CONTENT));
                btn[td].setScaleType(ImageButton.ScaleType.FIT_CENTER);
                btn[td].setVisibility(View.VISIBLE);
                btn[td].setPadding(0, 0, 0, 0);
                btn[td].setAdjustViewBounds(true);
                btn[td].setOnClickListener(viewOnClickListener);
                // Image Button Param Set
                txt[td] = new TextView(this);
                txt[td].setText("침실");
                txt[td].setTextColor(R.color.Main_Blue);
                txt[td].setTextSize(20);
                txt[td].setGravity(Gravity.CENTER);
//                txt[td].setGravity(Gravity.BOTTOM);
                txt[td].setPadding(0, 220, 0, 0);
                txt[td].setVisibility(View.VISIBLE);
                frame[td].addView(btn[td]);
                frame[td].addView(txt[td]);
                row[tr].addView(frame[td]);
//                setContentView(row[td]);
//                row[tr].addView(txt[td]);
            }
            tableLayout.addView(row[tr]);
        }
        if (lest_btn) {
            TableRow last_row = null;
            FrameLayout last_btn_frame =null;
            ImageButton last_btn = null;
            TextView last_text = null;
            last_row= new TableRow(this);
            last_row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    0,
                    1));
            last_row.setGravity(Gravity.CENTER);

            last_btn_frame = new FrameLayout(this);
            last_btn_frame.setForegroundGravity(Gravity.CENTER);
            last_btn_frame.setVisibility(View.VISIBLE);
            last_btn_frame.setPadding(10, 10, 10, 10);

            last_btn = new ImageButton(this);
            last_btn.setBackgroundResource(R.drawable.sample_room);
            last_btn.setLayoutParams(new TableRow.LayoutParams(380, 370, TableRow.LayoutParams.WRAP_CONTENT));
            last_btn.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            last_btn.setVisibility(View.VISIBLE);
            last_btn.setPadding(0, 0, 0, 0);
            last_btn.setAdjustViewBounds(true);
            last_btn.setOnClickListener(viewOnClickListener);
            // Image Button Param Set

            last_text = new TextView(this);
            last_text.setText("침실");
            last_text.setTextColor(R.color.Main_Blue);
            last_text.setTextSize(20);
            last_text.setGravity(Gravity.CENTER);
//                txt[td].setGravity(Gravity.BOTTOM);
            last_text.setPadding(0, 220, 0, 0);
            last_text.setVisibility(View.VISIBLE);

            last_btn_frame.addView(last_btn);
            last_btn_frame.addView(last_text);


            //////////////////

            FrameLayout addroom_frame = null;
            ImageButton addroom_btn = null;

            addroom_frame = new FrameLayout(this);
            addroom_frame.setForegroundGravity(Gravity.CENTER);
            addroom_frame.setVisibility(View.VISIBLE);
            addroom_frame.setPadding(10, 10, 10, 10);

            addroom_btn = new ImageButton(this);
            addroom_btn.setBackgroundResource(R.drawable.add_room);
            addroom_btn.setLayoutParams(new TableRow.LayoutParams(380, 370, TableRow.LayoutParams.WRAP_CONTENT));
            addroom_btn.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            addroom_btn.setVisibility(View.VISIBLE);
            addroom_btn.setPadding(0, 0, 0, 0);
            addroom_btn.setAdjustViewBounds(true);
            addroom_btn.setOnClickListener(viewOnClickListener);
            // Image Button Param Set

            addroom_frame.addView(addroom_btn);
            last_row.addView(last_btn_frame);
            last_row.addView(addroom_frame);

            tableLayout.addView(last_row);
        }

        else
        {
            TableRow last_row = null;
            FrameLayout addroom_frame = null;
            ImageButton addroom_btn = null;

            addroom_frame = new FrameLayout(this);
            addroom_frame.setForegroundGravity(Gravity.CENTER);
            addroom_frame.setVisibility(View.VISIBLE);
            addroom_frame.setPadding(10, 10, 10, 10);

            addroom_btn = new ImageButton(this);
            addroom_btn.setBackgroundResource(R.drawable.add_room);
            addroom_btn.setLayoutParams(new TableRow.LayoutParams(380, 370, TableRow.LayoutParams.WRAP_CONTENT));
            addroom_btn.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            addroom_btn.setVisibility(View.VISIBLE);
            addroom_btn.setPadding(0, 0, 0, 0);
            addroom_btn.setAdjustViewBounds(true);
            addroom_btn.setOnClickListener(viewOnClickListener);
            // Image Button Param Set



            ///////////////

            FrameLayout last_btn_frame =null;
            ImageButton last_btn = null;
            TextView last_text = null;
            last_row= new TableRow(this);
            last_row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    0,
                    1));
            last_row.setGravity(Gravity.CENTER);

            last_btn_frame = new FrameLayout(this);
            last_btn_frame.setForegroundGravity(Gravity.CENTER);
            last_btn_frame.setVisibility(View.VISIBLE);
            last_btn_frame.setPadding(10, 10, 10, 10);
            last_btn_frame.setVisibility(View.INVISIBLE);

            last_btn = new ImageButton(this);
            //last_btn.setBackgroundResource(R.drawable.sample_room);
            last_btn.setBackgroundColor(R.color.white);
            last_btn.setLayoutParams(new TableRow.LayoutParams(380, 370, TableRow.LayoutParams.WRAP_CONTENT));
            last_btn.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            last_btn.setVisibility(View.VISIBLE);
            last_btn.setPadding(0, 0, 0, 0);
            last_btn.setAdjustViewBounds(true);
            last_btn.setOnClickListener(viewOnClickListener);
            // Image Button Param Set
            last_btn_frame.addView(last_btn);
            addroom_frame.addView(addroom_btn);
            last_row.addView(addroom_frame);
            last_row.addView(last_btn_frame);
            tableLayout.addView(last_row);
        }

        TableRow margin_row = new TableRow(this);
        margin_row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                0,
                1));
        tableLayout.addView(margin_row);
    }

    void dbcheck(SQLiteService dbHelper) {
        int last_num;
        //last_num = dbHelper.select_last();
        //TableLayout tableLayout = (TableLayout) findViewById(R.id.layout_table);

        // num 개수에 따라 ROW 개수 설정하기..
//        if(last_num % 2 == 0) {
//            initializeMap(tableLayout, last_num / 2, 2);
//        }
//        else
//        {
//            initializeMap(tableLayout, (last_num / 2)+1, 2);
//        }
    }

//    @SuppressLint("ResourceAsColor")
//    void custom_btn(ImageButton btn, TextView txtV, String str){
//
//        btn.setBackgroundResource(R.drawable.sample_room);
////        @SuppressLint("ResourceType") ImageButton img = (ImageButton)findViewById(R.layout.custom_button);
////        ViewGroup.LayoutParams params = img.getLayoutParams();
//////        params.width = widthPixels;
////        btn.setMaxWidth(10);
////        btn.setMaxHeight(5);
//        btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        btn.setPadding(0,0,0,0);
//
//        txtV.setText(str);
//        txtV.setTextColor(R.color.Main_Blue);
//        txtV.setTextSize(14);
//        txtV.setGravity(Gravity.BOTTOM);
//        txtV.setGravity(Gravity.CENTER_HORIZONTAL);
//        txtV.setPadding(0,0,0,22);
//
//        btn.setVisibility(View.VISIBLE);
//        txtV.setVisibility(View.VISIBLE);
//
//    }
}