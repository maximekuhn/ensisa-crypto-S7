package fr.uha.ensisa.crypto.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.uha.ensisa.crypto.model.cryptography.AESHelper;
import fr.uha.ensisa.crypto.model.cryptography.DESHelper;

public class Calendar {
	private EventTable eventTable;
	private String name;

	/* cryptography */
	private String algorithm;
	private String password;

	/* AES */
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
		this.iv = null;
	}
	
	public Calendar(String name, String algorithm, String password,byte[] iv) {
		eventTable = new EventTable();
		this.name = name;
		this.algorithm = algorithm;
		this.password = password;
		this.iv = iv;
	}
	
	public EventTable getEventTable() {
		return eventTable;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void saveCalendar() throws IOException {
		// TODO add IV from AESHelper if encrypted using AES CBC
	    File dir = new File("data");
	    if (!dir.isDirectory()) {
	        if (!dir.mkdir()) {
	            throw new IOException("Can't create the data directory");
	        }
	    }

	    ObjectMapper mapper = new ObjectMapper();
	    String encrypted = encrypt(mapper.writeValueAsString(eventTable.getAllEvents()));
	    BufferedWriter writer = new BufferedWriter(new FileWriter("data/"+name));
	    if (iv == null)
	    	writer.write(algorithm+";;"+encrypted);
	    else
	    	writer.write(algorithm+";"+Base64.getEncoder().encodeToString(iv)+";"+encrypted);
	    writer.close();
	}
	
	String encrypt(String jsonCalendar) {
		switch (algorithm) {
			case "AES":
				return encryptAES(jsonCalendar);
			case "HMAC":
				return encryptDES(jsonCalendar);
			default:
				return jsonCalendar;
		}
	}

	protected String decrypt(String encrypted) {
		switch (algorithm) {
			case "AES":
				return decryptAES(encrypted);
			case "DES":
				return decryptDES(encrypted);
			default:
				return encrypted;
		}
	}

	private String decryptAES(String encrypted) {
		AESHelper helper = new AESHelper(encrypted, this.password, this.iv);
		try {
			return helper.decryptAES();
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String encryptAES(String jsonCalendar) {
		AESHelper helper = new AESHelper(jsonCalendar, this.password, null);
		try {
			String encrypted = helper.encryptAES();
			this.iv = helper.getIV();
			return encrypted;
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String decryptDES(String encrypted) {
		DESHelper helper = new DESHelper(encrypted, this.password);
		try {
			return helper.decryptDES();
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException
				| InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private String encryptDES(String jsonCalendar) {
		DESHelper helper = new DESHelper(jsonCalendar, this.password);
		try {
			return helper.encryptDES();
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException
				| InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}