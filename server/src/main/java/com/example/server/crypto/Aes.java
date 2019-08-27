package com.example.server.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Aes {

    private SecretKey mKey;

    public Aes() {
        // 获取秘钥生成器
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("Aes");

            // 通过种子初始化
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(System.currentTimeMillis());
            keyGenerator.init(128, secureRandom);
            // 生成秘钥并返回
            mKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String content) {
        if (mKey == null) {
            return new byte[]{0};
        }
        // 秘钥
        byte[] enCodeFormat = mKey.getEncoded();
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "Aes");
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance("Aes");
            // 初始化加密器
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            return cipher.doFinal(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return new byte[]{0};
    }

    public byte[] decrypt(byte[] content) {

        if (mKey == null) {
            return new byte[]{0};
        }
        // 秘钥
        byte[] enCodeFormat = mKey.getEncoded();
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 解密
            return cipher.doFinal(content);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return new byte[]{0};
    }
}
