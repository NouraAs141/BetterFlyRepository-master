package com.example.betterfly;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class vHome extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth myAuth;
    //dynamic color change attribute
private Color_Background_colors mColor =  new Color_Background_colors();
private Button mnext;
private LinearLayout mlayout;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //ID for dynamic color
            mnext = (Button) findViewById(R.id.coloring);



        //firebase
         myAuth =  FirebaseAuth.getInstance();

            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onClick(View v) {
            int color = mColor.getColor();
        switch (v.getId()) {
            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

                //button action coloring
            case R.id.coloring:
                mnext.setBackgroundColor(color);
                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
             = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(vHome.this,eventRetrievd.class));
                    finish();

                    return true;

                case R.id.profile:
                    startActivity(new Intent(vHome.this, vHome.class));
                    finish();

                    return true;

                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(vHome.this, MainActivity.class));
                    finish();
                    return true;

            }
            return false;
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
