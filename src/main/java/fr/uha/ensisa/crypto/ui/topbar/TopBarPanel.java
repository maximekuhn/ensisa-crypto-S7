package fr.uha.ensisa.crypto.ui.topbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

import fr.uha.ensisa.crypto.model.Event;
import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 * A customized JPanel that contains everything about creating events or calendar.
 */
public class TopBarPanel extends JPanel implements ActionListener {

    private static final String CREATE_EVENT_BUTTON_TEXT = "NEW EVENT";
    private static final String CREATE_CALENDAR_BUTTON_TEXT = "NEW CALENDAR";
    private static final String receive_CALENDAR_BUTTON_TEXT = "RECEIVE CALENDAR";
    private static final String SEARCH_BUTTON_TEXT = "Search";
    private static final String CREATE_EVENT_BUTTON_TOOLTIP = "Click here to create a new event";
    private static final String CREATE_CALENDAR_BUTTON_TOOLTIP = "Click here to receive a new calendar";
    private static final String receive_CALENDAR_BUTTON_TOOLTIP = "Click here to create a new event";
    private static final String SEARCH_BUTTON_TOOLTIP = "Click here to search an event by date";

    private MainWindow mainWindow;
    private MainWindowController controller;
    private JButton createEventButton;
    private JButton createCalendarButton;
    private JButton searchButton;
    private JDatePickerImpl datePicker;
    private TimePicker timePicker;
    private JButton receiveCalendarButton;

    /**
     * Constructor
     * @param mainWindow JFrame that contains everything
     */
    public TopBarPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.controller = this.mainWindow.getController();

        // create buttons
        this.createEventButton = new JButton(CREATE_EVENT_BUTTON_TEXT);
        this.createEventButton.setToolTipText(CREATE_EVENT_BUTTON_TOOLTIP);
        this.createEventButton.setEnabled(false); // as no calendar is selected by default
        this.createCalendarButton = new JButton(CREATE_CALENDAR_BUTTON_TEXT);
        this.createCalendarButton.setToolTipText(CREATE_CALENDAR_BUTTON_TOOLTIP);
        this.receiveCalendarButton = new JButton(receive_CALENDAR_BUTTON_TEXT);
        this.receiveCalendarButton.setToolTipText(receive_CALENDAR_BUTTON_TOOLTIP);
        this.searchButton = new JButton(SEARCH_BUTTON_TEXT);
        this.searchButton.setToolTipText(SEARCH_BUTTON_TOOLTIP);

        // handle click
        this.createEventButton.addActionListener(this);
        this.createCalendarButton.addActionListener(this);
        this.receiveCalendarButton.addActionListener(this);
        this.searchButton.addActionListener(this);

        // date picker
        UtilDateModel dateModel = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        this.datePicker = new JDatePickerImpl(datePanel);

        // time picker
        TimePickerSettings timePickerSettings = new TimePickerSettings();
        timePickerSettings.use24HourClockFormat();
        this.timePicker = new TimePicker(timePickerSettings);

        // add components
        this.add(this.createEventButton);
        this.add(this.createCalendarButton);
        this.add(this.receiveCalendarButton);
        this.add(this.datePicker);
        this.add(this.timePicker);
        this.add(this.searchButton);
    }

    /**
     * Buttons handler
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.createEventButton))
            this.createEventPopup();
        else if (e.getSource().equals(this.createCalendarButton))
            this.createCalendarPopup();
        else if (e.getSource().equals(this.receiveCalendarButton))
            this.receiveCalendarPopup();
        else if (e.getSource().equals(this.searchButton))
            this.searchEventByDate();
    }

    /**
     * Create a popup to create a new event
     * @see fr.uha.ensisa.crypto.ui.topbar.CreateEventPopup
     */
    private void createEventPopup() {
        CreateEventPopup popup = new CreateEventPopup(this.mainWindow);
        popup.setVisible(true);
    }

    /**
     * Create a popup to create a new calendar
     * @see fr.uha.ensisa.crypto.ui.topbar.CreateCalendarPopup
     */
    private void createCalendarPopup() {
        CreateCalendarPopup popup = new CreateCalendarPopup(this.mainWindow);
        popup.setVisible(true);
    }

    /**
     * Create a popup to receive a calendar
     * @see fr.uha.ensisa.crypto.ui.topbar.ReceiveCalendarPopup
     */
    private void receiveCalendarPopup() {
        ReceiveCalendarPopup popup = new ReceiveCalendarPopup(this.mainWindow);
        popup.setVisible(true);
    }

    /**
     * Handle search button click. If no event exist for the selected that, this method display an error message. Otherwise, it set the event panel to selected date in order to show the event.
     * @see fr.uha.ensisa.crypto.ui.CalendarPanel
     * @see fr.uha.ensisa.crypto.ui.MainWindowController
     */
    private void searchEventByDate() {
        Date date = (Date) this.datePicker.getModel().getValue();
        LocalTime time = this.timePicker.getTime();

        if (date == null || time == null)
            // if date is null, display a message
            this.showErrorPopup("Please specify a valid date/time combo.");
        else {
            // get date without time
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(date);
            c.set(java.util.Calendar.HOUR_OF_DAY, 0);
            c.set(java.util.Calendar.MINUTE, 0);
            c.set(java.util.Calendar.SECOND, 0);
            c.set(java.util.Calendar.MILLISECOND, 0);
            Date dateWithoutTime = c.getTime();

            // add time to date
            int seconds = time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond();
            Date searchDate = new Date(dateWithoutTime.getTime() + seconds * 1000);
            Collection<Event> events = this.controller.searchEventByDate(searchDate);
            if (events.size() == 0) {
                // if no event found, display a message
                this.showErrorPopup("No event found for given date.");
            } else
                this.controller.goToDate(searchDate);
        }
    }

    /**
     * A simple popup that displays a customizable error message.
     * @param errorMessage the error message to show.
     */
    private void showErrorPopup(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }

    /**
     * Activate create event button (a calendar needs to be loaded).
     */
    public void activateCreateEventButton() {
        this.createEventButton.setEnabled(true);
    }

    /**
     * Deactive create event button if no calendar is loaded.
     */
    public void deactivateCreateEventButton() {
        this.createEventButton.setEnabled(false);
    }
}
