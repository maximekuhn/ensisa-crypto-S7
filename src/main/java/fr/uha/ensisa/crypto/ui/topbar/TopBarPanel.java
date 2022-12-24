package fr.uha.ensisa.crypto.ui.topbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;

public class TopBarPanel extends JPanel implements ActionListener {

    private static final String CREATE_EVENT_BUTTON_TEXT = "NEW EVENT";
    private static final String CREATE_EVENT_BUTTON_TOOLTIP = "Click here to create a new event";

    private MainWindow mainWindow;
    private MainWindowController controller;
    private JButton createEventButton;

    public TopBarPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.controller = this.mainWindow.getController();

        // create button
        this.createEventButton = new JButton(CREATE_EVENT_BUTTON_TEXT);
        this.createEventButton.setToolTipText(CREATE_EVENT_BUTTON_TOOLTIP);

        // handle click
        this.createEventButton.addActionListener(this);

        // add button(s)
        this.add(this.createEventButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.createEventButton))
            this.createEventPopup();
    }

    private void createEventPopup() {
        CreateEventPopup popup = new CreateEventPopup(this.mainWindow);
        popup.setVisible(true);
    }
}
