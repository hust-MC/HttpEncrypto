package com.example.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Http服务器
 */
public class HttpServer {

    /** 标记运行状态 */
    private boolean mRunning;
    /** Http回调接口 */
    private HttpCallback mHttpCallback;

    public HttpServer(HttpCallback HttpCallback) {
        mHttpCallback = HttpCallback;
    }

    /**
     * 启动Http服务
     */
    public void startHttpServer() {
        if (mRunning) {
            return;
        }

        mRunning = true;

        try {
            // 指定端口为Http端口，即80
            ServerSocket serverSocket = new ServerSocket(80);

            while (mRunning) {
                // 本地建立Http服务，等待连接
                Socket socket = serverSocket.accept();
                System.out.println("Accept");

                // 当收到Http连接之后，从线程池中获取一个独立线程执行Http连接
                ExecutorService threadPool = Executors.newCachedThreadPool();
                threadPool.execute(new HttpThread(socket, mHttpCallback));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception");
        }
    }

    /**
     * 关闭Http服务
     */
    public void stopHttpServer() {
        mRunning = false;
    }

    public static Map<String, String> getHeader(String request) {
        Map<String, String> header = new HashMap<>();

        try {
            // 逐行解析request，得到Header信息
            BufferedReader reader = new BufferedReader(new StringReader(request));
            String line = reader.readLine();

            while (line != null && !line.trim().isEmpty()) {

                int p = line.indexOf(':');
                if (p >= 0) {
                    // http header均转为小写，方便业务方处理
                    header.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());

                }
                line = reader.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return header;
    }
}
