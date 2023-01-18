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

/**
 * Customized JFrame that contains everything and a controller to handle user's interactions.
 */
public class MainWindow extends JFrame {

    private MainWindowController controller;
    private JSplitPane splitPane;
    private JPanel sidebar;
    private CalendarListPanel calendarList;
    private JPanel mainPanel;
    private TopBarPanel topBar;
    private CalendarPanel calendarPanel;
    private DateDisplayPanel dateDisplay;

    /**
     * Constructor
     * @param agenda singleton used to manage all calendars and evet
     * @see fr.uha.ensisa.crypto.model.Agenda
     */
    public MainWindow(Agenda agenda) {
        // controller
        this.controller = new MainWindowController(agenda);

        this.setTitle("Cryptendar");
        this.setSize(1200, 600);
        this.setLocationRelativeTo(null); // window appears on center of the screen

        // close button
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        // sidebar (à gauche)

        this.calendarList = new CalendarListPanel(this);
        this.controller.setCalendarListPanel(this.calendarList);

        this.dateDisplay = new DateDisplayPanel(this);
        this.dateDisplay.setPreferredSize(new Dimension(Integer.MAX_VALUE, 190));

        this.sidebar = new JPanel();
        this.sidebar.setBorder(BorderFactory.createEmptyBorder());

        this.sidebar.setLayout(new BorderLayout(0, 0));
        this.sidebar.setBackground(new Color(5, 5, 5));
        this.sidebar.add(this.calendarList, BorderLayout.CENTER);
        this.sidebar.add(this.dateDisplay, BorderLayout.PAGE_END);

        // panneau principal (à droite)

        this.topBar = new TopBarPanel(this);
        this.topBar.setBackground(new Color(38, 38, 38));
        this.topBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 64));
        this.controller.setTopBarPanel(this.topBar);

        this.calendarPanel = new CalendarPanel();
        this.controller.setCalendarPanel(this.calendarPanel);

        this.mainPanel = new JPanel();
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder());
        this.mainPanel.setLayout(new BorderLayout(0, 0));
        this.mainPanel.setBackground(new Color(5, 5, 5));
        this.mainPanel.add(this.topBar, BorderLayout.PAGE_START);
        this.mainPanel.add(this.calendarPanel, BorderLayout.CENTER);

        // Séparation sidebar et panneau principal

        this.splitPane = new JSplitPane();
        this.splitPane.setBorder(BorderFactory.createEmptyBorder());

        this.splitPane.setDividerLocation(210);
        this.splitPane.setLeftComponent(this.sidebar);
        this.splitPane.setRightComponent(this.mainPanel);
        this.add(this.splitPane);

        this.setVisible(true);
    }

    /**
     * @return the controller of this JFrame
     */
    public MainWindowController getController() {
        return this.controller;
    }
}
