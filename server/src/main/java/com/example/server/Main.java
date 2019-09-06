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

    private static final String RSA_KEY = "MIICXAIBAAKBgQC0QWE2lO466f12i3LsXlFQRNG3KVaOH+uS9i4dK+rlRKYVh7nq\n" +
            "/QWHtWKqXQPnObrXv+G4IGnW1/PW02p1HyXu3kOvJganS6enQ9fsKcz4ldhCt83y\n" +
            "NOWgLOUh5ZK7/b5ZmMzhTYHV/yqvhns1/ysRkLomKeMj+1Uv6FqxN+D94wIDAQAB\n" +
            "AoGACaKA/rjhLvb43FBB2U6FTCQRjZfBD6G3JsjP2DFTweN0eSwjDONvWzyfxHlq\n" +
            "Tv6v9P5Fo2DgJ2Ktnur4AsCid1YiOQR7ILc60/rk/cUV7l9jW4//pYWTZe/nfwc1\n" +
            "1v5l2EjcGGPVMfViyVky+5pkXZ7c4CbDi6rSSeyOU/PBHOkCQQDdt7O590/vpDKX\n" +
            "BhxsutERnJ1eu3XEMCiSRojEh7t1edwd18RkQ2EqV2FAvQlVb+EPRllRcp78MKaZ\n" +
            "zR+OktUHAkEA0CB4Vn2a3AKi9INrwlrDOiY8q8ijFV1Lg7a4NalHZxnVovpIi5Xg\n" +
            "JMs2mHIKEZSaKbhGejAta3NolAisCYwVRQJAQgtwrBqxYw8kSrx1RyY3FOn0Lr3k\n" +
            "jLZTv47nOrO78XiFSxZ01/ECRpyUybYFy79x6Rzpikt8dF8BoZrqpYuzjQJBALx0\n" +
            "2mJ+483aeVHs+pOHegXaf8+RItMCopFGz7CPcr9R7lfgR/ZVJRxMAkix4poetCWY\n" +
            "3gBgRZBpdUWuQJF4jf0CQBxdoexfRtPdtDn8mnQoodOS3CHMj1MGJ87QdY5VdkZd\n" +
            "X6BBxV7H9Ox06m5UEfH7TOhkNIeF+sVroQ2umGvg1as=";

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