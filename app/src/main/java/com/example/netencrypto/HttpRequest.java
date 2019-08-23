package com.example.netencrypto;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

class HttpRequest {

    private Call mCall;
    private Callback mCallback = new HttpCallback();

    HttpRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(url).build();
        mCall = client.newCall(request);

    }

    void request() {
        if (mCall != null) {
            if (mCall.isExecuted()) {
                mCall.clone().enqueue(mCallback);
            } else {
                mCall.enqueue(mCallback);
            }
        }
    }

}
