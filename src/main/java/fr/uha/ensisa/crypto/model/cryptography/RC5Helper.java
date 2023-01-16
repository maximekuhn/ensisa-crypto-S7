package fr.uha.ensisa.crypto.model.cryptography;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
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

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RC5Helper {
    private static final String RC5_ALGORITHM = "RC5/CFB/PKCS5Padding";

    private static SecureRandom RAND = new SecureRandom();

    private String data;
    private String password;
    private byte[] iv;
    private byte[] salt;

    public RC5Helper(String data, String password, byte[] iv, byte[] salt) {
        this.data = data;
        this.password = password;
        this.iv = iv;
        this.salt = salt;
        Security.addProvider(new BouncyCastleProvider());
    }

    public void setData(String data) {
        this.data = data;
    }

    public String encryptRC5() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RC5_ALGORITHM);
        if (this.iv == null)
            this.initializeIV(cipher.getBlockSize());
        RAND.nextBytes(iv);
        SecretKeySpec secretKey = generateKeyFromPassword();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(this.iv));
        byte[] encryptedData = cipher.doFinal(this.data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public String decryptRC5() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RC5_ALGORITHM);
        
        if(this.salt == null)
            throw new IllegalStateException("salt can't be null when decrypting");
        
        SecretKeySpec secretKey = generateKeyFromPassword();
        
        // Initialize cipher
        if (this.iv == null)
            throw new IllegalStateException("initialization vector can't be null here");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(this.iv));
        byte[] decodedEncryptedData = Base64.getDecoder().decode(this.data);
        byte[] decryptedData = cipher.doFinal(decodedEncryptedData);
        return new String(decryptedData);
    }

    private void initializeIV(int ivSize) {
        this.iv = new byte[ivSize];
    }

    public byte[] getIV() {
        return this.iv;
    }
    
    public byte[] getSalt() {
        return this.salt;
    }

    private SecretKeySpec generateKeyFromPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        if (this.salt == null)
            this.generateRandomSalt();
        KeySpec keySpec = new PBEKeySpec(this.password.toCharArray(), this.salt, 65536, 256);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(secretKey.getEncoded(), "RC5");
    }

    void setPassword(String password) {
        this.password = password;
    }

    public void setIV(byte[] iv) {
        this.iv = iv;
    }
    
    private void generateRandomSalt() {
        this.salt = new byte[64];
        RAND.nextBytes(this.salt);
    }
}