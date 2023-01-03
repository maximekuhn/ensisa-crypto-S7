package fr.uha.ensisa.crypto.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.model.Calendar;
import fr.uha.ensisa.crypto.model.Event;
import fr.uha.ensisa.crypto.ui.calendar_selection.CalendarListPanel;
import fr.uha.ensisa.crypto.ui.topbar.TopBarPanel;

public class MainWindowController implements MouseListener, MouseMotionListener, KeyListener {

    private Agenda agenda;
    private CalendarPanel calendarPanel;
    private CalendarListPanel calendarListPanel;
    private TopBarPanel topBarPanel;

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

    public Agenda getAgenda() {
        return this.agenda;
    }

    public void createEvent(String calendar, String event, String description, String location, Date date,
            double duration) throws IOException, Error {
        Event newEvent = new Event(date, (long) duration, event, description, location);

        this.agenda.getCalendar(calendar).getEventTable().addEvent(newEvent);
        this.agenda.getCalendar(calendar).saveCalendar();

        // refresh view
        calendarPanel.refreshGrid();

    }

    public void createCalendar(String calendarName) throws IOException, Error {
        this.agenda.createCalendar(calendarName);

        // refresh view
        this.calendarPanel.refreshGrid();
        this.calendarListPanel.refreshPanel();
        this.setNewEventButtonState();
    }

    public void createCalendar(String calendarName,String algorithm, String password) throws IOException, Error {
        this.agenda.createCalendar(calendarName,algorithm,password);

        // refresh view
        this.calendarPanel.refreshGrid();
        this.calendarListPanel.refreshPanel();
        this.setNewEventButtonState();
    }

    public void setCalendarPanel(CalendarPanel calendarPanel) {
        this.calendarPanel = calendarPanel;
    }

    public void setCalendarListPanel(CalendarListPanel calendarListPanel) {
        this.calendarListPanel = calendarListPanel;
    }

    public void setTopBarPanel(TopBarPanel topBarPanel) {
        this.topBarPanel = topBarPanel;
    }

    public void goToDate(Date date) {
        this.calendarPanel.setSelectedDate(date);
    }

    public Collection<Event> searchEventByDate(Date date) {
        return this.agenda.search(date);
    }

    public Collection<String> getCalendarsNames() {
        return this.agenda.getCalendarNames();
    }

    public void loadCalendar(String calendarName) throws ClassNotFoundException, IOException {
        this.agenda.loadCalendar(calendarName, "");
        this.topBarPanel.activateCreateEventButton();
        this.calendarPanel.refreshGrid();
        this.setNewEventButtonState();
    }

    public void unloadCalendar(String calendarName) {
        this.agenda.unloadCalendar(calendarName);
        this.calendarPanel.refreshGrid();
        this.setNewEventButtonState();
    }

    public void deleteCalendar(String calendarName) throws IOException {
        this.agenda.deleteCalendar(calendarName);
        this.calendarListPanel.refreshPanel();
        this.calendarPanel.refreshGrid();
        this.setNewEventButtonState();
    }

    public Collection<String> getLoadedCalendarNames() {
        Collection<Calendar> loadedCalendars = this.agenda.getAllCalendars();
        Collection<String> loadedCalendarsName = new ArrayList<>();
        for (Calendar calendar : loadedCalendars) {
            loadedCalendarsName.add(calendar.getName());
        }
        return loadedCalendarsName;
    }

    private void setNewEventButtonState() {
        if (this.agenda.getAllCalendars().size() == 0)
            this.topBarPanel.deactivateCreateEventButton();
        else
            this.topBarPanel.activateCreateEventButton();
    }
    
    public Boolean isCrypted(String calendarName) throws IOException {
    	return this.agenda.isCrypted(calendarName);
    }
    
}
