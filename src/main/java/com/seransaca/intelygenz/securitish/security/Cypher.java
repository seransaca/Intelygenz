package com.seransaca.intelygenz.securitish.security;

import com.seransaca.intelygenz.securitish.service.exceptions.CypherException;
import com.seransaca.intelygenz.securitish.service.request.Constants;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Cypher {
    private static Cipher cipher;
    private static byte[] encoded = "EncoderString8$1".getBytes();
    private static SecretKey secretKey = new SecretKeySpec(encoded, "AES");

    public static final Integer TYPE_ITEM = 1;

    public static final Integer TYPE_PASSWORD = 2;


    public static String encrypt(String plainText, Integer type){
        byte[] plainTextByte = plainText.getBytes();
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            String encryptedText = encoder.encodeToString(encryptedByte);
            return encryptedText;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new CypherException(plainText, type.equals(TYPE_ITEM) ? Constants.ERROR_ITEM_ENCRYPT : Constants.ERROR_PASSWORD_ENCRYPT);
        }
    }

    public static String decrypt(String encryptedText, Integer type) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
            String decryptedText = new String(decryptedByte);
            return decryptedText;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new CypherException(encryptedText, type.equals(TYPE_ITEM) ? Constants.ERROR_ITEM_DECRYPT : Constants.ERROR_PASSWORD_DECRYPT);
        }


    }
}
