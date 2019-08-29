package com.example.server.http;

public interface HttpCallback {
    byte[] onResponse(String request);
}