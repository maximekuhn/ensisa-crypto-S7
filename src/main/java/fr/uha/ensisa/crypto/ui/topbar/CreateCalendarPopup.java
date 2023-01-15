package fr.uha.ensisa.crypto.ui.topbar;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;

public class CreateCalendarPopup extends JDialog implements ActionListener {

    private static final String POPUP_TITLE = "New Calendar";
    private static final String CALENDAR_LABEL_TEXT = "CALENDAR";
    private static final String PASS_LABEL_TEXT = "PASSWORD";
    private static final String PASS_CONFIRM_LABEL_TEXT = "CONFIRM PASSWORD";
    private static final String ALGO_LABEL_TEXT = "ALGORITHM";
    private static final String CREATE_BUTTON_TEXT = "create";
    private static final String CANCEL_BUTTON_TEXT = "cancel";

    private static final int POPUP_WIDTH = 700;
    private static final int POPUP_HEIGHT = 100;
    private static final int CALENDAR_TEXT_FIELD_LENGTH = 10;
    private static final int PASS_TEXT_FIELD_LENGTH = 10;
    private static final int PASS_CONFIRM_TEXT_FIELD_LENGTH = 10;

    private MainWindow mainWindow;
    private JPanel mainPanel;
    private GridLayout layout;

    private JLabel calendarLabel;
    private JTextField calendarTextField;
    private JLabel passLabel;
    private JTextField passTextField;
    private JLabel passConfirmLabel;
    private JTextField passConfirmTextField;
    private JLabel algoLabel;
    private JComboBox<String> algoListField;
    private JButton createButton;
    private JButton cancelButton;

    public CreateCalendarPopup(MainWindow mainWindow) {
        super(mainWindow, POPUP_TITLE);
        this.mainWindow = mainWindow;
        this.setModal(true);

        // create main panel to store everything in
        this.mainPanel = new JPanel();
        this.layout = new GridLayout(5, 2);
        this.mainPanel.setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        this.setPreferredSize(this.mainPanel.getPreferredSize());
        this.setMinimumSize(this.mainPanel.getPreferredSize());
        this.setMaximumSize(this.mainPanel.getPreferredSize());
        this.setLocationRelativeTo(mainWindow); // center popup
        this.mainPanel.setLayout(layout);

        // label
        this.calendarLabel = new JLabel(CALENDAR_LABEL_TEXT);
        this.passLabel = new JLabel(PASS_LABEL_TEXT);
        this.passConfirmLabel = new JLabel(PASS_CONFIRM_LABEL_TEXT);
        this.algoLabel = new JLabel(ALGO_LABEL_TEXT);

        // textfield
        this.calendarTextField = new JTextField(CALENDAR_TEXT_FIELD_LENGTH);
        this.passTextField = new JPasswordField(PASS_TEXT_FIELD_LENGTH);
        this.passConfirmTextField = new JPasswordField(PASS_CONFIRM_TEXT_FIELD_LENGTH);

        // buttons
        this.createButton = new JButton(CREATE_BUTTON_TEXT);
        this.cancelButton = new JButton(CANCEL_BUTTON_TEXT);
        this.createButton.addActionListener(this);
        this.cancelButton.addActionListener(this);

        // list
        // TODO algorithms list enum
        this.algoListField = new JComboBox<String>(new String[] { "NONE", "AES", "RC5" });

        // add components
        this.mainPanel.add(this.calendarLabel);
        this.mainPanel.add(this.calendarTextField);
        this.mainPanel.add(this.passLabel);
        this.mainPanel.add(this.passTextField);
        this.mainPanel.add(this.passConfirmLabel);
        this.mainPanel.add(this.passConfirmTextField);
        this.mainPanel.add(this.algoLabel);
        this.mainPanel.add(this.algoListField);

        this.mainPanel.add(this.createButton);
        this.mainPanel.add(this.cancelButton);

        this.add(this.mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.createButton))
            this.createCalendar();
        if (e.getSource().equals(this.cancelButton))
            this.closePopup();
    }

    private void closePopup() {
        this.dispose();
    }

    private boolean containsUpper(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i)))
                return true;
        }
        return false;
    }

    private void createCalendar() {
        String calendarName = this.calendarTextField.getText();
        MainWindowController controller = this.mainWindow.getController();

        // check calendar name
        if (calendarName.isBlank()) {
            this.showErrorPopup("Please specify a name for calender.");
            return;
        }
        if (controller.getCalendarsNames().contains(calendarName)) {
            this.showErrorPopup("Name is already used.");
            return;
        }

        // check algorithm
        if (this.algoListField.getSelectedIndex() > 0) { // encryption algorithm
            String password = this.passTextField.getText();
            String passwordConfirm = this.passConfirmTextField.getText();
            if (password.isBlank()) {
                this.showErrorPopup("Please specify a password");
                return;
            }
            if (!password.equals(passwordConfirm)) {
                this.showErrorPopup("Password and confirm must be equal.");
                return;
            }
            if (password.length() < 8) {
                this.showErrorPopup("Password must have at least 8 characters.");
                return;
            }
            if (!this.containsUpper(password)) {
                this.showErrorPopup("Password must contains at least one upper letter.");
                return;
            }

            String algorithm = this.algoListField.getSelectedItem().toString();
            try {
                controller.createCalendar(calendarName, algorithm, password);
                this.dispose();
            } catch (Exception e) {
                this.showErrorPopup("Creation failed !");
                e.printStackTrace();
            }

        } else { // no algorithm
            try {
                controller.createCalendar(calendarName);
                this.dispose();
            } catch (Exception e) {
                this.showErrorPopup("Creation failed !");
                e.printStackTrace();
            }
        }
    }

    private void showErrorPopup(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }
}
