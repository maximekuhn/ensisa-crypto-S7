package fr.uha.ensisa.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalendarTest {
	private Calendar sut; // System Under Test
	
	@BeforeEach
	public void init() {
		sut = new Calendar("Test");
	}
	
	@Test
	public void getEventTableTest() {
		Date date = new Date();
		Event event = new Event(date, 1, "Test", "...", "Localisation");
		sut.getEventTable().addEvent(event);
		assertEquals(event, sut.getEventTable().search(date));
	}
}
