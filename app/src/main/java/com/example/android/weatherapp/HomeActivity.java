package com.example.android.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container,new WeatherFragment()).commit();

        }
    }
}
