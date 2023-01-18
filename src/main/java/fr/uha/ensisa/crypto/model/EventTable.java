package fr.uha.ensisa.crypto.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a Java class that represents an event, which can have a date, a duration, a name, a description and a location.
 */
public class EventTable {

	/**
     * The list of events
     */
	private Map<Date, Event> events;

	/**
     * Constructor for creating a new EventTable object.
     */
	public EventTable() {
		this.events = new HashMap<>();
	}

	/**
     * Adds an event.
     * @param event the event to add
     */
	public void addEvent(Event event) {
		this.events.put(event.getDate(), event);
	}

	/**
     * Edits an event.
     * @param event the new event
     */
	public void editEvent(Event event) {
		this.events.remove(event.getDate());
		this.events.put(event.getDate(), event);
	}

	/**
     * Search an event.
     * @param date the date of the event
     */
	public Event search(Date date) {
		return this.events.get(date);
	}

	/**
     * Removes an event.
     * @param date the date of the event
     */
	public void removeEvent(Date date) {
		this.events.remove(date);
	}

	/**
     * Returns the list of events.
     * @return the list of events
     */
	public ArrayList<Event> getAllEvents() {
		ArrayList<Event> eventArrayList = new ArrayList<>();
		for (Event event : this.events.values()) {
			eventArrayList.add(event);
		}
		return eventArrayList;
	}
}
