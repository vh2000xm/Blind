package com.example.innoz.iotapplication;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ImageButton btn_sidemenu;
    View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);
        btn_sidemenu = (ImageButton)findViewById(R.id.btn_sidemenu) ;
        btn_sidemenu.setOnClickListener(viewOnClickListener);
        Button buttonCloseDrawer = (Button) findViewById(R.id.closedrawer);
        buttonCloseDrawer.setOnClickListener(viewOnClickListener);
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
            }
        }
    };
}
