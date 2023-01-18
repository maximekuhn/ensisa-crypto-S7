package fr.uha.ensisa.crypto.ui.calendar_selection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;


/**
 * This class is used to display all available calendar. User can load them, delete them or send them.
 */
public class CalendarListPanel extends JScrollPane implements ActionListener {

    private static final String DELETE_BUTTON_ICON_PATH = "assets/delete.png";
    private static final String SEND_BUTTON_ICON_PATH = "assets/send.png";

    private MainWindow mainWindow;
    private MainWindowController controller;
    private JPanel mainPanel;
    private List<JCheckBox> calendars;
    private List<JButton> sendButtons;
    private List<JButton> deleteButtons;

    /**
     * Constructor of the left panel.
     * @param mainWindow JFrame that display everything.
     */
    public CalendarListPanel(MainWindow mainWindow) {
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.mainWindow = mainWindow;
        this.controller = this.mainWindow.getController();

        this.resetMainPanel();

        this.calendars = new ArrayList<>();
        this.sendButtons = new ArrayList<>();
        this.deleteButtons = new ArrayList<>();
        this.createCheckBoxes();
    }

    /**
     * Refresh everything on the panel, including all checkboxes state, calendar names and buttons.
     */
    public void refreshPanel() {
        this.calendars.clear();
        this.deleteButtons.clear();
        this.sendButtons.clear();

        this.resetMainPanel();
        this.createCheckBoxes();
    }

    /**
     * Used to reset the main panel that contains everything.
     */
    private void resetMainPanel() {
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
        this.mainPanel.add(Box.createVerticalGlue());
        this.setColumnHeaderView(this.mainPanel);
        this.setViewportView(this.mainPanel);
        this.getViewport().getView().setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(this.getWidth(), 200));
    }

    /**
     * Create all checkboxes with the calendar name and delete/send buttons along them.
     */
    private void createCheckBoxes() {
        Collection<String> calendarsNamesList = this.controller.getCalendarsNames();
        if (calendarsNamesList.size() == 0)
            return;
        for (String calendarName : calendarsNamesList) {
            JPanel panel = new JPanel();
            panel.setBackground(Color.DARK_GRAY);
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JCheckBox calendarCheckBox = new JCheckBox(calendarName);
            calendarCheckBox.addActionListener(this);
            calendarCheckBox.setForeground(new Color(255, 255, 255));
            calendarCheckBox.setOpaque(false);
            this.calendars.add(calendarCheckBox);

            // check by default
            calendarCheckBox.setSelected(true);

            Icon deleteIcon = new ImageIcon(DELETE_BUTTON_ICON_PATH);
            JButton deleteButton = new JButton(deleteIcon);
            deleteButton.addActionListener(this);
            deleteButton.setOpaque(false);
            deleteButton.setContentAreaFilled(false);
            deleteButton.setBorderPainted(false);
            this.deleteButtons.add(deleteButton);

            Icon sendIcon = new ImageIcon(SEND_BUTTON_ICON_PATH);
            JButton sendButton = new JButton(sendIcon);
            sendButton.addActionListener(this);
            sendButton.setOpaque(false);
            sendButton.setContentAreaFilled(false);
            sendButton.setBorderPainted(false);
            this.sendButtons.add(sendButton);

            // add to panel
            panel.add(calendarCheckBox);
            panel.add(deleteButton);
            panel.add(sendButton);

            this.mainPanel.add(panel, 0);
        }

        // uncheck unloaded calendars
        Collection<String> loadedCalendarsName = this.controller.getLoadedCalendarNames();
        for (JCheckBox calendarCheckBox : this.calendars) {
            if (!loadedCalendarsName.contains(calendarCheckBox.getText()))
                this.unselectUnloadedCalendar(calendarCheckBox.getText());
        }
    }

    /**
     * Handle buttons clicks.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // calendars checkboxes
        JCheckBox activator = null;
        for (JCheckBox checkBox : this.calendars) {
            if (e.getSource().equals(checkBox))
                activator = checkBox;
        }
        if (activator != null) {
            String calendarName = activator.getText();
            if (activator.isSelected()) {
                this.loadCalendar(calendarName);
                this.refreshPanel();
            } else
                this.unloadCalendar(calendarName);
        }

        // delete buttons
        int index = -1;
        for (int i = 0; i < this.deleteButtons.size(); i++) {
            if (e.getSource().equals(this.deleteButtons.get(i)))
                index = i;
        }
        if (index != -1) {
            String calendarNameToDelete = this.calendars.get(index).getText();
            this.deleteCalendar(calendarNameToDelete);
        }

        // send buttons
        int send_index = -1;
        for (int i = 0; i < this.sendButtons.size(); i++) {
            if (e.getSource().equals(this.sendButtons.get(i)))
                send_index = i;
        }
        if (send_index != -1) {
            String calendarNameToSend = this.calendars.get(send_index).getText();
            this.sendCalendar(calendarNameToSend);
        }
    }

    /**
     * Send button handler.
     * @param calendarName the calendar to send.
     */
    private void sendCalendar(String calendarName) {
        try {
        	Agenda.getInstance().getCalendar(calendarName);
            this.sendPopup(calendarName);
        }
        catch (Error e) {
            this.showErrorPopup("Calendar is not loaded");
        }
    }

    /**
     * Checkbox handler.
     * @param calendarName the calendar to load.
     */
    private void loadCalendar(String calendarName) {
        try {
            if (this.controller.isCrypted(calendarName))
                this.passwordPopup(calendarName);
            else
                this.controller.loadCalendar(calendarName, "");
        } catch (ClassNotFoundException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error : unable to load calendar '" + calendarName + "'.");
            e.printStackTrace();
        }
    }

    /**
     * Checkbox handler.
     * @param calendarName the calendar to unload
     */
    private void unloadCalendar(String calendarName) {
        this.controller.unloadCalendar(calendarName);
    }

    /**
     * Delete button handler.
     * @param calendarName the calendar to delete
     */
    private void deleteCalendar(String calendarName) {
        try {
            this.controller.deleteCalendar(calendarName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error : unable to delete calendar '" + calendarName + "'.");
            e.printStackTrace();
        }
    }

    /**
     * Uncheck the checkbox when a calendar isn't loaded.
     * @param calendarName the calendar that isn't loaded.
     */
    private void unselectUnloadedCalendar(String calendarName) {
        for (JCheckBox calendarCheckBox : this.calendars) {
            if (calendarCheckBox.getText().equals(calendarName)) {
                calendarCheckBox.setSelected(false);
            }
        }
    }

    /**
     * Create a password popup when required.
     * @param calendarName the calendar to load with the password popup.
     * @see fr.uha.ensisa.crypto.ui.calendar_selection.PasswordPopup
     */
    private void passwordPopup(String calendarName) {
        PasswordPopup popup = new PasswordPopup(this.mainWindow, calendarName);
        popup.setVisible(true);
    }

    /**
     * Create a popup to send a calendar.
     * @param calendarName the calendar to send.
     * @see fr.uha.ensisa.crypto.ui.calendar_selection.SendPopup
     */
    private void sendPopup(String calendarName) {
        SendPopup popup = new SendPopup(this.mainWindow, calendarName);
        popup.setVisible(true);
    }

    /**
     * A simple popup that displays a customizable error message.
     * @param errorMessage the error message to show.
     */
    private void showErrorPopup(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }
}
