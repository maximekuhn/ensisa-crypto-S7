package fr.uha.ensisa.crypto.ui.topbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;

public class CreateCalendarPopup extends JDialog implements ActionListener {

    private static final String POPUP_TITLE = "New Calendar";
    private static final String CALENDAR_LABEL_TEXT = "CALENDAR";
    private static final String CREATE_BUTTON_TEXT = "create";

    private static final int POPUP_WIDTH = 300;
    private static final int POPUP_HEIGHT = 80;
    private static final int CALENDAR_TEXT_FIELD_LENGTH = 10;

    private MainWindow mainWindow;
    private JPanel mainPanel;
    private JLabel calendarLabel;
    private JTextField calendarTextField;
    private JButton createButton;

    public CreateCalendarPopup(MainWindow mainWindow) {
        super(mainWindow, POPUP_TITLE);
        this.mainWindow = mainWindow;
        this.setModal(true);
        this.setLocationRelativeTo(mainWindow); // center popup

        // create main panel to store everything in
        this.mainPanel = new JPanel();
        this.mainPanel.setPreferredSize(new Dimension(POPUP_WIDTH, POPUP_HEIGHT));
        this.setPreferredSize(this.mainPanel.getPreferredSize());
        this.setMinimumSize(this.mainPanel.getPreferredSize());
        this.setMaximumSize(this.mainPanel.getPreferredSize());

        // label
        this.calendarLabel = new JLabel(CALENDAR_LABEL_TEXT);

        // textfield
        this.calendarTextField = new JTextField(CALENDAR_TEXT_FIELD_LENGTH);

        // button
        this.createButton = new JButton(CREATE_BUTTON_TEXT);
        this.createButton.addActionListener(this);

        // add components
        this.mainPanel.add(this.calendarLabel);
        this.mainPanel.add(this.calendarTextField);

        this.mainPanel.add(this.createButton);

        this.add(this.mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.createButton))
            this.createCalendar();
    }

    private void createCalendar() {
        String calendarName = this.calendarTextField.getText();

        if (calendarName.isEmpty()) {
            this.showErrorPopup("Please specify a name for the calendar.");
        } else {
            try {
                MainWindowController controller = this.mainWindow.getController();
                controller.createCalendar(calendarName);
                this.dispose();
            } catch (IOException | Error e) {
                this.showErrorPopup("Creation failed !");
                e.printStackTrace();
            }
        }
    }

    private void showErrorPopup(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }
}
