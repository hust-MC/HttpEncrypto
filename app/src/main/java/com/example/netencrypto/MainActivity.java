package com.example.netencrypto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MCLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url("http://172.24.123.30").build();
        final Call call = client.newCall(request);
        final Callback callback = new HttpCallback();
        call.enqueue(callback);


        findViewById(R.id.send_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.clone().enqueue(callback);
            }
        });
    }


    private class HttpCallback implements Callback {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "Get方式请求失败");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.e(TAG, "Get方式请求成功，result--->" + response.body().string());

        }
    }
}
