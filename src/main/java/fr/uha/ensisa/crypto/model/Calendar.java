package fr.uha.ensisa.crypto.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
	
	public void saveCalendar() throws Error, IOException {
		File dir = new File("data");
		if (!dir.isDirectory())
			if (!dir.mkdir()) 
				throw new Error("Can't create the data directory");
        FileOutputStream fileOutputStream = new FileOutputStream("data/"+name);
        ObjectOutputStream ObjectOutputStream = new ObjectOutputStream(fileOutputStream);
        ObjectOutputStream.writeObject(eventTable.getAllEvents());
        ObjectOutputStream.close();
	}
	
}
