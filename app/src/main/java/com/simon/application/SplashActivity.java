package com.simon.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

/**
 * 1、处理事件分发
 * 2、设置theme防止白屏
 * 3、统一入口 管理其它跳转 如scheme协议 推送跳转
 *
 * 跳转其它页面生命周期
 * 2020-11-11 21:00:27.314 29937-29937/com.simon.application I/Simon: SplashActivity onPause
 * 2020-11-11 21:00:27.378 29937-29937/com.simon.application I/Simon: MainActivity onCreate
 * 2020-11-11 21:00:27.382 29937-29937/com.simon.application I/Simon: MainActivity onResume
 * 2020-11-11 21:00:27.904 29937-29937/com.simon.application I/Simon: SplashActivity onStop
 *
 * 在onStop中finish防止白屏
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CountDownTimer countDownTimer = new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Simon", "SplashActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Simon", "SplashActivity onStop");
        finish();
    }

}