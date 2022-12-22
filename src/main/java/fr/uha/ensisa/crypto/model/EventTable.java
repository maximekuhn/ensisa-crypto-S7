package fr.uha.ensisa.crypto.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventTable {
	private Map<Date, Event> events;
	
	public EventTable() {
		events = new HashMap<>();
	}
	
	public void addEvent(Event event) {
		events.put(event.getDate(), event);
	}
	
	public void editEvent(Event event) {
		events.remove(event.getDate());
		events.put(event.getDate(), event);
	}
	
	public Event search(Date date) {
		return events.get(date);
	}
	
	public void removeEvent(Date date) {
		events.remove(date);
	}
}
