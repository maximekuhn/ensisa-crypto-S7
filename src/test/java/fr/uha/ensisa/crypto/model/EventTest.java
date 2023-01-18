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
		this.date = new Date();
		this.duration = 1;
		this.event = "Test";
		this.description = "...";
		this.location = "ici";
		this.sut = new Event(this.date, this.duration, this.event, this.description, this.location);
	}

	@Test
	public void getDateTest() {
		assertEquals(this.date, this.sut.getDate());
		Date newDate = new Date(111111);
		this.sut.setDate(newDate);
		assertEquals(newDate, this.sut.getDate());
	}

	@Test
	public void getDurationTest() {
		assertEquals(this.duration, this.sut.getDuration());
		this.sut.setDuration(2);
		assertEquals(2, this.sut.getDuration());
	}

	@Test
	public void getEventTest() {
		assertEquals(this.event, this.sut.getEvent());
		this.sut.setEvent("a");
		assertEquals("a", this.sut.getEvent());
	}

	@Test
	public void getDescriptionTest() {
		assertEquals(this.description, this.sut.getDescription());
		this.sut.setDescription("nouvelle description");
		assertEquals("nouvelle description", this.sut.getDescription());
	}

	@Test
	public void getLocationTest() {
		assertEquals(this.location, this.sut.getLocation());
		this.sut.setLocation("nouvelle localisation");
		assertEquals("nouvelle localisation", this.sut.getLocation());
	}
}
