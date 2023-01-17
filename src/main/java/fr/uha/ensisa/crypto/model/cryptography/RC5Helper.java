package fr.uha.ensisa.crypto.model.cryptography;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
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

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RC5Helper {
    private static final String RC5_ALGORITHM = "RC5/CFB/PKCS5Padding";

    private static SecureRandom RAND = new SecureRandom();

    private String data;
    private String password;
    private byte[] iv;
    private byte[] salt;

    /**
     * 
     * @param data
     * @param password
     * @param iv
     * @param salt
     */
    public RC5Helper(String data, String password, byte[] iv, byte[] salt) {
        this.data = data;
        this.password = password;
        this.iv = iv;
        this.salt = salt;
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
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

    /**
     * 
     * @return
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String decryptRC5() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RC5_ALGORITHM);

        if (this.salt == null)
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

    /**
     * 
     * @return IV : byte[] representing initialization vector used for RC5 ecryption / decryption.
     * @see AESHelper#iv
     * @see AESHelper#decryptAES()
     */
    public byte[] getIV() {
        return this.iv;
    }

    /**
     * 
     * @return
     */
    public byte[] getSalt() {
        return this.salt;
    }

    /**
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private SecretKeySpec generateKeyFromPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        if (this.salt == null)
            this.generateRandomSalt();
        KeySpec keySpec = new PBEKeySpec(this.password.toCharArray(), this.salt, 65536, 256);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(secretKey.getEncoded(), "RC5");
    }

    /**
     * 
     * @param password
     */
    void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * @param iv
     */
    public void setIV(byte[] iv) {
        this.iv = iv;
    }

    /**
     * this function generate a random salt
     */
    private void generateRandomSalt() {
        this.salt = new byte[64];
        RAND.nextBytes(this.salt);
    }
}
