package com.example.innoz.iotapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ImageButton btn_sidemenu, btn_smallroom, btn_kitchen, btn_addroom, btn_mainroom;
    View drawerView;
    public String TAG = "MainActivity";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private SQLiteService dbHelper = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);
       // Set_Layout(2);

        btn_smallroom = (ImageButton) findViewById(R.id.btn_room1);
        btn_sidemenu = (ImageButton) findViewById(R.id.btn_sidemenu);
        btn_kitchen = (ImageButton) findViewById(R.id.btn_kitchen);
        btn_mainroom = (ImageButton) findViewById(R.id.btn_livingroom);
        btn_addroom = (ImageButton) findViewById(R.id.btn_addroom);


        btn_smallroom.setOnClickListener(viewOnClickListener);
        btn_sidemenu.setOnClickListener(viewOnClickListener);
        btn_kitchen.setOnClickListener(viewOnClickListener);
        btn_mainroom.setOnClickListener(viewOnClickListener);
        btn_addroom.setOnClickListener(viewOnClickListener);


        Button buttonCloseDrawer = (Button) findViewById(R.id.closedrawer);
        buttonCloseDrawer.setOnClickListener(viewOnClickListener);

        if (dbHelper == null) {
            dbHelper = new SQLiteService(getApplicationContext(), "BLUETOOTH_INFO.db", null, 1);
        }
        Log.d(TAG, "db value : " + dbHelper.getResult());
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

            int id = v.getId();
            switch (id) {
                case R.id.btn_sidemenu:
                    mDrawerLayout.openDrawer(drawerView);
                    break;
                case R.id.closedrawer:
                    mDrawerLayout.closeDrawers();
                    break;

                case R.id.btn_kitchen:
                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("small_text", getString(R.string.kichen)).putExtra(EXTRA_DEVICE_ADDRESS, "20:17:01:05:58:33"));
                    break;

                case R.id.btn_room1:
                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("small_text", getString(R.string.small_room)));
                    break;

                case R.id.btn_livingroom:
                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("small_text", getString(R.string.living_room)).putExtra(EXTRA_DEVICE_ADDRESS, "20:17:01:05:58:33"));
                    break;

                case R.id.btn_addroom:
                    //startActivity(new Intent(MainActivity.this, Help_Bluetooth_Activity.class));
                    startActivityForResult(new Intent(MainActivity.this, Addroom_Activity.class), 0);
                    break;

                default:
                    Log.d("MainActivity", String.valueOf(id) + "is clicked");
            }
        }
    };

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

    void Set_Layout(int line) {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.layout_table);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        TableRow row[] = new TableRow[7];
        ImageButton btn[] = new ImageButton[7];
        FrameLayout frame[] = new FrameLayout[7];

        for (int tr = 0; tr < line; tr++) {
            row[tr] = new TableRow(this);
            row[tr].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    0,
                    1));


            for (int td = 0; td < 2; td++) {
//                frame[td] = new FrameLayout(this);
//                frame[td].setLayoutParams(new FrameLayout.LayoutParams(
//                        FrameLayout.LayoutParams.WRAP_CONTENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT
//                ));
//                frame[td].setForegroundGravity(Gravity.CENTER);
//                frame[td].setVisibility(View.VISIBLE);

                btn[td] = new ImageButton(this);
                btn[td].setBackgroundResource(R.drawable.sample_room);
                btn[td].setLayoutParams(new TableRow.LayoutParams(
                        0,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1));

                btn[td].setVisibility(View.VISIBLE);
                btn[td].setPadding(0,0,0,0);
                btn[td].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                btn[td].setAdjustViewBounds(true);
                btn[td].setOnClickListener(viewOnClickListener);
                // Image Button Param Set

//                frame[td].addView(btn[td]);
                row[tr].addView(btn[td]);
            }
            tableLayout.addView(row[tr]);
        }
        TableRow margin_row = new TableRow(this);
        margin_row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                0,
                1));
        tableLayout.addView(margin_row);
    }

    void dbcheck(SQLiteService dbHelper) {
        int last_num;
        last_num = dbHelper.select_last();
        TableLayout tableLayout = (TableLayout) findViewById(R.id.layout_table);

        // num 개수에 따라 ROW 개수 설정하기..
//        if(last_num % 2 == 0) {
//            initializeMap(tableLayout, last_num / 2, 2);
//        }
//        else
//        {
//            initializeMap(tableLayout, (last_num / 2)+1, 2);
//        }
    }
}
