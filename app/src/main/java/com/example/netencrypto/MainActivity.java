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
    /**
     * AES对称密钥
     */
    private byte[] mAesKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 创建Http请求对象，用于后续发起Http请求
        HttpRequest request = new HttpRequest("http://172.24.123.58");
        // 添加点击事件
        findViewById(R.id.send_bt).setOnClickListener(new RequestClick(request));
    }

    /**
     * 点击发起请求监听器
     */
    private class RequestClick implements OnClickListener {
        // Http请求
        private HttpRequest mRequest;

        /**
         * 请求点击监听器
         *
         * @param request 请求对象
         */
        RequestClick(HttpRequest request) {
            mRequest = request;
        }

        @Override
        public void onClick(View v) {
            if (mAesKey == null || mAesKey.length <= 0) {
                // 当前未获取AES密钥，发起握手协议请求密钥
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
                // 如果已经有Aes key，则直接发起业务请求
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