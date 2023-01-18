package fr.uha.ensisa.crypto.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.model.Calendar;
import fr.uha.ensisa.crypto.model.Event;
import fr.uha.ensisa.crypto.ui.calendar_selection.CalendarListPanel;
import fr.uha.ensisa.crypto.ui.topbar.TopBarPanel;

/**
 * Controller that handles every user interactions. This controller is used by many classes in the UI.
 */
public class MainWindowController implements MouseListener, MouseMotionListener, KeyListener {

	private Agenda agenda;
	private CalendarPanel calendarPanel;
	private CalendarListPanel calendarListPanel;
	private TopBarPanel topBarPanel;

	/**
	 * Constructor.
	 * @param agenda singleton used to manage calendars and events. The controller interact with the agenda to do everything.
	 */
	public MainWindowController(Agenda agenda) {
		this.agenda = agenda;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @return the singleton instance of Agenda
	 * @see fr.uha.ensisa.crypto.model.Agenda
	 */
	public Agenda getAgenda() {
		return this.agenda;
	}

	/**
	 * Create an event in the select calendar
	 * @param calendar the calendar to add the event in
	 * @param event event name
	 * @param description event description
	 * @param location event location
	 * @param date event date
	 * @param duration event duration
	 * @throws IOException
	 * @throws Error
	 */
	public void createEvent(String calendar, String event, String description, String location, Date date,
			double duration) throws IOException, Error {
		Event newEvent = new Event(date, (long) duration, event, description, location);

		this.agenda.getCalendar(calendar).getEventTable().addEvent(newEvent);
		this.agenda.getCalendar(calendar).saveCalendar();

		// refresh view
		this.calendarPanel.refreshGrid();

	}

	/**
	 * Create a calendar
	 * @param calendarName the name of the calendar to be created
	 * @throws IOException
	 * @throws Error
	 */
	public void createCalendar(String calendarName) throws IOException, Error {
		this.agenda.createCalendar(calendarName);

		// refresh view
		this.calendarPanel.refreshGrid();
		this.calendarListPanel.refreshPanel();
		this.setNewEventButtonState();
	}

	/**
	 * Create a calendar with an algorithm and a password.
	 * @param calendarName the name of the calendar to be created
	 * @param algorithm algorithm used for encryption / decryption
	 * @param password needed to encrypt (or decrypt later) the calendar
	 * @throws IOException
	 * @throws Error
	 */
	public void createCalendar(String calendarName, String algorithm, String password) throws IOException, Error {
		this.agenda.createCalendar(calendarName, algorithm, password);

		// refresh view
		this.calendarPanel.refreshGrid();
		this.calendarListPanel.refreshPanel();
		this.setNewEventButtonState();
	}

	/**
	 * Set calendar panel to allow refreshs when needed.
	 * @param calendarPanel
	 */
	public void setCalendarPanel(CalendarPanel calendarPanel) {
		this.calendarPanel = calendarPanel;
	}

	/**
	 * Set calendarListPanel to allow refreshs when needed.
	 * @param calendarListPanel
	 */
	public void setCalendarListPanel(CalendarListPanel calendarListPanel) {
		this.calendarListPanel = calendarListPanel;
	}

	/**
	 * Set topBarPanel to allow refreshs when needed.
	 * @param topBarPanel
	 */
	public void setTopBarPanel(TopBarPanel topBarPanel) {
		this.topBarPanel = topBarPanel;
	}

	/**
	 * Refresh the calendar panel to the selected date
	 * @param date to display on the calendar panel
	 * @see fr.uha.ensisa.crypto.ui.CalendarPanel#setSelectedDate(Date)
	 */
	public void goToDate(Date date) {
		this.calendarPanel.setSelectedDate(date);
	}

	/**
	 *
	 * @param date to search event
	 * @return all event of this corresponding date
	 */
	public Collection<Event> searchEventByDate(Date date) {
		return this.agenda.search(date);
	}

	/**
	 *
	 * @return all calendars in data/ folder
	 */
	public Collection<String> getCalendarsNames() {
		return this.agenda.getCalendarNames();
	}

	/**
	 * Load calendar and refresh left panel (calendar selection).
	 * @param calendarName the calendar to load
	 * @param password the password needed if calendar is crypted
	 * @return true if calendar has been successfully loaded, false otherwise.
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @see fr.uha.ensisa.crypto.ui.calendar_selection.CalendarListPanel
	 */
	public boolean loadCalendar(String calendarName, String password) throws ClassNotFoundException, IOException {
		boolean loaded = this.agenda.loadCalendar(calendarName, password);
		this.topBarPanel.activateCreateEventButton();
		this.calendarPanel.refreshGrid();
		this.setNewEventButtonState();
		return loaded;
	}

	/**
	 * Unload calendar and refresh calendar panel.
	 * @param calendarName
	 */
	public void unloadCalendar(String calendarName) {
		this.agenda.unloadCalendar(calendarName);
		this.calendarPanel.refreshGrid();
		this.setNewEventButtonState();
	}

	/**
	 * Delete the calendar.
	 * @param calendarName the calendar to delete
	 * @throws IOException
	 */
	public void deleteCalendar(String calendarName) throws IOException {
		this.agenda.deleteCalendar(calendarName);
		this.calendarListPanel.refreshPanel();
		this.calendarPanel.refreshGrid();
		this.setNewEventButtonState();
	}

	/**
	 * Get all loaded calendars' names.
	 * @return all loaded calendars' names
	 */
	public Collection<String> getLoadedCalendarNames() {
		Collection<Calendar> loadedCalendars = this.agenda.getAllCalendars();
		Collection<String> loadedCalendarsName = new ArrayList<>();
		for (Calendar calendar : loadedCalendars) {
			loadedCalendarsName.add(calendar.getName());
		}
		return loadedCalendarsName;
	}

	/**
	 * Check if at least one calendar is loaded and activate or deactive the 'New Event' button.
	 */
	private void setNewEventButtonState() {
		if (this.agenda.getAllCalendars().size() == 0)
			this.topBarPanel.deactivateCreateEventButton();
		else
			this.topBarPanel.activateCreateEventButton();
	}

	/**
	 *
	 * @param calendarName
	 * @return true if calendar is crypted, false otherwise.
	 * @throws IOException
	 */
	public boolean isCrypted(String calendarName) throws IOException {
		return this.agenda.isCrypted(calendarName);
	}

	/**
	 * Receive and create a new crypted calendar.
	 * @param calendarName the calendar to receive
	 * @param algorithm the encryption algorithm
	 * @param password the password needed for the algorithm
	 * @throws IOException
	 * @throws Error
	 * @throws InvalidKeyException
	 * @throws ClassNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public void receiveCalendar(String calendarName, String algorithm, String password)
			throws IOException, Error, InvalidKeyException, ClassNotFoundException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		this.agenda.receiveCalendar(calendarName, algorithm, password);

		// refresh view
		this.calendarPanel.refreshGrid();
		this.calendarListPanel.refreshPanel();
		this.setNewEventButtonState();
	}

	/**
	 * Receive and create a non crypted calendar.
	 * @param calendarName
	 * @throws IOException
	 * @throws Error
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public void receiveCalendar(String calendarName) throws IOException, Error, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		this.agenda.receiveCalendar(calendarName);

		// refresh view
		this.calendarPanel.refreshGrid();
		this.calendarListPanel.refreshPanel();
		this.setNewEventButtonState();
	}

	/**
	 * Send the selected calendar.
	 * @param calendarName
	 * @param address IP address to send the calendar to.
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 * @throws Error
	 */
	public void sendCalendar(String calendarName, String address)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeySpecException, IOException, Error {
		this.agenda.getCalendar(calendarName).sendCalendar(address);

		// refresh view
		this.calendarPanel.refreshGrid();
		this.calendarListPanel.refreshPanel();
		this.setNewEventButtonState();
	}

}
