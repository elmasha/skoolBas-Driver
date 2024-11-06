package com.app.driver.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.driver.R;

public class WeatherActivity extends AppCompatActivity {

    private TextView tempp;
    private ImageView iconView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tempp = findViewById(R.id.temp);
        iconView = findViewById(R.id.iconView);


    }
}