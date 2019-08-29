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
import com.example.server.crypto.Aes;

import java.io.IOException;

public class MainActivity extends Activity {

    private static final String TAG = "MCLOG";
    private byte[] mAesKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final HttpRequest request = new HttpRequest("http://172.24.123.58");

        findViewById(R.id.send_bt).setOnClickListener(new RequestClick(request));
    }

    private class RequestClick implements OnClickListener {

        private HttpRequest mRequest;

        RequestClick(HttpRequest request) {
            mRequest = request;
        }

        @Override
        public void onClick(View v) {
            if (mAesKey == null || mAesKey.length <= 0) {
                mRequest.handshake(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "Get方式获取密钥失败", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        mAesKey = response.body().bytes();
                        Log.e(TAG, "Get方式获取密钥成功，result--->" + new String(mAesKey));
                    }
                });
            } else {
                mRequest.request(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "Get方式请求失败", e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        byte[] responseContent = response.body().bytes();
                        Aes aes = new Aes(mAesKey);
                        String content = new String(aes.decrypt(responseContent));
                        Log.e(TAG, "Get方式请求成功，result--->" + content);
                    }
                });
            }
        }
    }
}