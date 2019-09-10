package com.example.server;

import com.example.crypto.Aes;
import com.example.crypto.DH;
import com.example.crypto.DataUtils;
import com.example.crypto.RSA;
import com.example.server.http.HttpCallback;
import com.example.server.http.HttpServer;

import java.util.Map;

/**
 * Server主程序。
 * 负责启动Http服务器，接受客户端的Http请求，解析Http的请求内容并返回响应数据。
 */
public class Main {

    private static final String RSA_KEY = "MIICXQIBAAKBgQCkqccrUNPRW7MEX9ph3Yx/5KEWWSUkI5+UnecrrimZUAm+p7KM\n" +
            "H4v01r0sjQNYhBLhPLNBS/PEWN93IJIdVBrfyV+ShXQhr0j5V8pV0h4kBFLZedSc\n" +
            "kO7+VXQKaEuxL/BUVSRpmIY8JO40jvFrjlxsuQ2qlNz+up6/pSSeWrtoVQIDAQAB\n" +
            "AoGAMHYnQABR1tP+I9NyGktyBlHrdwBVhgVV+g9e8eQYKh78PzhrvAGs1yo8EtiN\n" +
            "m4eDZvbOavFHlQny+sSDPE58eddZGi8hn0ztjUGQqyZtla7sbW9yzw0pZsTmAsgi\n" +
            "4kISw97TE5ZoDk7B26JnteRsQUQ56I7nZXjLJkkgBf93cQECQQDa2Kw8fVHXeAmA\n" +
            "pwUEV1BK8HpJ7WrMI1qlelx8dt3wtksCuDYGOoXU12VK1TtV7CL56OKRSBbxwUJC\n" +
            "A1NN+TLJAkEAwJ49Ix0OuIjq98NELi2pMTvFR+WD89PDWgMiNoHCuSkuW5FwSWhs\n" +
            "o8G6SsFLWQHkO3A2N9Kk44IUvkFD1hwjLQJAMPa/en0zfXj+70jvJ2x9q3aodbfn\n" +
            "6CqU0mdRIAvcVkoC+GXMuJIJdXiH0jlpIC1IGhAP+R9e+tIZh/mEfvKdeQJBAIWX\n" +
            "unN2HdWjMMRyrAETLju1Zti8uM4N23m8nqgLS5C8nShpMOXZBTB2lsxuT+IFy9Pl\n" +
            "udTxxeb9O4HRJkmD7mkCQQDMLVhZe7TMS5UF2aCoys5jVMe75wd8oCVvQcxugwkA\n" +
            "kv0rGxGoMSQK3aF/MgDbPZwUTHnaa9TzHWj9bwPW/531";

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
//        System.out.println("DH : " + dh.get_public_key());
//
//        String encrypted = RSA.encrypt(dh.get_public_key(), RSA.RSA_PUB_KEY);
//        System.out.println(encrypted);
//
//        String content = RSA.decrypt(encrypted, RSA.RSA_PRI_KEY);
//
//        System.out.println(content);


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