package fr.uha.ensisa.crypto.model;

import java.util.Date;

public class Event {

	private Date date;
	private long duration;
	private String event;
	private String description;
	private String location;
	
    public Event() {
    }
	
	public Event(Date date, long duration, String event, String description, String location) {
		this.setDate(date);
		this.setDuration(duration);
		this.setEvent(event);
		this.setDescription(description);
		this.setLocation(location);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
