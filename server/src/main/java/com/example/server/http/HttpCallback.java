package com.example.server.http;

/**
 * Http请求回调，将Http的请求消息传出，并接收业务数据返回
 */
public interface HttpCallback {
    /**
     * 收到消息的回调通知
     *
     * @param request 客户端请求内容
     * @return 返回给客户端的response消息
     */
    byte[] onResponse(String request);
}