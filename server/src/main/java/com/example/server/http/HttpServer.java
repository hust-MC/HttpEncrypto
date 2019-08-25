package com.example.server.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private boolean mRunning;
    private HttpCallback mHttpCallback;

    public HttpServer(HttpCallback HttpCallback) {
        mHttpCallback = HttpCallback;
    }

    public void startHttpServer() {
        if (mRunning) {
            return;
        }

        mRunning = true;

        try {
            ServerSocket serverSocket = new ServerSocket(80);

            while (mRunning) {
                final Socket socket = serverSocket.accept();
                System.out.println("Accept");

                ExecutorService threadPool = Executors.newCachedThreadPool();
                threadPool.execute(new HttpThread(socket, mHttpCallback));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception");

        }
    }

    public void stopHttpServer() {
        mRunning = false;
    }
}
