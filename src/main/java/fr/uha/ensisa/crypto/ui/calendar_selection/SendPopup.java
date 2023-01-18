package fr.uha.ensisa.crypto.ui.calendar_selection;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;

/**
 * Create a popup to send a Calendar.
 */
public class SendPopup extends JDialog implements ActionListener {

    private static final String POPUP_TITLE = "SEND CALENDAR";
    private static final int POPUP_WIDTH = 300;
    private static final int POPUP_HEIGHT = 100;
    private static final String ADDRESS_LABEL_TEXT = "Address :";
    private static final int ADDRESS_TEXT_FIELD_LENGTH = 20;
    private static final String CREATE_BUTTON_TEXT = "send";
    private static final String CANCEL_BUTTON_TEXT = "cancel";

    private MainWindow mainWindow;
    private MainWindowController controller;
    private String calendarName;

    private JPanel mainPanel;
    private JLabel addressLabel;
    private JTextField addressTextField;
    private JButton sendButton;
    private JButton cancelButton;

    /**
     * Constructor
     * @param mainWindow JFrame that contains everything
     * @param calendarName the calendar to send
     */
    public SendPopup(MainWindow mainWindow, String calendarName) {
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
        this.addressLabel = new JLabel(ADDRESS_LABEL_TEXT);
        this.addressTextField = new JTextField(ADDRESS_TEXT_FIELD_LENGTH);

        // buttons
        this.sendButton = new JButton(CREATE_BUTTON_TEXT);
        this.cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        this.sendButton.addActionListener(this);
        this.cancelButton.addActionListener(this);

        // add components
        this.mainPanel.add(this.addressLabel);
        this.mainPanel.add(this.addressTextField);
        this.mainPanel.add(this.sendButton);
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
        else if (e.getSource().equals(this.sendButton))
            this.sendCalendar(this.calendarName);
    }

    /**
     * Send the select calendar. Display an error message if something went wrong.
     * @param calendarName the calendar to send
     */
    private void sendCalendar(String calendarName) {
        try {
            String address = this.addressTextField.getText();
            this.controller.sendCalendar(calendarName, address);
            this.closePopup();
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | Error e) {
            this.showErrorPopup("Error : unable to load calendar '" + calendarName + "'.");
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
