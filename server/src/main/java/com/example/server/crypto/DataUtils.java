package com.example.server.crypto;

import java.nio.ByteBuffer;

public class DataUtils {

    public static int byte2Int(byte[] bytes) {
        // 将byte[] 封装为 ByteBuffer
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }

    public static byte[] int2Byte(int data) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(data);
        return buffer.array();
    }
}
