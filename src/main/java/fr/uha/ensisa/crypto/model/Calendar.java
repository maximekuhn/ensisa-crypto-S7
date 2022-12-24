package fr.uha.ensisa.crypto.model;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Calendar {
	private EventTable eventTable;
	private String name;
	
	public Calendar(String name) {
		eventTable = new EventTable();
		this.name = name;
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
	    mapper.writeValue(new File("data/" + name), eventTable.getAllEvents());
	}
	
}
