package com.example.innoz.iotapplication;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ImageButton btn_sidemenu, btn_smallroom, btn_kitchen, btn_addroom, btn_mainroom;
    View drawerView;
    public String key;
    public String TAG="MainActivity";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

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
                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("small_text", getString(R.string.kichen)).putExtra(EXTRA_DEVICE_ADDRESS,"20:17:01:05:58:33"));
                    break;

                case R.id.btn_room1:
                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("small_text", getString(R.string.small_room)));
                    break;

                case R.id.btn_livingroom:
                    startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("small_text", getString(R.string.living_room)));
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

            case 1:
                key = data.getStringExtra("address");
                Log.d(TAG,key);
                    ////////////////////////////
                    // ADDROOM Acrivity에서 데이터값 받아서 detail 로 넘기기
                    ////////////////////////////
                break;

            default:
                break;
        }
    }


}
