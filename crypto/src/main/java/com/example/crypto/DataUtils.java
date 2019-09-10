package com.example.crypto;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.nio.ByteBuffer;
import java.util.Base64;

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
    static int byte2Int(byte[] bytes) {
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

    @TargetApi(Build.VERSION_CODES.O)
    public static String base64Encode(byte[] data) {
        try {
            return android.util.Base64.encodeToString(data, android.util.Base64.NO_WRAP);
        } catch (Exception e) {
            return Base64.getEncoder().encodeToString(data);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] base64Decode(String data) {
        try {
            return android.util.Base64.decode(data, android.util.Base64.NO_WRAP);
        } catch (Exception e) {
            return Base64.getMimeDecoder().decode(data);
        }
    }
}
