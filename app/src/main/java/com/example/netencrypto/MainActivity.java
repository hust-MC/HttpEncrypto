package com.example.netencrypto;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.example.netencrypto.http.HttpRequest;

import java.io.IOException;


public class MainActivity extends Activity {

    private static final String TAG = "MCLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final HttpRequest request = new HttpRequest("http://172.24.123.10");

        findViewById(R.id.send_bt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                request.request(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "Get方式请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e(TAG, "Get方式请求成功，result--->" + response.body().string());
                    }
                });

            }
        });
    }
}