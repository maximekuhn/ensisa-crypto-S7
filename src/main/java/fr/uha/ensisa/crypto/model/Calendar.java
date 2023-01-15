package fr.uha.ensisa.crypto.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.uha.ensisa.crypto.model.cryptography.AESHelper;

public class Calendar {

	private EventTable eventTable;
	private String name;

	/* cryptography */
	private String algorithm;
	private String password;

	/* AES */
	private byte[] iv;
	private byte[] salt;

	public Calendar(String name) {
		this.eventTable = new EventTable();
		this.name = name;
		this.algorithm = "";
		this.password = "";
	}

	public Calendar(String name, String algorithm, String password) {
		this(name);
		this.algorithm = algorithm;
		this.password = password;
		this.iv = null;
		this.salt = null;
	}

	public Calendar(String name, String algorithm, String password, byte[] iv, byte[] salt) {
		this(name, algorithm, password);
		this.iv = iv;
		this.salt = salt;
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

		// Write calendar into file
		ObjectMapper mapper = new ObjectMapper();
		String data = mapper.writeValueAsString(this.eventTable.getAllEvents());
		BufferedWriter writer = new BufferedWriter(new FileWriter("data/" + this.name));

		switch (this.algorithm) {
			case "AES":
				this.saveAESCalendar(data, writer);
				break;
			default: // NONE
				this.saveDefaultCalendar(data, writer);
				break;
		}

		writer.close();
	}

	private void saveDefaultCalendar(String data, BufferedWriter writer) throws IOException {
		// format is => NONE; data
		writer.write("NONE;" + data);
	}

	private void saveAESCalendar(String data, BufferedWriter writer) throws IOException {
		// format is => AES; iv; salt; encrypted data
		String encryptedData = this.encrypt(data);
		writer.write(
				this.algorithm + ";" +
						Base64.getEncoder().encodeToString(this.iv) + ";" +
						Base64.getEncoder().encodeToString(this.salt) + ";" +
						encryptedData);
	}

	String encrypt(String jsonCalendar) {
		switch (algorithm) {
			case "AES":
				return encryptAES(jsonCalendar);
			default: // should not be here
				return jsonCalendar;
		}
	}

	protected String decrypt(String encrypted) {
		switch (algorithm) {
			case "AES":
				return decryptAES(encrypted);
			default: // should not be here
				return encrypted;
		}
	}

	private String decryptAES(String encrypted) {
		AESHelper helper = new AESHelper(encrypted, this.password, this.iv, this.salt);
		try {
			return helper.decryptAES();
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			return null;
		}
	}

	private String encryptAES(String jsonCalendar) {
		AESHelper helper = new AESHelper(jsonCalendar, this.password, null, null);
		try {
			String encrypted = helper.encryptAES();
			this.iv = helper.getIV();
			this.salt = helper.getSalt();
			return encrypted;
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			return null;
		}
	}
}