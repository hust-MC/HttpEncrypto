package com.example.server.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DH {

    private static final int dhP = 23;
    private static final int dhG = 5;

    private int mPrivateKey;

    public DH() {
        Random r = new Random();
        mPrivateKey = r.nextInt(10);
        System.out.println("private key is : " + mPrivateKey);

    }

    public int get_public_key() {
        return (int) Math.pow(dhG, mPrivateKey) % dhP;
    }

    public byte[] get_secret_key(long publicKey) {
        int secretKey = (int) Math.pow(publicKey, mPrivateKey) % dhP;
        return sha256(secretKey);
    }

    public byte[] get_secret_key(byte[] publicKey) {

        int key = DataUtils.byte2Int(publicKey);

        int secretKey = (int) Math.pow(key, mPrivateKey) % dhP;
        return sha256(secretKey);
    }

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
