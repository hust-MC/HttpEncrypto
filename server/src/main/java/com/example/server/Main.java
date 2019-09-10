package com.example.server;

import com.example.crypto.Aes;
import com.example.crypto.DH;
import com.example.crypto.DataUtils;
import com.example.crypto.RSA;
import com.example.server.http.HttpCallback;
import com.example.server.http.HttpServer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Server主程序。
 * 负责启动Http服务器，接受客户端的Http请求，解析Http的请求内容并返回响应数据。
 */
public class Main {

    public static final String RSA_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKSpxytQ09FbswRf" +
            "2mHdjH/koRZZJSQjn5Sd5yuuKZlQCb6nsowfi/TWvSyNA1iEEuE8s0FL88RY33cg" +
            "kh1UGt/JX5KFdCGvSPlXylXSHiQEUtl51JyQ7v5VdApoS7Ev8FRVJGmYhjwk7jSO" +
            "8WuOXGy5DaqU3P66nr+lJJ5au2hVAgMBAAECgYAwdidAAFHW0/4j03IaS3IGUet3" +
            "AFWGBVX6D17x5BgqHvw/OGu8AazXKjwS2I2bh4Nm9s5q8UeVCfL6xIM8Tnx511ka" +
            "LyGfTO2NQZCrJm2Vruxtb3LPDSlmxOYCyCLiQhLD3tMTlmgOTsHbome15GxBRDno" +
            "judleMsmSSAF/3dxAQJBANrYrDx9Udd4CYCnBQRXUErwekntaswjWqV6XHx23fC2" +
            "SwK4NgY6hdTXZUrVO1XsIvno4pFIFvHBQkIDU035MskCQQDAnj0jHQ64iOr3w0Qu" +
            "LakxO8VH5YPz08NaAyI2gcK5KS5bkXBJaGyjwbpKwUtZAeQ7cDY30qTjghS+QUPW" +
            "HCMtAkAw9r96fTN9eP7vSO8nbH2rdqh1t+foKpTSZ1EgC9xWSgL4Zcy4kgl1eIfS" +
            "OWkgLUgaEA/5H1760hmH+YR+8p15AkEAhZe6c3Yd1aMwxHKsARMuO7Vm2Ly4zg3b" +
            "ebyeqAtLkLydKGkw5dkFMHaWzG5P4gXL0+W51PHF5v07gdEmSYPuaQJBAMwtWFl7" +
            "tMxLlQXZoKjKzmNUx7vnB3ygJW9BzG6DCQCS/SsbEagxJArdoX8yANs9nBRMedpr" +
            "1PMdaP1vA9b/nfU=";

    private static final String CONTENT = "这是第%d次请求";

    /**
     * 入口程序
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Start");

        final Aes aes = new Aes();
        final DH dh = new DH();

        HttpServer httpServer = new HttpServer(new HttpCallback() {
            /**
             * 记录连接次数
             */
            private int mCount;

            @Override
            public byte[] onResponse(String request) {
                // 接收到response，可以做相关的解析，获取其中的业务参数及客户端信息。
                System.out.println(request);

                if (isHandshake(request)) {
                    // 如果当前是握手请求，则返回DH公钥
                    Map<String, String> header = HttpServer.getHeader(request);
                    String handshake = header.get(Aes.HANDSHAKE);
                    System.out.println("handshake is : " + handshake);
                    int dhPubKey = Integer.valueOf(RSA.decrypt(handshake, RSA_KEY));
                    aes.setKey(dh.get_secret_key(dhPubKey));
                    return DataUtils.int2Byte(dh.get_public_key());
                }
                // 如果不是握手请求，则返回加密后的业务数据
                return aes.encrypt(String.format(CONTENT, ++mCount));
            }
        });
        // 启动Http服务
        httpServer.startHttpServer();
    }

    /**
     * 通过request中header是否有Handshake字段判断握手请求
     *
     * @param request request内容
     * @return 是否是握手请求
     */
    private static boolean isHandshake(String request) {
        return (request != null && request.contains(Aes.HANDSHAKE));
    }

}