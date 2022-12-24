package fr.uha.ensisa.crypto.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class CreateEventPopup extends JDialog implements ActionListener {

    private static final String POPUP_TITLE = "New Event";
    private static final String CREATE_BUTTON_TEXT = "create";
    private static final String CANCEL_BUTTON_TEXT = "cancel";

    private static final int POPUP_WIDTH = 400;
    private static final int POPUP_HEIGHT = 180;
    private static final int EVENT_TEXT_FIELD_LENGTH = 10;
    private static final int DESCRIPTION_TEXT_FIELD_LENGTH = EVENT_TEXT_FIELD_LENGTH;
    private static final int LOCATION_TEXT_FIELD_LENGTH = EVENT_TEXT_FIELD_LENGTH;
    private static final double DURATION_MINIMUM = 0.5;
    private static final double DURATION_MAXIMUM = 24.0;
    private static final double DURATION_STEP = 0.5;
    private static final double DURATION_START = 2.0;

    private JPanel mainPanel;
    private JTextField eventTextField;
    private JTextField descriptionTextField;
    private JTextField locationTextField;
    private JButton createButton;
    private JButton cancelButton;
    private JDatePickerImpl datePicker;
    private JSpinner durationSpinner; // hours

    public CreateEventPopup(MainWindow mainWindow) {
        super(mainWindow, POPUP_TITLE);
        this.setModal(true);
        this.setLocationRelativeTo(mainWindow); // center popup

        // create main panel to put everything in
        this.mainPanel = new JPanel();
        this.mainPanel.setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        this.setPreferredSize(this.mainPanel.getPreferredSize());
        this.setMinimumSize(this.mainPanel.getPreferredSize());
        this.setMaximumSize(this.mainPanel.getPreferredSize());

        // text fields (event, description & location)
        this.eventTextField = new JTextField(EVENT_TEXT_FIELD_LENGTH);
        this.descriptionTextField = new JTextField(DESCRIPTION_TEXT_FIELD_LENGTH);
        this.locationTextField = new JTextField(LOCATION_TEXT_FIELD_LENGTH);

        // date
        UtilDateModel dateModel = new UtilDateModel();
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

        // buttons
        this.createButton = new JButton(CREATE_BUTTON_TEXT);
        this.cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        this.createButton.addActionListener(this);
        this.cancelButton.addActionListener(this);

        // add all components to popup
        this.mainPanel.add(this.eventTextField);
        this.mainPanel.add(this.descriptionTextField);
        this.mainPanel.add(this.locationTextField);
        this.mainPanel.add(this.createButton);
        this.mainPanel.add(this.cancelButton);
        this.mainPanel.add(this.datePicker);
        this.mainPanel.add(this.durationSpinner);
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
        double duration = (double) this.durationSpinner.getValue();
        System.out.println("event : " + event + "\ndescription : " + description + "\nlocation : " + location + "\ndate : " + date.toString() + "\nduration : " + duration);
    }
}
