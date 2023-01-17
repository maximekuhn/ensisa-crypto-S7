/**
 * @author Goker Batuhan / Grainca Albi
 */
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

/**
 * RC5Helper is a class designed to encrypt and decrypt data, using a password.
 * In this class, we used the RC5 algorithm with CFB mode and PKCS5Padding.
 */
public class RC5Helper {
    private static final String RC5_ALGORITHM = "RC5/CFB/PKCS5Padding";

    private static SecureRandom RAND = new SecureRandom();

    private String data;
    private String password;
    private byte[] iv;
    private byte[] salt;

    /**
     * This is the constructor of class RC5Helper
     * @param data : data to encrypt or decrypt.
     * @param password : to be used to generate a key to encrypt / decrypt data.
     * @param iv : initialization vector needed for RC5/CFB.
     * @param salt : randomly generated when generating a key from password, unique to each password
     */
    public RC5Helper(String data, String password, byte[] iv, byte[] salt) {
        this.data = data;
        this.password = password;
        this.iv = iv;
        this.salt = salt;
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * This method allows to set the data to encrypt / decrypt.
     * @param data : data to encrypt or decrypt.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * This method allows to encrypt the {@link RC5Helper#data} using {@link RC5Helper#password}.
     * 
     * @return String encrypted {@link AESHelper#data}
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @see RC5Helper#data
     * @see RC5Helper#iv
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
     * This method allows to decrypt the {@link RC5Helper#data} using {@link RC5Helper#password}.
     * 
     * @return String decrypted {@link AESHelper#data}
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @see RC5Helper#data
     * @see RC5Helper#iv
     * @see RC5Helper#salt
     */
    public String decryptRC5() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RC5_ALGORITHM);

        if (this.salt == null)
            throw new IllegalStateException("salt can't be null when decrypting");

        SecretKeySpec secretKey = this.generateKeyFromPassword();
        
        // Initialize cipher
        if (this.iv == null)
            throw new IllegalStateException("initialization vector can't be null here");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(this.iv));
        byte[] decodedEncryptedData = Base64.getDecoder().decode(this.data);
        byte[] decryptedData = cipher.doFinal(decodedEncryptedData);
        return new String(decryptedData);
    }
    
    /**
     * This method initialize {@link RC5Helper#iv}.
     * 
     * @param ivSize
     * @see RC5Helper#iv
     */
    private void initializeIV(int ivSize) {
        this.iv = new byte[ivSize];
    }

    /**
     * This method allows to get {@link RC5Helper#iv}.
     * 
     * @return IV : byte[] representing initialization vector used for RC5 ecryption / decryption.
     * @see RC5Helper#iv
     * @see RC5Helper#decryptRC5()
     */
    public byte[] getIV() {
        return this.iv;
    }

    /**
     * This method allows to get {@link RC5Helper#salt}.
     * 
     * @return salt : byte[] used to generate key from password 
     * @see RC5Helper#salt
     * @see RC5Helper#decryptRC5()
     */
    public byte[] getSalt() {
        return this.salt;
    }

    /**
     * This method allows to generate a key from {@link RC5Helper#password}.
     * 
     * @return key : SecretKeySpec representing encoded {@link RC5Helper#password}
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @see RC5Helper#password
     * @see RC5Helper#salt
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
     * This method allows to set the {@link RC5Helper#password}.
     * 
     * @param password
     * @see RC5Helper#password
     */
    void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method allows to set {@link RC5Helper#iv}.
     * 
     * @param iv
     * @see RC5Helper#iv
     */
    public void setIV(byte[] iv) {
        this.iv = iv;
    }

    /**
     * <p>This method generate a random {@link RC5Helper#salt}.</p>
     * 
     * <p>The length of this byte[] is fixed to 64 and is generated using a SecureRandom instance.</p>
     * 
     * @see RC5Helper#salt
     * @see RC5Helper#RAND
     */
    private void generateRandomSalt() {
        this.salt = new byte[64];
        RAND.nextBytes(this.salt);
    }
}
