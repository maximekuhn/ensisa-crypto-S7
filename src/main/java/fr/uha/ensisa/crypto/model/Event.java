package fr.uha.ensisa.crypto.model;

import java.util.Date;

/**
 * This is a Java class that represents an event, which can have a date, a duration, a name, a description and a location.
 */
public class Event {

	/**
     * The date of the event
     */
	private Date date;
	
	/**
     * The duration of the event
     */
	private long duration;
	
	/**
     * The name of the event
     */
	private String event;
	
	/**
     * The description of the event
     */
	private String description;
	
	/**
     * The location of the event
     */
	private String location;

	/**
     * Constructor for creating a new Event object.
     */
	public Event() {
	}

	/**
     * Constructor for creating a new Event object.
     * @param date the date of the event
     * @param duration the duration of the event
     * @param event the name of the event
     * @param description the description of the event
     * @param location the location of the event
     */
	public Event(Date date, long duration, String event, String description, String location) {
		this.setDate(date);
		this.setDuration(duration);
		this.setEvent(event);
		this.setDescription(description);
		this.setLocation(location);
	}

	/**
     * Returns the date of the event.
     * @return the date of the event
     */
	public Date getDate() {
		return date;
	}

	/**
     * Sets the date of the event.
     */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
     * Returns the duration of the event.
     * @return the duration of the event
     */
	public long getDuration() {
		return duration;
	}

	/**
     * Sets the duration of the event.
     */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
     * Returns the name of the event.
     * @return the name of the event
     */
	public String getEvent() {
		return event;
	}

	/**
     * Sets the name of the event.
     */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
     * Returns the description of the event.
     * @return the description of the event
     */
	public String getDescription() {
		return description;
	}

	/**
     * Sets the description of the event.
     */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
     * Returns the location of the event.
     * @return the location of the event
     */
	public String getLocation() {
		return location;
	}

	/**
     * Sets the location of the event.
     */
	public void setLocation(String location) {
		this.location = location;
	}

}
