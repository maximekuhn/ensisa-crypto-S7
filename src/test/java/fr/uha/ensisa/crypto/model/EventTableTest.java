package fr.uha.ensisa.crypto.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventTableTest {
	private EventTable sut; // System Under Test
	private Date date;
	private long duration;
	private String event;
	private String description;
	private String location;
	private Event eventObj;

	@BeforeEach
	public void init() {
		this.date = new Date(4444);
		this.duration = 1;
		this.event = "Test";
		this.description = "...";
		this.location = "ici";
		this.eventObj = new Event(this.date, this.duration, this.event, this.description, this.location);
		this.sut = new EventTable();
	}

	@Test
	public void addEventTest() {
		this.sut.addEvent(this.eventObj);
		assertEquals(this.eventObj, this.sut.search(this.date));
		assertEquals(this.description, this.sut.search(this.date).getDescription());
	}

	@Test
	public void editEventTest() {
		this.sut.editEvent(new Event(this.date, this.duration, this.event, "nouvelle description", this.location));
		assertEquals("nouvelle description", this.sut.search(this.date).getDescription());
	}

	@Test
	public void removeEventTest() {
		this.sut.addEvent(this.eventObj);
		this.sut.removeEvent(this.date);
		assertNull(this.sut.search(this.date));
	}

	@Test
	public void getAllEventsTest() {
		this.sut.addEvent(this.eventObj);
		this.sut.addEvent(new Event(new Date(5555), this.duration, this.event, this.description, this.location));
		assertEquals(2, this.sut.getAllEvents().size());
	}
}
