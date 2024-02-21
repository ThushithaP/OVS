package com.ovs.VotingSystem.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtils {
    public static final String SECRET_KEY = "OVS2024SPRINGBOO";

    public static String encrypt(Integer id) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(),"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(String.valueOf(id).getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static Integer decrypt(String encId) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(),"AES");
        Cipher cipher =Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encId));
        return Integer.parseInt(new String(decryptedBytes ));
    }
}
