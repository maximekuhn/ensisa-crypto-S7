package fr.uha.ensisa.crypto.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.ui.topbar.TopBarPanel;

public class MainWindow extends JFrame {

    private MainWindowController controller;
    private JSplitPane splitPane;
    private JPanel sidebar;
    private JPanel calendarList;
    private JPanel dateDisplay;
    private JPanel mainPanel;
    private TopBarPanel topBar;
    private CalendarPanel calendarPanel;

    public MainWindow(Agenda agenda) {
        // controller
        this.controller = new MainWindowController(agenda);

        this.setTitle("Cryptendar");
        this.setSize(960, 600);
        this.setLocationRelativeTo(null); // window appears on center of the screen

        // close button
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        });

        // sidebar (à gauche)

        calendarList = new JPanel();
        calendarList.setBackground(new Color(50, 50, 200));

        dateDisplay = new JPanel();
        dateDisplay.setPreferredSize(new Dimension(Integer.MAX_VALUE, 150));
        dateDisplay.setBackground(new Color(50, 200, 50));

        sidebar = new JPanel();
        sidebar.setBorder(BorderFactory.createEmptyBorder());

        sidebar.setLayout(new BorderLayout(0, 0));
        sidebar.setBackground(new Color(5, 5, 5));
        sidebar.add(calendarList, BorderLayout.CENTER);
        sidebar.add(dateDisplay, BorderLayout.PAGE_END);

        // panneau principal (à droite)

        this.topBar = new TopBarPanel(this);
        this.topBar.setBackground(new Color(50, 200, 200));
        this.topBar.setPreferredSize(new Dimension(Integer.MAX_VALUE, 64));

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

        splitPane.setDividerLocation(150);
        splitPane.setLeftComponent(sidebar);
        splitPane.setRightComponent(mainPanel);
        this.add(splitPane);

        this.setVisible(true);
    }

    public MainWindowController getController() {
        return this.controller;
    }
}
