package com.simon.base_library.https;

import android.os.Environment;

/**
 * @author YDS
 * @date 2020/11/18
 * @discribe
 */
@SuppressWarnings("all")
public class XhttpConstants {
    public static final String BASE_URL = "";

    public static final String BATH_SD_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/";

    //获取数据成功
    public static final int SUCCESS = 200;
    //获取数据+提示
    public static final int SUCCESS_CODE = 201;
    //暂无内容
    public static final int NO_DATA = 300;
    //非企业用户注册
    public static final int CODE400 = 400;
    //数据库异常
    public static final int CODE401 = 401;
    //Token失效
    public static final int CODE500 = 500;
    //其他
    public static final int CODE4012 = 4012;
}
