package fr.uha.ensisa.crypto.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Date;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.model.Event;

public class MainWindowController implements MouseListener, MouseMotionListener, KeyListener {

    private Agenda agenda;
    private CalendarPanel calendarPanel;

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

    public void createEvent(String calendar, String event, String description, String location, Date date, double duration) {
        Event newEvent = new Event(date, (long) duration, event, description, location);

        // TODO check if calendar exist
        this.agenda.getCalendar(calendar).getEventTable().addEvent(newEvent);
        try {
            this.agenda.getCalendar(calendar).saveCalendar();
        } catch (IOException | Error e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // TODO refresh all panels (or frame ?) showing events
        calendarPanel.refreshGrid();

    }

    public void createCalendar(String calendarName) {
        try {
            this.agenda.createCalendar(calendarName);
        } catch (IOException | Error e) {
            e.printStackTrace();
        }

        // TODO refresh all panels (or frame ?) showing calendars
        calendarPanel.refreshGrid();
    }

    public void setCalendarPanel(CalendarPanel calendarPanel) {
        this.calendarPanel = calendarPanel;
    }
}
