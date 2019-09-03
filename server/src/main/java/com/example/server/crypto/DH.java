package com.example.server.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Dh算法
 */
public class DH {

    /**
     * 原根对，P和G
     */
    private static final int dhP = 23;
    private static final int dhG = 5;

    /**
     * DH私钥
     */
    private int mPrivateKey;

    /**
     * 构造器，默认生成一个10以下的整形数作为DH私钥
     */
    public DH() {
        Random r = new Random();
        mPrivateKey = r.nextInt(10);
        System.out.println("private key is : " + mPrivateKey);

    }

    /**
     * 根据DH算法生成公钥，传递给服务端，用于做密钥交换
     *
     * @return DH公钥
     */
    public int get_public_key() {
        return (int) Math.pow(dhG, mPrivateKey) % dhP;
    }

    /**
     * 根据对方的DH公钥生成最终密钥
     *
     * @param publicKey 对方的公钥，long形式
     * @return 最终密钥
     */
    public byte[] get_secret_key(long publicKey) {
        int secretKey = (int) Math.pow(publicKey, mPrivateKey) % dhP;
        return sha256(secretKey);
    }

    /**
     * 根据对方的DH公钥生成最终密钥
     *
     * @param publicKey 对方的公钥，字节数组形式
     * @return 最终密钥
     */
    public byte[] get_secret_key(byte[] publicKey) {
        int key = DataUtils.byte2Int(publicKey);
        int secretKey = (int) Math.pow(key, mPrivateKey) % dhP;
        return sha256(secretKey);
    }

    /**
     * sha256算法，用于对DH密钥做Hash转换，生成32字节的数据，作为AES的密钥
     *
     * @param data DH密钥
     * @return Sha256形式的数据，作为AES的密钥
     */
    private byte[] sha256(int data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(DataUtils.int2Byte(data));
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
