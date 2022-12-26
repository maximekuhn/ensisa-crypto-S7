package fr.uha.ensisa.crypto.ui.calendar_selection;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;

public class CalendarListPanel extends JPanel implements ActionListener {

    private MainWindow mainWindow;
    private MainWindowController controller;
    private List<JCheckBox> calendars;

    public CalendarListPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.controller = this.mainWindow.getController();

        this.calendars = new ArrayList<>();
        this.createCheckBoxes();
    }

    public void refreshPanel() {
        for(JCheckBox checkBox : this.calendars)
            this.remove(checkBox);
        this.createCheckBoxes();
        this.revalidate();
    }

    private void createCheckBoxes() {
        Collection<String> calendarsNamesList = this.controller.getCalendarsNames();
        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(layout);
        for(String calendarName : calendarsNamesList) {
            JCheckBox calendarCheckBox = new JCheckBox(calendarName);
            calendarCheckBox.addActionListener(this);
            calendarCheckBox.setForeground(new Color(255, 255, 255));
            this.calendars.add(calendarCheckBox);

            // add to panel
            this.add(calendarCheckBox);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBox activator = null;
        for(JCheckBox checkBox : this.calendars) {
            if(e.getSource().equals(checkBox))
                activator = checkBox;
        }
        if(activator != null) {
            String calendarName = activator.getText();
            if(activator.isSelected()) this.loadCalendar(calendarName);
            else this.unloadCalendar(calendarName);
        }
        
    }

    private void loadCalendar(String calendarName) {
        try {
            this.controller.loadCalendar(calendarName);
        } catch (ClassNotFoundException | IOException e) {
            JOptionPane.showMessageDialog(this, "Error : unable to load calendar '" + calendarName + "'");
            e.printStackTrace();
        }
    }

    private void unloadCalendar(String calendarName) {
        this.controller.unloadCalendar(calendarName);
    }
    
}
