package fr.uha.ensisa.crypto.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.ui.calendar_selection.CalendarListPanel;
import fr.uha.ensisa.crypto.ui.dateDisplay.DateDisplayPanel;
import fr.uha.ensisa.crypto.ui.topbar.TopBarPanel;

public class MainWindow extends JFrame {

    private MainWindowController controller;
    private JSplitPane splitPane;
    private JPanel sidebar;
    private CalendarListPanel calendarList;
    private JPanel mainPanel;
    private TopBarPanel topBar;
    private CalendarPanel calendarPanel;
    private DateDisplayPanel dateDisplay;

    public MainWindow(Agenda agenda) {
        // controller
        this.controller = new MainWindowController(agenda);

        this.setTitle("Cryptendar");
        this.setSize(1020, 600);
        this.setLocationRelativeTo(null); // window appears on center of the screen

        // close button
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        // sidebar (à gauche)

        this.calendarList = new CalendarListPanel(this);
        this.calendarList.setBackground(new Color(38, 38, 38));
        this.controller.setCalendarListPanel(this.calendarList);

        this.dateDisplay = new DateDisplayPanel(this);
        this.dateDisplay.setPreferredSize(new Dimension(Integer.MAX_VALUE, 190));

        sidebar = new JPanel();
        sidebar.setBorder(BorderFactory.createEmptyBorder());

        sidebar.setLayout(new BorderLayout(0, 0));
        sidebar.setBackground(new Color(5, 5, 5));
        sidebar.add(calendarList, BorderLayout.CENTER);
        sidebar.add(dateDisplay, BorderLayout.PAGE_END);

        // panneau principal (à droite)

        this.topBar = new TopBarPanel(this);
        this.topBar.setBackground(new Color(38, 38, 38));
        this.topBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 64));
        this.controller.setTopBarPanel(this.topBar);

        calendarPanel = new CalendarPanel();
        controller.setCalendarPanel(calendarPanel);

        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(5, 5, 5));
        mainPanel.add(topBar, BorderLayout.PAGE_START);
        mainPanel.add(calendarPanel, BorderLayout.CENTER);

        // Séparation sidebar et panneau principal

        splitPane = new JSplitPane();
        splitPane.setBorder(BorderFactory.createEmptyBorder());

        splitPane.setDividerLocation(210);
        splitPane.setLeftComponent(sidebar);
        splitPane.setRightComponent(mainPanel);
        this.add(splitPane);

        this.setVisible(true);
    }

    public MainWindowController getController() {
        return this.controller;
    }
}
