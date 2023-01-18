package fr.uha.ensisa.crypto.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
		assertTrue(calendarNames.contains("Test Calendar"));
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

	@Test
	public void testLoadCalendar() throws IOException, ClassNotFoundException {
		agenda.getCalendar("Test Calendar").saveCalendar();
		agenda.unloadCalendar("Test Calendar");
		agenda.loadCalendar("Test Calendar", "");
		assertEquals(event.getDuration(),
				agenda.getCalendar("Test Calendar").getEventTable().getAllEvents().get(0).getDuration());
	}
	
	@Test
	@DisplayName("Test creating calendar with algorithm and password")
	public void testCreatingCalendarAlgoPassword() {
		// test when name is already taken
		String name = "Test Calendar"; // already taken, @BeforeEach
		String algorithm = "algo";
		String password = "VerySecurePassword123456";
		assertThrows(Error.class, () -> agenda.createCalendar(name, algorithm, password));
		
		// test when name is not taken
		String newName = "my excellent calendar";
		try {
			agenda.createCalendar(newName, algorithm, password);
			assertTrue(agenda.getCalendarNames().contains(newName));
			Calendar c = agenda.getCalendar(newName);
			assertNotNull(c);
			
			// delete calendar
			agenda.deleteCalendar(newName);
		} catch (IOException e) {
			fail("No exception should be thrown");
		} catch (Error e) {
			fail("No exception should be thrown");
		}
	}
	
	

	@AfterEach
	public void tearDown() throws IOException {
		agenda.deleteCalendar("Test Calendar");
	}

}