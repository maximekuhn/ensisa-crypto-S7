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

/**
 * AESHelper is a class designed to encrypt and decrypt data, using a password.
 * The AES algorithm used is AES, CBC mode and PKCS5Padding.
 * A new AESHelper should be instanciated each time you wish to encrypt or decrypt some data.
 */
public class AESHelper {

    /**
     * specify which AES algorithm is used.
     */
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * SecureRandom instance to generate random unpredictable numbers.
     */
    private static SecureRandom RAND = new SecureRandom();

    /**
     * String to encrypt or decrypt.
     */
    private String data;

    /**
     * password used to create a secret key.
     */
    private String password;

    /**
     * Initilization vector needed for AES CBC mode. Should be unique for each encryption process.
     */
    private byte[] iv;

    /**
     * Salt used to generate a secret key from password. Should be unique for each key generation process.
     */
    private byte[] salt;

    /**
     * Constructor of class AESHelper.
     * @param data data to encrypt / decrypt
     * @param password password to be used to generate a key to encrypt / decrypt data
     * @param iv initialization vector needed for AES/CBC
     * @param salt randomly generated when generating a key from password, unique to each password
     */
    public AESHelper(String data, String password, byte[] iv, byte[] salt) {
        this.data = data;
        this.password = password;
        this.iv = iv;
        this.salt = salt;
    }

    /**
     * Specify data to encrypt / decrypt.
     * @param data
     * @see AESHelper#encryptAES()
     * @see AESHelper#decryptAES()
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * <p>
     * This method will encrypt {@link AESHelper#data} using {@link AESHelper#password}.
     * </p>
     * <p>
     * When encrypting data, this method will generate a random IV based on cipher object's block size.
     * This method will also call {@link AESHelper#generateKeyFromPassword()} to get a SecretKeySpec from {@link AESHelper#password}.
     * </p>
     * @return String encrypted {@link AESHelper#data}
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @see AESHelper#data
     * @see AESHelper#initializeIV(int)
     * @see AESHelper#generateKeyFromPassword()
     */
    public String encryptAES() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        // iv can be null when creating a calendar
        if (this.iv == null)
            this.initializeIV(cipher.getBlockSize());

        SecretKeySpec key = this.generateKeyFromPassword();

        // initialize cipher
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(this.iv));

        // encrypt data
        byte[] cipherText = cipher.doFinal(this.data.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * <p>
     * This method will decrypt {@link AESHelper#data} using {@link AESHelper#password}.
     * </p>
     * Algorithm used is "AES/CBC/PKCS5Padding" (specified by {@link AESHelper#AES_ALGORITHM})
     * @return String decrypted {@link AESHelper#data}
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IllegalStateException if {@link AESHelper#salt} or {@link AESHelper#iv} is null
     */
    public String decryptAES() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // Initialize decryption object
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

        if (this.salt == null)
            throw new IllegalStateException("salt can't be null when decrypting");

        SecretKeySpec key = this.generateKeyFromPassword();

        // Initialize cipher
        if (this.iv == null)
            throw new IllegalStateException("initialization vector can't be null here");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(this.iv));

        // decrypt data
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(this.data));
        return new String(plainText);
    }

    /**
     * Generate a random initilization vector from given ivSize. IV is generated using a SecureRandom instance.
     * @param ivSize size of the initialization vector (usually block size from AES cipher object)
     * @see AESHelper#encryptAES()
     * @see AESHelper#RAND
     * @see SecureRandom
     */
    private void initializeIV(int ivSize) {
        this.iv = new byte[ivSize];
        RAND.nextBytes(this.iv);
    }

    /**
     * Getter for {@link AESHelper#iv}.
     * @return byte[] corresponding initialization vector (IV)
     * @see AESHelper#initializeIV(int)
     */
    public byte[] getIV() {
        return this.iv;
    }

    /**
     * Getter for {@link AESHelper#salt}.
     * @return byte[] corresponding salt used to generate key from password
     * @see AESHelper#generateKeyFromPassword()
     */
    public byte[] getSalt() {
        return this.salt;
    }

    /**
     * <p>
     * Generates a SecretKeySpec object from a password using the PBKDF2WithHmacSHA256 key derivation function.
     * </p>
     * <p>
     * The method creates a SecretKeyFactory object, which is used to generate a KeySpec from the password, randomly generated salt and a number of iterations.
     * </p>
     * <p>
     * This SecretKeySpec object can be used to encrypt and decrypt data using the Advanced Encryption Standard (AES) algorithm.
     * </p>
     * @return SecretKeySpec a 256 bits length key used to encrypt or decrypt some data using AES algorithm
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @see AESHelper#password
     * @see AESHelper#getSalt()
     * @see AESHelper#encryptAES()
     * @see AESHelper#decryptAES()
     */
    private SecretKeySpec generateKeyFromPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        if (this.salt == null)
            this.generateRandomSalt();
        KeySpec keySpec = new PBEKeySpec(this.password.toCharArray(), this.salt, 65536, 256);
        SecretKey secretKey = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    /**
     * Allows user to set initialization vector for decryption process.
     * @param iv : byte[] initialization vector needed to decrypt data
     * @see AESHelper#iv
     * @see AESHelper#decryptAES()
     */
    public void setIV(byte[] iv) {
        this.iv = iv;
    }


    /**
     * <p>
     * Generate a random byte[] used as salt for generating a key from password.
     * </p>
     * The length of this byte[] is fixed to 64 and is generated using a SecureRandom instance.
     * @see AESHelper#RAND
     * @see SecureRandom
     */
    private void generateRandomSalt() {
        this.salt = new byte[64];
        RAND.nextBytes(this.salt);
    }

    /**
     * Setter of {@link AESHelper#password}.
     * This method is mainly used for testing purposes as it is in package visibility.
     * @param password
     */
    void setPassword(String password) {
        this.password = password;
    }
}
