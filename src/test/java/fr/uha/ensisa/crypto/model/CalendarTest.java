package fr.uha.ensisa.crypto.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalendarTest {
	private Calendar sut; // System Under Test

	@BeforeEach
	public void init() {
		this.sut = new Calendar("Test");
	}

	@Test
	public void getEventTableTest() {
		Date date = new Date();
		Event event = new Event(date, 1, "Test", "...", "Localisation");
		this.sut.getEventTable().addEvent(event);
		assertEquals(event, this.sut.getEventTable().search(date));
	}

	@Test
	public void testSaveCalendar() throws IOException {
		this.sut.saveCalendar();

		File file = new File("data/Test");
		assertTrue(file.exists());
		file.delete();
	}

}
