package com.example.netencrypto.http;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HttpCallback implements Callback {

    private static final String TAG = "MCLOG";

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, "Get方式请求失败");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.e(TAG, "Get方式请求成功，result--->" + response.body().string());

    }
}