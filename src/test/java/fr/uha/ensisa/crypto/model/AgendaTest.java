package fr.uha.ensisa.crypto.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgendaTest {
	
	private static Agenda agenda;
	private Date date;
	private long duration;
	private String eventname;
	private String description;
	private String location;
	private Event event;
	
	@BeforeAll
	public static void setUpBeforeClass() {
		agenda = Agenda.getInstance();
	}
	
	@BeforeEach
	public void setUp() throws IOException, Error {
		date = new Date(4444);
		duration = 1;
		eventname = "Test";
		description = "...";
		location = "ici";
		agenda.createCalendar("Test Calendar");
		event = new Event(date, duration, eventname, description, location);
		agenda.getCalendar("Test Calendar").getEventTable().addEvent(event);
	}
	
	@Test
	public void testGetCalendarNames() {
		Collection<String> calendarNames = agenda.getCalendarNames();
		assertEquals(1, calendarNames.size());
	}
	
	@Test
	public void testSearch() {
		Event event = agenda.getCalendar("Test Calendar").getEventTable().getAllEvents().iterator().next();
		Collection<Event> searchResults = agenda.search(event.getDate());
		assertEquals(event, searchResults.iterator().next());
	}
	
	@Test
	public void testGetAllEvents() {
		Collection<Event> allEvents = agenda.getAllEvents();
		assertEquals(1, allEvents.size());
	}
	
	@AfterEach
	public void tearDown() throws IOException {
		agenda.deleteCalendar("Test Calendar");
	}
	
}