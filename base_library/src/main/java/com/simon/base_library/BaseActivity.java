package com.simon.base_library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.xutils.x;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        x.view().inject(this);
    }
}