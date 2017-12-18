package com.example.innoz.iotapplication;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ImageButton btn_sidemenu, btn_smallroom, btn_kitchen, btn_addroom, btn_mainroom;
    View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

//        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.rooms, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);

        btn_smallroom = (ImageButton)findViewById(R.id.btn_room1) ;
        btn_sidemenu = (ImageButton)findViewById(R.id.btn_sidemenu) ;
        btn_kitchen = (ImageButton)findViewById(R.id.btn_kitchen) ;
        btn_mainroom = (ImageButton)findViewById(R.id.btn_livingroom) ;
        btn_addroom= (ImageButton)findViewById(R.id.btn_addroom) ;


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
                    startActivity(new Intent(MainActivity.this, DetailActivity.class));
                    break;

                case R.id.btn_room1:
                    startActivity(new Intent(MainActivity.this, DetailActivity.class));
                    break;

                case R.id.btn_livingroom:
                    startActivity(new Intent(MainActivity.this, DetailActivity.class));
                    break;

                case R.id.btn_addroom:
                    startActivity(new Intent(MainActivity.this, Addroom_Activity.class));
                    break;

                default:
                    Log.d("MainActivity", String.valueOf(id) + "is clicked");
            }
        }
    };
}
