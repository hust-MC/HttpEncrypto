package com.example.crypto;

import java.nio.ByteBuffer;

/**
 * 数据处理工具类
 */
public class DataUtils {

    /**
     * 字节数组转成整形数
     *
     * @param bytes 待转换的字节数组
     * @return 将字节数组拼接成整形数
     */
    public static int byte2Int(byte[] bytes) {
        // 将byte[] 封装为 ByteBuffer
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }

    /**
     * 整形数转成字节数组
     *
     * @param data 待转换的整形数
     * @return 将整形数拆解成字节数组
     */
    public static byte[] int2Byte(int data) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(data);
        return buffer.array();
    }
}
