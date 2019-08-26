package com.example.server.crypto;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private SecretKey generateKey(String seed) throws Exception {
        // 获取秘钥生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 通过种子初始化
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed.getBytes("UTF-8"));
        keyGenerator.init(128, secureRandom);
        // 生成秘钥并返回
        return keyGenerator.generateKey();
    }

    private byte[] encrypt(String content, SecretKey secretKey) throws Exception {
        // 秘钥
        byte[] enCodeFormat = secretKey.getEncoded();
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 加密
        return cipher.doFinal(content.getBytes("UTF-8"));
    }
}
