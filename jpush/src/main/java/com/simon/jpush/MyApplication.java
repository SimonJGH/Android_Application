package com.simon.jpush;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import cn.jpush.android.api.JPushInterface;


/**
 * @author YDS
 * @date 2020/11/17
 * @discribe
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

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
