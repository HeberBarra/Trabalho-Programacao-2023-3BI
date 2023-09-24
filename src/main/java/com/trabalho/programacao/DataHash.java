package com.trabalho.programacao;

import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DataHash {

    private static byte[] saltGenerator() {
        SecureRandom aleatorioSeguro = new SecureRandom();
        byte[] salt = new byte[16];

        aleatorioSeguro.nextBytes(salt);

        return salt;
    }

    public static String dataHasher(String textoOriginal, byte[] salt) {
        MessageDigest messageDigest;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e ) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return "";
        }

        messageDigest.update(salt);
        byte[] encondedHash = messageDigest.digest(textoOriginal.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(encondedHash.length * 2);

        for (byte hash: encondedHash) {
            String hex = Integer.toHexString(0xff & hash);
            if (hex.length() == 1) { hexString.append('0'); }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String[] dataHasher(String textoOriginal) {
        byte[] salt = saltGenerator();
        return new String[]{dataHasher(textoOriginal, salt), Arrays.toString(salt)};
    }
}
