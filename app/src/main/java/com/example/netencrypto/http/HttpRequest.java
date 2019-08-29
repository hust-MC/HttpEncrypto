package com.example.netencrypto.http;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Http请求对象，封装了Http请求队列、Http配置、握手协议等方法
 */
public class HttpRequest {

    private static final String HANDSHAKE = "handshake";
    private Request.Builder mBuilder;

    /**
     * 创建一个Http请求，传入服务器的url
     *
     * @param url 服务器地址
     */
    public HttpRequest(String url) {
        mBuilder = new Request.Builder()
                .get()
                .url(url);
    }

    /**
     * 握手请求，获取AES密钥
     *
     * @param callback 请求回调
     */
    public void handshake(Callback callback) {
        // 增加Http头，表示握手连接
        mBuilder.addHeader(HANDSHAKE, HANDSHAKE);
        request(callback);
        mBuilder.removeHeader(HANDSHAKE);
    }

    /**
     * 发起Http请求
     *
     * @param callback 请求回调
     */
    public void request(Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(mBuilder.build());
        call.enqueue(callback);
    }

}
