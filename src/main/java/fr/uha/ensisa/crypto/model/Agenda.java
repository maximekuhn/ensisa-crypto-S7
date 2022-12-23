package fr.uha.ensisa.crypto.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class Agenda {
	
	private Map<String,Calendar> calendars = new HashMap<String,Calendar>();
	
    private static Agenda instance;
	
    public static Agenda getInstance() {
        if (instance == null) {
            instance = new Agenda();
        }
        return instance;
    }
    
	public Collection<String> getCalendarNames(){
        File dir = new File("data/");
        ArrayList<String> names = new ArrayList<String>();
        try {
        	Files.list(dir.toPath()).toList().forEach(path->names.add(path.getFileName().toString()));
        	return names;
		} catch (IOException e) {
			return new ArrayList<String>();
		}
	}
	
    
    public void createCalendar(String name) throws IOException, Error {
    	calendars.put(name, new Calendar(name));
    	calendars.get(name).saveCalendar();
    }
    
    /*public void loadCalendar(String path_to_file) throws IOException, ClassNotFoundException {
    	File file = new File("data/"+path_to_file);
		FileInputStream fileInputStream = new FileInputStream(file);
    	ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream); 
		@SuppressWarnings("unchecked")
		ArrayList<Event> events = (ArrayList<Event>) objectInputStream.readObject();
		Calendar calendar = new Calendar(path_to_file);
		for (Event event : events) {
			calendar.getEventTable().addEvent(event);
		}
		objectInputStream.close();
    }*/
    
    public Calendar getCalendar(String name) throws Error {
    	Calendar calendar = calendars.get(name);
    	if (calendar==null)
    		throw new Error("Calendar doesn't exists");
    	return calendar;
    }
    
    public void deleteCalendar(String path_to_file) throws IOException {
    	File file = new File("data/"+path_to_file);
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
}
