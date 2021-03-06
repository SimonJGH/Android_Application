package com.simon.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author YDS
 * @date 2020/11/14
 * @discribe 1、简化事件处理 非必要初始化放在启动页执行
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG){
            // 日志开启
            ARouter.openLog();
            // 调试模式开启，如果在install run模式下运行，则必须开启调试模式
            ARouter.openDebug();
        }
        ARouter.init(this);

    }
}
