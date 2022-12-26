package fr.uha.ensisa.crypto.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Calendar {
	private EventTable eventTable;
	private String name;
	private String algorithm;
	private String password;
	
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
	
	private String encrypt(String jsonCalendar) {
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
		return null;
	}

	private String encryptAES(String jsonCalendar) {
		return null;
	}
}