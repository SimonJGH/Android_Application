package com.simon.application;

import android.app.Application;

/**
 * @author YDS
 * @date 2020/11/14
 * @discribe 1、简化事件处理 非必要初始化放在启动页执行
 */
class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
