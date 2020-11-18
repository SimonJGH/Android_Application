package com.simon.base_library.https;

/**
 * @author YDS
 * @date 2020/11/18
 * @discribe 请求回调
 */
public interface XhttpDataCallBack<T> {

    void onSuccess(T result);

    void onError(T errorMsg);

    void onFinished();

}
