package com.example.server.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpThread implements Runnable {

    private Socket mSocket;
    private HttpCallback mHttpCallback;

    HttpThread(Socket socket, HttpCallback callback) {
        mSocket = socket;
        mHttpCallback = callback;
    }

    @Override
    public void run() {
        try {

            InputStream inputStream = mSocket.getInputStream();

            // 获取HTTP请求头
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            String request = new String(buffer);
            System.out.println("request:\n" + request + "\n");

            String response = "";
            if (mHttpCallback != null) {
                response = mHttpCallback.onResponse(request);
            }

            //将响应头发送给客户端
            String responseFirstLine = "HTTP/1.1 200 OK\r\n";

            String responseHead = "Content-Type:" + "text/html" + "\r\n";

            OutputStream outSocket = mSocket.getOutputStream();
            System.out.println("ServerResponse:\n" + responseFirstLine + "\n" + responseHead + "\n");
            outSocket.write(responseFirstLine.getBytes());
            outSocket.write(responseHead.getBytes());
            outSocket.write("\r\n".getBytes());
            outSocket.write(response.getBytes());
            mSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}