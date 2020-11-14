package com.simon.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Simon", "MainActivity onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Simon", "MainActivity onResume");
    }

}