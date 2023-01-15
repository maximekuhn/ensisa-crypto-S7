package fr.uha.ensisa.crypto.model.cryptography;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DESHelper {
	
	private static final String DES_ALGORITHM = "";

    private String data;
    private String password;

    public DESHelper(String data, String password) {
        this.data = data;
        this.password = password;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String encryptDES() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
    	return null;
    }

    public String decryptDES() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
    	return null;
    }

	private SecretKeySpec generateKeyFromPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
    	return null;
    }

    void setPassword(String password) {
        this.password = password;
    }

}
