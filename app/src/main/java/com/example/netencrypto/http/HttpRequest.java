package com.example.netencrypto.http;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpRequest {

    private Call mCall;
    private Callback mCallback = new HttpCallback();

    public HttpRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(url).build();
        mCall = client.newCall(request);

    }

    public void request() {
        if (mCall != null) {
            if (mCall.isExecuted()) {
                mCall.clone().enqueue(mCallback);
            } else {
                mCall.enqueue(mCallback);
            }
        }
    }

}
