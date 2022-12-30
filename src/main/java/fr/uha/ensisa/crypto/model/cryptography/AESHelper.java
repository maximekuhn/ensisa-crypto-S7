package fr.uha.ensisa.crypto.model.cryptography;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESHelper {

    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static SecureRandom RAND = new SecureRandom();

    private String data;
    private String password;
    private byte[] iv;

    public AESHelper(String data, String password, byte[] iv) {
        this.data = data;
        this.password = password;
        this.iv = iv;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String encryptAES() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        // iv can be null when creating a calendar
        if (this.iv == null)
            this.initializeIV(cipher.getBlockSize());
        RAND.nextBytes(iv);

        SecretKeySpec key = this.generateKeyFromPassword();

        // initialize cipher
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(this.iv));

        // encrypt data
        byte[] cipherText = cipher.doFinal(this.data.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decryptAES() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // Initialize decryption object
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        SecretKeySpec key = this.generateKeyFromPassword();

        // Initialize cipher
        if(this.iv == null)
                throw new IllegalStateException("initialization vector can't be null here");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(this.iv));

        // decrypt data
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(this.data));
        return new String(plainText);
    }

    private void initializeIV(int ivSize) {
        this.iv = new byte[ivSize];
    }

    private SecretKeySpec generateKeyFromPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec keySpec = new PBEKeySpec(this.password.toCharArray(), "ceci est du sel".getBytes(), 65536, 256);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }
}
