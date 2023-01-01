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
    

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.cancelButton))
            this.closePopup();
        else if (e.getSource().equals(this.submitButton))
			this.loadCalendar(calendarName);
			
	}


	private void loadCalendar(String calendarName) {
		try {
			this.controller.loadCalendar(calendarName);
			this.closePopup();
		} catch (ClassNotFoundException | IOException e) {
            this.showErrorPopup("Calendar " + calendarName + " is unavailable.");
			e.printStackTrace();
		}
	}


	private void closePopup() {
		 this.dispose();
	}
	
	private void showErrorPopup(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }

}
