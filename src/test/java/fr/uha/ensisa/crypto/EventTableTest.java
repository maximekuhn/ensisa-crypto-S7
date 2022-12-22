package fr.uha.ensisa.crypto;

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
		date = new Date();
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
		sut.removeEvent(date);
		assertNull(sut.search(date));
	}
}
