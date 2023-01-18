package fr.uha.ensisa.crypto.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.uha.ensisa.crypto.model.cryptography.Network;

/**
 * An implementation of an agenda that stores multiple calendars. 
 * It allows creating and loading calendars, as well as listing all the calendar names that are currently stored. 
 * The class is implemented as a singleton, meaning there is only one instance of it.
 */
public final class Agenda {

	/**
     * A {@link HashMap} that store all the loaded calendars with their name as key
     */
	private Map<String, Calendar> calendars = new HashMap<String, Calendar>();

	/**
     * Instance of the Agenda as the class is a singleton.
     * Generated at the first call of {@link Agenda#getInstance()}.
     */
	private static Agenda instance;

	/**
	 * On first call, generate the {@link Agenda#instance} of the {@link Agenda} 
     * @return the {@link Agenda#instance} of the {@link Agenda}
     */
	public static Agenda getInstance() {
		if (instance == null) {
			instance = new Agenda();
		}
		return instance;
	}

	/**
	 * Lists all the names of the existing calendars, loaded or not, by reading file names in the data directory.
     * @return names of all calendars existing
     */
	public Collection<String> getCalendarNames() {
		File dir = new File("data/");
		ArrayList<String> names = new ArrayList<String>();
		try {
			Files.list(dir.toPath()).toList().forEach(path -> names.add(path.getFileName().toString()));
			return names;
		} catch (IOException e) {
			return new ArrayList<String>();
		}
	}

	/**
	 * Creates a {@link Calendar} with no encryption algorithm, stores it in {@link Agenda#calendars} and saves it.
	 * @param name the name of the calendar
	 * @throws IOException if an error occurs while creating or writing to the file
	 * @throws Error the calendar already exists
	 * @see Calendar#saveCalendar()
     */
	public void createCalendar(String name) throws IOException, Error {
		if (getCalendarNames().contains(name))
			throw new Error("Calendar already exists");
		calendars.put(name, new Calendar(name));
		calendars.get(name).saveCalendar();
	}

	/**
	 * Create a crypted {@link Calendar}, store it in {@link Agenda#calendars} and save it.
     * @param name the name of the calendar
     * @param algorithm the encryption algorithm used for the calendar
     * @param password the password used for the encryption
	 * @throws IOException if an error occurs while creating or writing to the file
	 * @throws Error the calendar already exists
	 * @see Calendar#saveCalendar()
     */
	public void createCalendar(String name, String algorithm, String password) throws IOException, Error {
		if (getCalendarNames().contains(name))
			throw new Error("Calendar already exists");
		calendars.put(name, new Calendar(name, algorithm, password));
		calendars.get(name).saveCalendar();
	}

	/**
	 * Load calendar from specified file.
	 * 
	 * @param pathToFile to load (calendar name).
	 * @param password   password needed to decrypt file (if calendar is crypted).
	 *                   Can be empty if calendar to load is not crypted.
	 * @return boolean indicating if calendar has been successfully loaded (true) or
	 *         not (false).
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public boolean loadCalendar(String pathToFile, String password) throws IOException, ClassNotFoundException {
		File file = new File("data/" + pathToFile);
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line);
			}
		}

		String[] fileContent = resultStringBuilder.toString().split(";");

		// choose between NONE, AES, RC5, ...
		Calendar calendar = null;
		String data = null;
		switch (fileContent[0]) {
			case "AES":
				calendar = this.loadAESCalendar(pathToFile, password, fileContent);
				data = calendar.decrypt(fileContent[3]);
				break;
			case "RC5":
				calendar = this.loadRC5Calendar(pathToFile, password, fileContent);
				data = calendar.decrypt(fileContent[3]);
				break;
			default: // NONE
				calendar = this.loadDefaultCalendar(pathToFile);
				data = fileContent[1];
				break;
		}

		if (data == null)
			return false;

		ObjectMapper objectMapper = new ObjectMapper();
		List<Event> events = objectMapper.readValue(data,
				objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Event.class));
		for (Event event : events) {
			calendar.getEventTable().addEvent(event);
		}
		calendars.put(pathToFile, calendar);
		return true;
	}

	/**
	 * Receive a calendar from a specified IP address and decrypt it using RSA.
	 * Once the calendar received, create a new calendar encrypted with the specified algorithm.
	 * 
	 * @param name the name of the calendar
	 * @param algorithm the encryption algorithm used to encrypt the calendar
	 * @param password the password used to decrypt the calendar
	 * @throws IOException if an error occurs while reading from the input stream
	 * @throws ClassNotFoundException if the class of the serialized object cannot be found
	 * @throws InvalidKeyException if the key is invalid
	 * @throws NoSuchAlgorithmException if the specified algorithm is not available
	 * @throws NoSuchPaddingException if the specified padding scheme is not available
	 * @throws IllegalBlockSizeException if the length of data provided to the cipher is incorrect
	 * @throws BadPaddingException if the data is not padded properly
	 */
	public void recieveCalendar(String name, String algorithm, String password)
			throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		ObjectMapper objectMapper = new ObjectMapper();

		Calendar calendar = new Calendar(name, algorithm, password);

		List<Event> events = objectMapper.readValue(Network.getInstance().reciever(),
				objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Event.class));
		for (Event event : events) {
			calendar.getEventTable().addEvent(event);
		}
		calendars.put(name, calendar);
		calendars.get(name).saveCalendar();
	}

	/**
	 * Receive a calendar from a specified IP address and decrypt it using RSA.
	 * Once the calendar received, create a new calendar with no encryption algorithm.
	 * 
	 * @param name the name of the calendar
	 * @param algorithm the encryption algorithm used to encrypt the calendar
	 * @param password the password used to decrypt the calendar
	 * @throws IOException if an error occurs while reading from the input stream
	 * @throws ClassNotFoundException if the class of the serialized object cannot be found
	 * @throws InvalidKeyException if the key is invalid
	 * @throws NoSuchAlgorithmException if the specified algorithm is not available
	 * @throws NoSuchPaddingException if the specified padding scheme is not available
	 * @throws IllegalBlockSizeException if the length of data provided to the cipher is incorrect
	 * @throws BadPaddingException if the data is not padded properly
	 */
	public void recieveCalendar(String name)
			throws JsonMappingException, InvalidKeyException, JsonProcessingException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();

		Calendar calendar = new Calendar(name);

		List<Event> events = objectMapper.readValue(Network.getInstance().reciever(),
				objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Event.class));
		for (Event event : events) {
			calendar.getEventTable().addEvent(event);
		}
		calendars.put(name, calendar);
		calendars.get(name).saveCalendar();
	}

	/**
	 * Load an RC5 crypted Calendar.
	 * 
	 * @param name        of the calendar to load.
	 * @param password    password needed to decrypt the calendar to load.
	 * @param fileContent String[] containing IV and salt needed for RC5 decryption
	 *                    process.
	 * @return a new Calendar containing all of the above parameters.
	 * @see Calendar
	 * @see fr.uha.ensisa.crypto.model.cryptography.RC5Helper
	 */
	private Calendar loadRC5Calendar(String name, String password, String[] fileContent) {
		String algorithm = fileContent[0];
		byte[] iv = Base64.getDecoder().decode(fileContent[1]);
		byte[] salt = Base64.getDecoder().decode(fileContent[2]);
		return new Calendar(name, algorithm, password, iv, salt);
	}

	/**
	 * Load an AES crypted Calendar.
	 * 
	 * @param name        of the calendar to load.
	 * @param password    password needed to decrypt the calendar to load.
	 * @param fileContent String[] containing IV and salt needed for AES decryption
	 *                    process.
	 * @return a new Calendar containing all of the above parameters.
	 * @see Calendar
	 * @see fr.uha.ensisa.crypto.model.cryptography.AESHelper
	 */
	private Calendar loadAESCalendar(String name, String password, String[] fileContent) {
		String algorithm = fileContent[0];
		byte[] iv = Base64.getDecoder().decode(fileContent[1]);
		byte[] salt = Base64.getDecoder().decode(fileContent[2]);
		return new Calendar(name, algorithm, password, iv, salt);
	}

	/**
	 * Load a non crypted calendar.
	 * 
	 * @param name of the calendar to load.
	 * @return new Calendar containing the above parameter.
	 */
	private Calendar loadDefaultCalendar(String name) {
		return new Calendar(name);
	}

	/**
	 * Check if a calendar is crypted from his corresponding file.
	 * @param pathToFile to check (calendar name).
	 * @return boolean indicating if calendar is crypted.
	 * @throws IOException if an error occurs while reading to the file
	 */
	public boolean isCrypted(String pathToFile) throws IOException {
		File file = new File("data/" + pathToFile);
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line);
			}
		}
		String[] fileContent = resultStringBuilder.toString().split(";");
		return !fileContent[0].equals("NONE");
	}

	/**
	 * Returns a loaded calendar.
	 * @param name name of the loaded calendar.
	 * @return the loaded calendar.
	 * @throws IOException if an error occurs while reading to the file
	 */
	public Calendar getCalendar(String name) throws Error {
		Calendar calendar = calendars.get(name);
		if (calendar == null)
			throw new Error("Calendar doesn't exists");
		return calendar;
	}

	/**
	 * Delete the file of a calendar and unload it if it was loaded.
	 * @param path_to_file to delete (calendar name).
	 * @return the loaded calendar.
	 * @throws IOException if the file doesn't exists
	 * @see Agenda#unloadCalendar(String)
	 */
	public void deleteCalendar(String path_to_file) throws IOException {
		File file = new File("data/" + path_to_file);
		if (!file.exists())
			throw new IOException("File not found");
		file.delete();
		unloadCalendar(path_to_file);
	}

	/**
	 * Remove the specified calendar from {@link Agenda#calendars}. 
	 * @param name name of the loaded calendar.
	 */
	public void unloadCalendar(String name) {
		calendars.remove(name);
	}

	/**
	 * Search all events at a specified date from all the {@link Agenda#calendars} that are loaded. 
	 * @param date specified date of searched events.
	 * @return a collection of all events happening on the specified date.
	 */
	public Collection<Event> search(Date date) {
		ArrayList<Event> events = new ArrayList<Event>();
		for (Calendar calendar : calendars.values()) {
			Event event = calendar.getEventTable().search(date);
			if (event != null)
				events.add(event);
		}
		return events;
	}

	/**
	 * Retrieve all events of the {@link Agenda#calendars} that are loaded. 
	 * @return a collection of all events.
	 */
	public Collection<Event> getAllEvents() {
		ArrayList<Event> events = new ArrayList<Event>();
		for (Calendar calendar : calendars.values()) {
			Collection<Event> eventsOfCalendar = calendar.getEventTable().getAllEvents();
			for (Event event : eventsOfCalendar) {
				events.add(event);
			}
		}
		return events;
	}

	/**
	 * Retrieve all the loaded calendars from {@link Agenda#calendars}.
	 * @return all the loaded calendars.
	 */
	public Collection<Calendar> getAllCalendars() {
		return calendars.values();
	}

}
