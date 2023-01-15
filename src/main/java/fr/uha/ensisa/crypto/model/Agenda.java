package fr.uha.ensisa.crypto.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Agenda {

	private Map<String, Calendar> calendars = new HashMap<String, Calendar>();

	private static Agenda instance;

	public static Agenda getInstance() {
		if (instance == null) {
			instance = new Agenda();
		}
		return instance;
	}

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

	public void createCalendar(String name) throws IOException, Error {
		if (getCalendarNames().contains(name))
			throw new Error("Calendar already exists");
		calendars.put(name, new Calendar(name));
		calendars.get(name).saveCalendar();
	}

	public void createCalendar(String name, String algorithm, String password) throws IOException, Error {
		if (getCalendarNames().contains(name))
			throw new Error("Calendar already exists");
		calendars.put(name, new Calendar(name, algorithm, password));
		calendars.get(name).saveCalendar();
	}

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

	private Calendar loadRC5Calendar(String name, String password, String[] fileContent) {
		String algorithm = fileContent[0];
		byte[] iv = Base64.getDecoder().decode(fileContent[1]);
		byte[] salt = Base64.getDecoder().decode(fileContent[2]);
		return new Calendar(name, algorithm, password, iv, salt);
	}

	private Calendar loadAESCalendar(String name, String password, String[] fileContent) {
		String algorithm = fileContent[0];
		byte[] iv = Base64.getDecoder().decode(fileContent[1]);
		byte[] salt = Base64.getDecoder().decode(fileContent[2]);
		return new Calendar(name, algorithm, password, iv, salt);
	}

	private Calendar loadDefaultCalendar(String name) {
		return new Calendar(name);
	}

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

	public Calendar getCalendar(String name) throws Error {
		Calendar calendar = calendars.get(name);
		if (calendar == null)
			throw new Error("Calendar doesn't exists");
		return calendar;
	}

	public void deleteCalendar(String path_to_file) throws IOException {
		File file = new File("data/" + path_to_file);
		if (!file.exists())
			throw new IOException("File not found");
		file.delete();
		unloadCalendar(path_to_file);
	}

	public void unloadCalendar(String name) {
		calendars.remove(name);
	}

	public Collection<Event> search(Date date) {
		ArrayList<Event> events = new ArrayList<Event>();
		for (Calendar calendar : calendars.values()) {
			Event event = calendar.getEventTable().search(date);
			if (event != null)
				events.add(event);
		}
		return events;
	}

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

	public Collection<Calendar> getAllCalendars() {
		return calendars.values();
	}
}
