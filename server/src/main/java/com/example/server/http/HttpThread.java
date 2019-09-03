package com.example.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 独立子线程，专门用于处理http请求及响应
 */
public class HttpThread implements Runnable {

    private Socket mSocket;
    /** Http请求回调，用于将收到的客户端消息传递给使用方，并接收来自使用方的响应，通过Http返回给客户端 */
    private HttpCallback mHttpCallback;

    HttpThread(Socket socket, HttpCallback callback) {
        mSocket = socket;
        mHttpCallback = callback;
    }

    @Override
    public void run() {
        try {
            // 通过BufferedReader包装；
            // 1、提升I/O效率；2、便于逐行读入
            BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

            String content;
            StringBuilder request = new StringBuilder();

            // 完成客户端请求内容的逐行读入
            while ((content = reader.readLine()) != null && !content.trim().isEmpty()) {
                request.append(content).append("\n");
            }

            System.out.println("request:\n" + request + "\n");

            byte[] response = new byte[0];
            if (mHttpCallback != null) {
                // 将客户端的请求回调出去，并获取处理后的结果，最终作为response的具体业务数据
                response = mHttpCallback.onResponse(request.toString());
            }

            // 将响应头发送给客户端
            String responseFirstLine = "HTTP/1.1 200 OK\r\n";
            // 添加Http类型字段
            String responseHead = "Content-Type:" + "text/html" + "\r\n";

            OutputStream outSocket = mSocket.getOutputStream();
            System.out.println("ServerResponse:\n" + responseFirstLine + "\n" + responseHead + "\n");
            outSocket.write(responseFirstLine.getBytes());
            outSocket.write(responseHead.getBytes());
            outSocket.write("\r\n".getBytes());
            outSocket.write(response);
            mSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}