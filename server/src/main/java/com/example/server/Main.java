package com.example.server;

import com.example.server.http.HttpCallback;
import com.example.server.http.HttpServer;

public class Main {


    public static void main(String[] args) {
        System.out.println("Start");

        HttpServer httpServer = new HttpServer(new HttpCallback() {
            private int mCount;

            @Override
            public String onResponse(String response) {
                // 接收到response，可以做相关的解析，获取其中的业务参数及客户端信息。
                System.out.println(response);

                return "这是第" + ++mCount + "次请求";
            }
        });
        httpServer.startHttpServer();
    }

}