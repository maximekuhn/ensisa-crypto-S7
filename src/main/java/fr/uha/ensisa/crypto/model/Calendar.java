package fr.uha.ensisa.crypto.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Calendar {
	private EventTable eventTable;
	private String name;

	/* cryptography */
	private String algorithm;
	private String password;

	/* AES */
	private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static SecureRandom RAND = new SecureRandom();
	private byte[] iv;
	
	public Calendar(String name) {
		eventTable = new EventTable();
		this.name = name;
		this.algorithm = "";
		this.password = "";
	}
	
	public Calendar(String name, String algorithm, String password) {
		eventTable = new EventTable();
		this.name = name;
		this.algorithm = algorithm;
		this.password = password;
	}
	
	public EventTable getEventTable() {
		return eventTable;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void saveCalendar() throws IOException {
	    File dir = new File("data");
	    if (!dir.isDirectory()) {
	        if (!dir.mkdir()) {
	            throw new IOException("Can't create the data directory");
	        }
	    }

	    ObjectMapper mapper = new ObjectMapper();
	    String encrypted = encrypt(mapper.writeValueAsString(eventTable.getAllEvents()));
	    BufferedWriter writer = new BufferedWriter(new FileWriter("data/"+name));
	    writer.write(algorithm+";"+encrypted);
	    writer.close();
	}
	
	String encrypt(String jsonCalendar) {
		switch (algorithm) {
			case "AES":
				return encryptAES(jsonCalendar);
			default:
				return jsonCalendar;
		}
	}
	
	protected String decrypt(String encrypted) {
		switch (algorithm) {
			case "AES":
				return decryptAES(encrypted);
			default:
				return encrypted;
		}
	}

	private String decryptAES(String encrypted) {
		try {
			// Initialize decryption object
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

			// Generate key from password
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec keySpec = new PBEKeySpec(this.password.toCharArray(), "ceci est du sel".getBytes(), 65536, 256);
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

			// TODO check if iv is null and load it

			// Initialize cipher
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(this.iv));

			// decode
			byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encrypted));
			return new String(plainText);
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private String encryptAES(String jsonCalendar) {
		try {
			// Initialize IV
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			int ivSize = cipher.getBlockSize();
			this.iv = new byte[ivSize];
			RAND.nextBytes(iv);

			// Generate key from password
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec keySpec = new PBEKeySpec(this.password.toCharArray(), "ceci est du sel".getBytes(), 65536, 256); // TODO change salt
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

			// Initialize cipher
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(this.iv));

			// encrypt data
			byte[] cipherText = cipher.doFinal(jsonCalendar.getBytes());
			return Base64.getEncoder().encodeToString(cipherText);
		}
		catch (Exception e){
			return null;
		}
	}
}