package fr.uha.ensisa.crypto.ui.topbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;

public class TopBarPanel extends JPanel implements ActionListener {

    private static final String CREATE_EVENT_BUTTON_TEXT = "NEW EVENT";
    private static final String CREATE_CALENDAR_BUTTON_TEXT = "NEW CALENDAR";
    private static final String CREATE_EVENT_BUTTON_TOOLTIP = "Click here to create a new event";
    private static final String CREATE_CALENDAR_BUTTON_TOOLTIP = "Click here to create a new calendar";

    private static final int SEARCH_BAR_LENGTH = 10;

    private MainWindow mainWindow;
    private MainWindowController controller;
    private JButton createEventButton;
    private JButton createCalendarButton;

    public TopBarPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.controller = this.mainWindow.getController();

        // create button
        this.createEventButton = new JButton(CREATE_EVENT_BUTTON_TEXT);
        this.createEventButton.setToolTipText(CREATE_EVENT_BUTTON_TOOLTIP);
        this.createCalendarButton = new JButton(CREATE_CALENDAR_BUTTON_TEXT);
        this.createCalendarButton.setToolTipText(CREATE_CALENDAR_BUTTON_TOOLTIP);

        // handle click
        this.createEventButton.addActionListener(this);
        this.createCalendarButton.addActionListener(this);

        // add button(s)
        this.add(this.createEventButton);
        this.add(this.createCalendarButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.createEventButton))
            this.createEventPopup();
        else if (e.getSource().equals(this.createCalendarButton))
            this.createCalendarPopup();
    }

    private void createEventPopup() {
        CreateEventPopup popup = new CreateEventPopup(this.mainWindow);
        popup.setVisible(true);
    }

    private void createCalendarPopup() {
        CreateCalendarPopup popup = new CreateCalendarPopup(this.mainWindow);
        popup.setVisible(true);
    }
}
