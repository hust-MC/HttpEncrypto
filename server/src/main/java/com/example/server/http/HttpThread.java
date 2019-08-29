package com.example.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

            String content;
            StringBuilder request = new StringBuilder();


            while ((content = reader.readLine()) != null && !content.trim().isEmpty()) {
                request.append(content).append("\n\r");
            }

            System.out.println("request:\n" + request + "\n");

            byte[] response = new byte[0];
            if (mHttpCallback != null) {
                response = mHttpCallback.onResponse(request.toString());
            }

            //将响应头发送给客户端
            String responseFirstLine = "HTTP/1.1 200 OK\r\n";

            String responseHead = "Content-Type:" + "text/html" + "\r\n";

            OutputStream outSocket = mSocket.getOutputStream();
            System.out.println("ServerResponse:\n" + responseFirstLine + "\n" + responseHead + "\n");
            outSocket.write(responseFirstLine.getBytes());
            outSocket.write(responseHead.getBytes());
            outSocket.write("\r\n".getBytes());
            outSocket.write(response);
            mSocket.close();
            outSocket.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}