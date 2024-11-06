package com.app.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.driver.MainActivity.DirectionsMapsActivity;
import com.app.driver.MainActivity.MapsActivity;

public class DriverSplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_splash);


        final Intent i = new Intent(this, MapsActivity.class);
        Thread timer =new Thread(){
            public void run (){
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }


        };
        timer.start();
    }
}