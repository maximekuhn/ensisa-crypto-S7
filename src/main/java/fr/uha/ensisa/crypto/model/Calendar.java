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
import fr.uha.ensisa.crypto.model.cryptography.Network;
import fr.uha.ensisa.crypto.model.cryptography.RC5Helper;

/**
 * This is a Java class that represents a calendar, which can have events added to it and can be saved to a file or sent to a specified address.
 * It uses encryption algorithm like AES and RC5 to encrypt the data before saving it to a file or sending it over the network.
 */
public class Calendar {

	/**
     * A table that holds all the events in the calendar
     */
	private EventTable eventTable;

	/**
     * The name of the calendar
     */
	private String name;

	/* cryptography */
	/**
     * The encryption algorithm used for the calendar
     */
	private String algorithm;
	/**
    The password used for the encryption and decryption
    */
	private String password;

	/* AES */
	/**
     * The initialization vector used for the encryption
     */
	private byte[] iv;
	/**
     * The salt used for the encryption
     */
	private byte[] salt;

	/**
     * Constructor for creating a new Calendar object with a specified name.
     * @param name the name of the calendar
     */
	public Calendar(String name) {
		this.eventTable = new EventTable();
		this.name = name;
		this.algorithm = "";
		this.password = "";
	}

	/**
     * Constructor for creating a new Calendar object with a specified name, encryption algorithm, and password.
     * @param name the name of the calendar
     * @param algorithm the encryption algorithm used for the calendar
     * @param password the password used for the encryption
     */
	public Calendar(String name, String algorithm, String password) {
		this(name);
		this.algorithm = algorithm;
		this.password = password;
		this.iv = null;
		this.salt = null;
	}

	/**
    * Constructor for creating a new Calendar object with a specified name, encryption algorithm, password, initialization vector, and salt used for AES.
    * @param name the name of the calendar
    * @param algorithm the encryption algorithm used for the calendar
    * @param password the password used for the encryption
    * @param iv the initialization vector used for the encryption
    * @param salt the salt used for the encryption
    */
	public Calendar(String name, String algorithm, String password, byte[] iv, byte[] salt) {
		this(name, algorithm, password);
		this.iv = iv;
		this.salt = salt;
	}

	/**
     * Returns the event table of the calendar.
     * @return the event table of the calendar
     */
	public EventTable getEventTable() {
		return this.eventTable;
	}

	/**
     * Returns the name of the calendar.
     * @return the name of the calendar
     */
	public String getName() {
		return this.name;
	}

	/**
     * Saves the calendar to a file. The file is saved in the "data" directory.
     * The eventTable is serialized in JSON format and is then encrypted before being written to the file.
     * The format is algorithm;data
     * @throws IOException if an error occurs while creating or writing to the file
     */
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
			case "RC5":
				this.saveRC5Calendar(data, writer);
				break;
			default: // NONE
				this.saveDefaultCalendar(data, writer);
				break;
		}

		writer.close();
	}

	/**
	 * Sends the calendar to a specified address.
	 * The data is encrypted in RSA before being sent.
	 * @param adress the IP address to send the calendar to
	 * @throws IOException if an error occurs while sending the data
	 * @throws InvalidKeyException if the encryption key is invalid
	 * @throws NoSuchAlgorithmException if the specified encryption algorithm is not available
	 * @throws NoSuchPaddingException if the specified padding scheme is not available
	 * @throws IllegalBlockSizeException if the data is not the correct size for the encryption algorithm
	 * @throws BadPaddingException if the data is not padded correctly for the encryption algorithm
	 * @throws InvalidKeySpecException if the encryption key is not valid
	 */
	public void sendCalendar(String adress) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		ObjectMapper mapper = new ObjectMapper();
		Network.getInstance().sender(mapper.writeValueAsString(this.eventTable.getAllEvents()), adress);
	}

	private void saveRC5Calendar(String data, BufferedWriter writer) throws IOException {
		// format is => RC5; iv; salt; encrypted data
		String encryptedData = this.encrypt(data);
		writer.write(
				this.algorithm + ";" +
						Base64.getEncoder().encodeToString(this.iv) + ";" +
						Base64.getEncoder().encodeToString(this.salt) + ";" +
						encryptedData);

	}

	/**
	* Saves the calendar to a file without encryption.
	* The format is NONE;data
	*
	* @param data the calendar data to be written to the file
	* @param writer the writer object used to write to the file
	* @throws IOException if an error occurs while writing to the file
	*/
	private void saveDefaultCalendar(String data, BufferedWriter writer) throws IOException {
		// format is => NONE; data
		writer.write("NONE;" + data);
	}

	/**
	 * Saves the calendar to a file using the AES encryption algorithm.
	 * The data is encrypted before being written to the file.
	 * The format is AES;iv;salt;encrypted data
	 *
	 * @param data the calendar data to be written to the file
	 * @param writer the writer object used to write to the file
	 * @throws IOException if an error occurs while writing to the file
	 */
	private void saveAESCalendar(String data, BufferedWriter writer) throws IOException {
		// format is => AES; iv; salt; encrypted data
		String encryptedData = this.encrypt(data);
		writer.write(
				this.algorithm + ";" +
						Base64.getEncoder().encodeToString(this.iv) + ";" +
						Base64.getEncoder().encodeToString(this.salt) + ";" +
						encryptedData);
	}

	/**
	 * Encrypts the calendar data using the specified encryption algorithm.
	 * If the {@link Calendar#algorithm} is unknown, returns the String as given.
	 *
	 * @param jsonCalendar the calendar data to be encrypted
	 * @return the encrypted calendar data
	 */
	private String encrypt(String jsonCalendar) {
		switch (this.algorithm) {
			case "AES":
				return this.encryptAES(jsonCalendar);
			case "RC5":
				return this.encryptRC5(jsonCalendar);
			default: // should not be here
				return jsonCalendar;
		}
	}

	/**
	 * Decrypts the calendar data using the specified encryption algorithm.
	 * If the {@link Calendar#algorithm} is unknown, returns the String as given.
	 *
	 * @param encrypted the calendar data to be decrypted
	 * @return the decrypted calendar data
	 */
	protected String decrypt(String encrypted) {
		switch (this.algorithm) {
			case "AES":
				return this.decryptAES(encrypted);
			case "RC5":
				return this.decryptRC5(encrypted);
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

	private String decryptRC5(String encrypted) {
		RC5Helper helper = new RC5Helper(encrypted, this.password, this.iv, this.salt);
		try {
			return helper.decryptRC5();
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException
				| InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			return null;
		}
	}

	private String encryptRC5(String jsonCalendar) {
		RC5Helper helper = new RC5Helper(jsonCalendar, this.password, null, null);
		try {
			String encrypted = helper.encryptRC5();
			this.iv = helper.getIV();
			this.salt = helper.getSalt();
			return encrypted;
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			return null;
		}
	}
}