package com.example.netencrypto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.netencrypto.http.HttpRequest;
import com.example.server.crypto.Aes;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String str = "这是一段明文";
        Aes aes = new Aes();

        byte[] encrypted = aes.encrypt(str);
        Log.e("MCLOG", new String(encrypted));
        Log.e("MCLOG", new String(aes.decrypt(encrypted)));

        final HttpRequest request = new HttpRequest("http://172.24.123.30");

        findViewById(R.id.send_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.request();
            }
        });
    }


}
