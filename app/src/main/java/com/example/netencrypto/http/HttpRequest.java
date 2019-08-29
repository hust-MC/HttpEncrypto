package com.example.netencrypto.http;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpRequest {

    public static final String HANDSHAKE = "handshake";
    private Request.Builder mBuilder;

    public HttpRequest(String url) {
        mBuilder = new Request.Builder()
                .get()
                .url(url);
    }

    public void handshake(Callback callback) {
        mBuilder.addHeader(HANDSHAKE, HANDSHAKE);
        request(callback);
        mBuilder.removeHeader(HANDSHAKE);
    }

    public void request(Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(mBuilder.build());
        call.enqueue(callback);
    }

}
