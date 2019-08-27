package com.example.server;

import com.example.server.crypto.Aes;
import com.example.server.http.HttpCallback;
import com.example.server.http.HttpServer;

/**
 * Server主程序。
 * 负责启动Http服务器，接受客户端的Http请求，解析Http的请求内容并返回响应数据。
 */
public class Main {

    /**
     * 入口程序
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Start");


        String str = "哈哈哈";
        Aes aes = new Aes();
        byte[] encrypted = aes.encrypt(str);

        System.out.println("the encrypted code is : " + new String(encrypted));

        System.out.println("the origin code is : " + new String(aes.decrypt(encrypted)));

        HttpServer httpServer = new HttpServer(new HttpCallback() {
            /**
             * 记录连接次数
             */
            private int mCount;

            @Override
            public String onResponse(String response) {
                // 接收到response，可以做相关的解析，获取其中的业务参数及客户端信息。
                System.out.println(response);

                return "这是第" + ++mCount + "次请求";
            }
        });
        // 启动Http服务
        httpServer.startHttpServer();
    }

}