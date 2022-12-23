package fr.uha.ensisa.crypto.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.uha.ensisa.crypto.model.Event;
import fr.uha.ensisa.crypto.model.EventTable;

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
		date = new Date(4444);
		duration = 1;
		event = "Test";
		description = "...";
		location = "ici";
		eventObj = new Event(date, duration, event, description, location);
		sut = new EventTable();
	}
	
	@Test
	public void addEventTest() {
		sut.addEvent(eventObj);
		assertEquals(eventObj, sut.search(date));
		assertEquals(description, sut.search(date).getDescription());
	}
	
	@Test
	public void editEventTest() {
		sut.editEvent(new Event(date, duration, event, "nouvelle description", location));
		assertEquals("nouvelle description", sut.search(date).getDescription());
	}
	
	@Test
	public void removeEventTest() {
		sut.addEvent(eventObj);
		sut.removeEvent(date);
		assertNull(sut.search(date));
	}
	
	
	@Test
	public void getAllEventsTest() {
		sut.addEvent(eventObj);
		sut.addEvent(new Event(new Date(5555), duration, event, description, location));
		assertEquals(2,sut.getAllEvents().size());
	}
}
