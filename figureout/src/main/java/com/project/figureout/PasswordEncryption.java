package com.project.figureout;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Getter
@Setter
@Component
public class PasswordEncryption {

    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding"; // AES with mode and padding
    private static final byte[] ENCRYPTION_KEY = "1234567890123456".getBytes(); // Must be exactly 16 bytes

    public String encryptPassword(String plainPassword) throws Exception {
        SecretKey key = new SecretKeySpec(ENCRYPTION_KEY, "AES"); // Algorithm for the key
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainPassword.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Encode to Base64 for safe storage
    }

    public String decryptPassword(String encryptedPassword) throws Exception {
        SecretKey key = new SecretKeySpec(ENCRYPTION_KEY, "AES");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword); // Decode from Base64
        return new String(cipher.doFinal(decodedBytes));
    }
}
