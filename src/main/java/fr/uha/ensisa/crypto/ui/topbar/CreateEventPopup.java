package fr.uha.ensisa.crypto.ui.topbar;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.model.Calendar;
import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class CreateEventPopup extends JDialog implements ActionListener {

    private static final String POPUP_TITLE = "New Event";
    private static final String CREATE_BUTTON_TEXT = "create";
    private static final String CANCEL_BUTTON_TEXT = "cancel";
    private static final String EVENT_LABEL_TEXT = "TITLE";
    private static final String DESCRIPTION_LABEL_TEXT = "DESCRIPTION";
    private static final String LOCATION_LABEL_TEXT = "LOCATION";
    private static final String DATE_LABEL_TEXT = "DATE";
    private static final String DURATION_LABEL_TEXT = "DURATION";
    private static final String CALENDAR_LABEL_TEXT = "CALENDAR";

    private static final int POPUP_WIDTH = 400;
    private static final int POPUP_HEIGHT = 180;
    private static final int EVENT_TEXT_FIELD_LENGTH = 10;
    private static final int DESCRIPTION_TEXT_FIELD_LENGTH = EVENT_TEXT_FIELD_LENGTH;
    private static final int LOCATION_TEXT_FIELD_LENGTH = EVENT_TEXT_FIELD_LENGTH;
    private static final double DURATION_MINIMUM = 1;
    private static final double DURATION_MAXIMUM = 24.0;
    private static final double DURATION_STEP = 1;
    private static final double DURATION_START = 2.0;

    private MainWindow mainWindow;
    private JPanel mainPanel;
    private JLabel eventLabel;
    private JLabel descriptionLabel;
    private JLabel locationLabel;
    private JLabel dateLabel;
    private JLabel durationLabel;
    private JLabel calendarLabel;
    private JTextField eventTextField;
    private JTextField descriptionTextField;
    private JTextField locationTextField;
    private JButton createButton;
    private JButton cancelButton;
    private JDatePickerImpl datePicker;
    private JSpinner durationSpinner; // hours
    private JComboBox calendarsList;

    public CreateEventPopup(MainWindow mainWindow) {
        super(mainWindow, POPUP_TITLE);
        this.mainWindow = mainWindow;
        this.setModal(true);
        this.setLocationRelativeTo(mainWindow); // center popup

        // create main panel to put everything in
        this.mainPanel = new JPanel();
        this.mainPanel.setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        this.setPreferredSize(this.mainPanel.getPreferredSize());
        this.setMinimumSize(this.mainPanel.getPreferredSize());
        this.setMaximumSize(this.mainPanel.getPreferredSize());
        GridLayout gridLayout = new GridLayout(7, 2);
        this.mainPanel.setLayout(gridLayout);

        // text fields (event, description & location)
        this.eventTextField = new JTextField(EVENT_TEXT_FIELD_LENGTH);
        this.descriptionTextField = new JTextField(DESCRIPTION_TEXT_FIELD_LENGTH);
        this.locationTextField = new JTextField(LOCATION_TEXT_FIELD_LENGTH);

        // date
        UtilDateModel dateModel = new UtilDateModel();
        LocalDate now = LocalDate.now();
        dateModel.setDate(
            now.getYear(),
            now.getMonthValue(),
            now.getDayOfMonth()
        );
        dateModel.setSelected(true);
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        this.datePicker = new JDatePickerImpl(datePanel);

        // duration
        SpinnerNumberModel durationModel = new SpinnerNumberModel(
            DURATION_START,
            DURATION_MINIMUM,
            DURATION_MAXIMUM,
            DURATION_STEP
        );
        this.durationSpinner = new JSpinner(durationModel);

        // calendars
        Agenda agenda = this.mainWindow.getController().getAgenda();
        Collection<Calendar> allCalendars = agenda.getAllCalendars();
        Collection<String> calendars = new ArrayList<>();
        for(Calendar calendar : allCalendars)
            calendars.add(calendar.getName());
        this.calendarsList = new JComboBox<>(calendars.toArray());

        // buttons
        this.createButton = new JButton(CREATE_BUTTON_TEXT);
        this.cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        this.createButton.addActionListener(this);
        this.cancelButton.addActionListener(this);

        // labels
        this.eventLabel = new JLabel(EVENT_LABEL_TEXT);
        this.descriptionLabel = new JLabel(DESCRIPTION_LABEL_TEXT);
        this.locationLabel = new JLabel(LOCATION_LABEL_TEXT);
        this.dateLabel = new JLabel(DATE_LABEL_TEXT);
        this.durationLabel = new JLabel(DURATION_LABEL_TEXT);
        this.calendarLabel = new JLabel(CALENDAR_LABEL_TEXT);

        // add all components to popup
        this.mainPanel.add(this.eventLabel);
        this.mainPanel.add(this.eventTextField);

        this.mainPanel.add(this.descriptionLabel);
        this.mainPanel.add(this.descriptionTextField);

        this.mainPanel.add(this.locationLabel);
        this.mainPanel.add(this.locationTextField);

        this.mainPanel.add(this.dateLabel);
        this.mainPanel.add(this.datePicker);

        this.mainPanel.add(this.durationLabel);
        this.mainPanel.add(this.durationSpinner);

        this.mainPanel.add(this.calendarLabel);
        this.mainPanel.add(this.calendarsList);

        this.mainPanel.add(this.createButton);
        this.mainPanel.add(this.cancelButton);
        
        this.add(this.mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(this.cancelButton)) this.closePopup();
        else if(e.getSource().equals(this.createButton)) this.createEvent();
    }

    private void closePopup() {
        this.dispose();
    }

    private void createEvent() {
        String event = this.eventTextField.getText();
        String description = this.descriptionTextField.getText();
        String location = this.locationTextField.getText();
        Date date = (Date) this.datePicker.getModel().getValue();
        date = Date.from(date.toInstant().truncatedTo( ChronoUnit.DAYS ));
        double duration = (double) this.durationSpinner.getValue();
        String calendar = (String) this.calendarsList.getSelectedItem();

        // TODO handle errors
        // TODO show error when any field is empty

        MainWindowController controller = this.mainWindow.getController();
        controller.createEvent(calendar, event, description, location, date, duration);

        // TODO remove this print
        System.out.println("event : " + event + "\ndescription : " + description + "\nlocation : " + location + "\ndate : " + date.toString() + "\nduration : " + duration + "\ncalendar : " + calendar);

        this.closePopup();
    }
}
