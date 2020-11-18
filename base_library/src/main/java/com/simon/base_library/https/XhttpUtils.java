package com.simon.base_library.https;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author YDS
 * @date 2020/11/18
 * @discribe
 */
@SuppressWarnings("all")
public class XhttpUtils {

    private static final XhttpUtils mHttpXutils = new XhttpUtils();

    private XhttpUtils() {
    }

    public static XhttpUtils getInstance() {
        return mHttpXutils;
    }

    /**
     * restful接口请求
     *
     * @param method   HttpMethod请求方法
     * @param params   equestParams请求参数
     * @param callBack XhttpCallBack结果回调
     */
    public void requestRestful(HttpMethod method, RequestParams params, final XhttpDataCallBack<String> callBack) {
        params.addHeader("X-Auth-Token", "TOKEN");
        //params.setAsJsonContent(true);
        //params.setBodyContent(json);

        x.http().request(method, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                initRequestCode(result, callBack);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.onError(cex.getMessage());
            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 下载文件 不需要传入下载路径，默认储存sd卡download文件夹，名称为网络文件名称或时间戳
     *
     * @param url      网络文件地址
     * @param callBack XhttpFileCallBack文件结果回调
     */
    public void downloadFile(String url, XhttpFileCallBack<File> callBack) {
        downloadFile(url, XhttpConstants.BATH_SD_PATH + parseFileName(url), callBack);
    }

    /**
     * 下载文件
     *
     * @param url      网络文件地址
     * @param path     本地储存文件路径（含文件名称）
     * @param callBack XhttpFileCallBack文件结果回调
     */
    public void downloadFile(String url, String path, XhttpFileCallBack<File> callBack) {
        RequestParams params = new RequestParams(XhttpConstants.BASE_URL + url);
        params.addHeader("X-Auth-Token", "TOKEN");
        params.setSaveFilePath(path);
        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                callBack.onProgress((int) (current / total));
            }

            @Override
            public void onSuccess(File file) {
                callBack.onSuccess(file);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.onError(cex.getMessage());
            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 多文件上次
     *
     * @param objectMap
     * @param url
     * @param paths
     * @param callBack
     */
    public void uploadFile(Map<String, Object> objectMap, String url, List<String> paths, XhttpFileCallBack<File> callBack) {
        JSONObject json = new JSONObject(objectMap);
        RequestParams params = new RequestParams(XhttpConstants.BASE_URL + url);
        params.addHeader("X-Auth-Token", "TOKEN");
        params.setAsJsonContent(true);
        //判断文件路径是否为空
        List<KeyValue> list = new ArrayList<>();
        if (paths != null || paths.isEmpty()) {
            callBack.onError("upload file path is empty");
            return;
        }
        //多文件添加
        for (String path : paths) {
            list.add(new KeyValue("file", new File(path)));
            if (!TextUtils.isEmpty(json.toString())) {
                list.add(new KeyValue("parameters", json.toString()));
            }
        }
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);

        x.http().post(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                callBack.onProgress((int) (current / total));
            }

            @Override
            public void onSuccess(File file) {
                callBack.onSuccess(file);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callBack.onError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.onError(cex.getMessage());
            }

            @Override
            public void onFinished() {
                callBack.onFinished();
            }
        });
    }

    /**
     * 处理请求结果码
     *
     * @param result
     * @param commonCallBack
     */
    private void initRequestCode(String result, XhttpDataCallBack xutilCallBack) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            switch (code) {
                case XhttpConstants.CODE500:
                    /*Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(MyApplication.getInstance(), LoginActivity.class);
                    startActivity(intent);*/
                    break;
                case XhttpConstants.SUCCESS:
                    xutilCallBack.onSuccess(result);
                    break;
                case XhttpConstants.SUCCESS_CODE:
                    xutilCallBack.onSuccess(result);
                    break;
                case XhttpConstants.NO_DATA:
                    xutilCallBack.onSuccess(result);
                    break;
                default:
                    xutilCallBack.onError(msg);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            xutilCallBack.onError(e.getMessage());
        }
    }

    /**
     * 利用文件url转换出文件名
     *
     * @param url
     * @return
     */
    public static String parseFileName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }


}
