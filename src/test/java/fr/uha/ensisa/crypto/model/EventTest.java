package fr.uha.ensisa.crypto.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventTest {
	private Event sut; // System Under Test
	private Date date;
	private long duration;
	private String event;
	private String description;
	private String location;

	@BeforeEach
	public void init() {
		date = new Date();
		duration = 1;
		event = "Test";
		description = "...";
		location = "ici";
		sut = new Event(date, duration, event, description, location);
	}

	@Test
	public void getDateTest() {
		assertEquals(date, sut.getDate());
		Date newDate = new Date(111111);
		sut.setDate(newDate);
		assertEquals(newDate, sut.getDate());
	}

	@Test
	public void getDurationTest() {
		assertEquals(duration, sut.getDuration());
		sut.setDuration(2);
		assertEquals(2, sut.getDuration());
	}

	@Test
	public void getEventTest() {
		assertEquals(event, sut.getEvent());
		sut.setEvent("a");
		assertEquals("a", sut.getEvent());
	}

	@Test
	public void getDescriptionTest() {
		assertEquals(description, sut.getDescription());
		sut.setDescription("nouvelle description");
		assertEquals("nouvelle description", sut.getDescription());
	}

	@Test
	public void getLocationTest() {
		assertEquals(location, sut.getLocation());
		sut.setLocation("nouvelle localisation");
		assertEquals("nouvelle localisation", sut.getLocation());
	}
}
