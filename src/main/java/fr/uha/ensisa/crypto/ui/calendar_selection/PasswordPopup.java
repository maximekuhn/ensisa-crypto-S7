package fr.uha.ensisa.crypto.ui.calendar_selection;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;

/**
 * This class is used to display a popup that requires a password, to load a calendar.
 */
public class PasswordPopup extends JDialog implements ActionListener {

    private static final String POPUP_TITLE = "Password";
    private static final int POPUP_WIDTH = 300;
    private static final int POPUP_HEIGHT = 100;
    private static final String PASS_LABEL_TEXT = "Password :";
    private static final int PASS_TEXT_FIELD_LENGTH = 20;
    private static final String CREATE_BUTTON_TEXT = "submit";
    private static final String CANCEL_BUTTON_TEXT = "cancel";

    private MainWindow mainWindow;
    private MainWindowController controller;
    private String calendarName;

    private JPanel mainPanel;
    private JLabel passLabel;
    private JTextField passTextField;
    private JButton submitButton;
    private JButton cancelButton;

    /**
     * Constructor
     * @param mainWindow JFrame that contains everything
     * @param calendarName the calendar to load using the password.
     */
    public PasswordPopup(MainWindow mainWindow, String calendarName) {
        super(mainWindow, POPUP_TITLE);
        this.mainWindow = mainWindow;
        this.controller = this.mainWindow.getController();
        this.setModal(true);
        this.calendarName = calendarName;

        // create main panel
        this.mainPanel = new JPanel();
        this.mainPanel.setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        this.setPreferredSize(this.mainPanel.getPreferredSize());
        this.setMinimumSize(this.mainPanel.getPreferredSize());
        this.setMaximumSize(this.mainPanel.getPreferredSize());
        this.setLocationRelativeTo(mainWindow);

        // label and textfield
        this.passLabel = new JLabel(PASS_LABEL_TEXT);
        this.passTextField = new JPasswordField(PASS_TEXT_FIELD_LENGTH);

        // buttons
        this.submitButton = new JButton(CREATE_BUTTON_TEXT);
        this.cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        this.submitButton.addActionListener(this);
        this.cancelButton.addActionListener(this);

        // add components
        this.mainPanel.add(this.passLabel);
        this.mainPanel.add(this.passTextField);
        this.mainPanel.add(this.submitButton);
        this.mainPanel.add(this.cancelButton);

        this.add(this.mainPanel);

    }

    /**
     * Buttons handler.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.cancelButton))
            this.closePopup();
        else if (e.getSource().equals(this.submitButton))
            this.loadCalendar(calendarName);
    }

    /**
     * Load the calendar after the password is typed. Display an error message if password is incorrect or calendar is unavailable.
     * @param calendarName the calendar to load.
     */
    private void loadCalendar(String calendarName) {
        try {
            String password = this.passTextField.getText();
            boolean loaded = this.controller.loadCalendar(calendarName, password);
            if (loaded == true)
                this.closePopup();
            else {
                this.passTextField.setText("");
                this.showErrorPopup("Invalid Password");
            }
        } catch (ClassNotFoundException | IOException e) {
            this.showErrorPopup("Calendar " + calendarName + " is unavailable.");
            e.printStackTrace();
        }
    }

    /**
     * Close this popup.
     */
    private void closePopup() {
        this.dispose();
    }

    /**
     * A simple popup that displays a customizable error message.
     * @param errorMessage the error message to show.
     */
    private void showErrorPopup(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }

}
