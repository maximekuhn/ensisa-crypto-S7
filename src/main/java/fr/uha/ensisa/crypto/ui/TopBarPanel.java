package fr.uha.ensisa.crypto.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TopBarPanel extends JPanel implements ActionListener {
    
    private static final String CREATE_BUTTON_TEXT = "NEW EVENT";
    private static final String CREATE_BUTTON_TOOLTIP = "Click here to create a new event";
    private static final String POPUP_CREATE_EVENT_TEXT = "Create a new event";

    private MainWindow mainWindow;
    private MainWindowController controller;
    private JButton createButton;


    public TopBarPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.controller = this.mainWindow.getController();

        // create button
        this.createButton = new JButton(CREATE_BUTTON_TEXT);
        this.createButton.setToolTipText(CREATE_BUTTON_TOOLTIP);

        // handle click
        this.createButton.addActionListener(this);

        // add button(s)
        this.add(this.createButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(this.createButton)) this.createEventPopup();
    }

    private void createEventPopup() {
        CreateEventPopup popup = new CreateEventPopup();
        popup.setVisible(true);
    }
}
