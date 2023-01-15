package fr.uha.ensisa.crypto.model.cryptography;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RC5Helper {
    private static final String RC5_ALGORITHM = "RC5/CFB/PKCS5Padding";

    private static SecureRandom RAND = new SecureRandom();

    private String data;
    private String password;
    private byte[] iv;

    public RC5Helper(String data, String password, byte[] iv) {
        this.data = data;
        this.password = password;
        this.iv = iv;
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
        return new String(Base64.getEncoder().encode(encryptedData));
    }

    public String decryptRC5() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RC5_ALGORITHM);
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

    private SecretKeySpec generateKeyFromPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            byte[] key = this.password.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "RC5");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    void setPassword(String password) {
        this.password = password;
    }

    public void setIV(byte[] iv) {
        this.iv = iv;
    }
}
