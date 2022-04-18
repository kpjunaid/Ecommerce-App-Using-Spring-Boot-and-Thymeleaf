package com.shoparoo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TokenGeneratorUtil {
    public static String generateToken() throws NoSuchAlgorithmException {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            String randomNumber = Integer.toString(random.nextInt());
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digestBytes = messageDigest.digest(randomNumber.getBytes());

            return bytesToHex(digestBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException("Unable to generate token");
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (byte b: bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }
}
