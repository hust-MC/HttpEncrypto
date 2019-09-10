package com.example.crypto;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

    public static final String RSA_PRI_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKSpxytQ09FbswRf" +
            "2mHdjH/koRZZJSQjn5Sd5yuuKZlQCb6nsowfi/TWvSyNA1iEEuE8s0FL88RY33cg" +
            "kh1UGt/JX5KFdCGvSPlXylXSHiQEUtl51JyQ7v5VdApoS7Ev8FRVJGmYhjwk7jSO" +
            "8WuOXGy5DaqU3P66nr+lJJ5au2hVAgMBAAECgYAwdidAAFHW0/4j03IaS3IGUet3" +
            "AFWGBVX6D17x5BgqHvw/OGu8AazXKjwS2I2bh4Nm9s5q8UeVCfL6xIM8Tnx511ka" +
            "LyGfTO2NQZCrJm2Vruxtb3LPDSlmxOYCyCLiQhLD3tMTlmgOTsHbome15GxBRDno" +
            "judleMsmSSAF/3dxAQJBANrYrDx9Udd4CYCnBQRXUErwekntaswjWqV6XHx23fC2" +
            "SwK4NgY6hdTXZUrVO1XsIvno4pFIFvHBQkIDU035MskCQQDAnj0jHQ64iOr3w0Qu" +
            "LakxO8VH5YPz08NaAyI2gcK5KS5bkXBJaGyjwbpKwUtZAeQ7cDY30qTjghS+QUPW" +
            "HCMtAkAw9r96fTN9eP7vSO8nbH2rdqh1t+foKpTSZ1EgC9xWSgL4Zcy4kgl1eIfS" +
            "OWkgLUgaEA/5H1760hmH+YR+8p15AkEAhZe6c3Yd1aMwxHKsARMuO7Vm2Ly4zg3b" +
            "ebyeqAtLkLydKGkw5dkFMHaWzG5P4gXL0+W51PHF5v07gdEmSYPuaQJBAMwtWFl7" +
            "tMxLlQXZoKjKzmNUx7vnB3ygJW9BzG6DCQCS/SsbEagxJArdoX8yANs9nBRMedpr" +
            "1PMdaP1vA9b/nfU=";

    public static final String RSA_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkqccrUNPRW7MEX9ph3Yx/5KEW" +
            "WSUkI5+UnecrrimZUAm+p7KMH4v01r0sjQNYhBLhPLNBS/PEWN93IJIdVBrfyV+S" +
            "hXQhr0j5V8pV0h4kBFLZedSckO7+VXQKaEuxL/BUVSRpmIY8JO40jvFrjlxsuQ2q" +
            "lNz+up6/pSSeWrtoVQIDAQAB";


    private static final String RSA_ALGORITHM = "RSA";

    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static String encrypt(int data, String publicKey) {
        String message = String.valueOf(data);
        // base64编码的公钥
        byte[] decoded = base64Decode(publicKey);
        byte[] result = new byte[0];
        try {
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            result = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return  base64Encode(result);
    }

    public static String decrypt(String encrypted, String privateKey) {
        // base64解码的私钥
        byte[] decoded = base64Decode(privateKey);
        byte[] content = base64Decode(encrypted);
        byte[] result = new byte[0];
        try {
            RSAPrivateKey priKey  = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            result = cipher.doFinal(content);

        } catch (InvalidKeySpecException | NoSuchPaddingException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return new String(result);
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
