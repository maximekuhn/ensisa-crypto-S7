package fr.uha.ensisa.crypto.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.model.Calendar;
import fr.uha.ensisa.crypto.model.Event;
import fr.uha.ensisa.crypto.model.EventTable;

/**
 * Main centered panel that display all events from all loaded calendars.
 */
public class CalendarPanel extends JScrollPane {
    private JPanel grid;
    private Date selectedDate;

    /**
     * Constructor.
     */
    public CalendarPanel() {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        this.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        this.selectedDate = new Date();
        this.grid = new JPanel();
        this.grid.setLayout(new GridLayout(48, 7, 0, 0));
        this.grid.setPreferredSize(new Dimension(this.getWidth(), 24 * 90));
        this.refreshGrid();
        this.setViewportView(this.grid);
        this.grid.setBackground(new Color(50, 50, 50));
    }

    /**
     * Refresh the whole panel.
     */
    public void refreshGrid() {

        Agenda agenda = Agenda.getInstance();
        Collection<Calendar> calendars = agenda.getAllCalendars();

        this.grid.removeAll();
        Date monday = Date.from(this.selectedDate.toInstant() // On convertit vers une LocalDate pour pouvoir récupérer le
                                                         // lundi puis reconversion en Date
                .atZone(ZoneId.systemDefault())
                .toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay().atZone(ZoneId.systemDefault())
                .toInstant());

        for (int i = 0; i < 336; i++) {
            int day = i % 7;
            int minute = 30 * (i / 7);
            Date current = this.addDaysMinutes(monday, day, minute);
            JPanel panel = new JPanel();
            JLabel label = new JLabel(
                    "<html><center>" +
                            current.getDate() + "/" + (current.getMonth() + 1) + "/" + (1900 + current.getYear()) +
                            "<br>" + current.getHours() + ":" + current.getMinutes() + "<br>" +
                            "</center></html>");
            if (day >= 5)
                panel.setBackground(new Color(50, 50, 50));
            else
                panel.setBackground(new Color(40, 40, 40));

            label.setForeground(new Color(80, 80, 80));

            panel.add(label);
            panel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, new Color(60, 60, 60)));

            // panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 110)));
            this.grid.add(panel);
        }

        for (int i = 0; i < 336; i++) {
            int day = i % 7;
            int minute = 30 * (i / 7);
            Date current = this.addDaysMinutes(monday, day, minute);
            ArrayList<Event> events = new ArrayList<>();
            for (Calendar calendar : calendars) {
                EventTable eventTable = calendar.getEventTable();
                Event event = eventTable.search(current);
                if (event != null) {
                    events.add(event);
                }
            }
            JPanel panel = (JPanel) this.grid.getComponent(i);
            StringBuilder tooltip = new StringBuilder("<html>");
            if (events.size() > 0) {
                panel.removeAll();
                for (Event e : events) {
                    panel.add(new JLabel(e.getEvent()));
                    tooltip.append("<center><strong>").append(e.getEvent()).append("</center></strong><br>");
                    tooltip.append(e.getDescription()).append("<br>");
                    tooltip.append("Lieu: ").append(e.getLocation()).append("<br>");
                    tooltip.append("Durée: ").append(e.getDuration()).append("<br>");

                }
                tooltip.append("</html>");
                panel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, new Color(60, 60, 60)));
                panel.setBackground(new Color(200, 100, 150));
                panel.setToolTipText(tooltip.toString());
                for (Event e : events) {
                    for (int j = 0; j < e.getDuration() * 2; j++) {
                        JPanel panel2 = (JPanel) this.grid.getComponent(j * 7 + i);
                        if (j == 0 && e.getDuration() > 1) {
                            panel2.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(60, 60, 60)));
                        } else if (j > 0) {
                            panel2.removeAll();
                            if (j == (e.getDuration() * 2 - 1))
                                panel2.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(60, 60, 60)));
                            else
                                panel2.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, new Color(60, 60, 60)));

                            panel2.setBackground(new Color(200, 100, 150));
                            panel2.setToolTipText(tooltip.toString());
                        }
                    }

                }

            }

        }
        this.revalidate();
    }

    /**
     * Change current display to selected date.
     * @param date to display
     */
    public void setSelectedDate(Date date) {
        this.selectedDate = date;
        this.refreshGrid();
    }

    /**
     * Add minutes and date to every event.
     * @param current current date
     * @param days days to display
     * @param minutes minutes to display
     * @return current Date
     */
    private Date addDaysMinutes(Date current, int days, int minutes) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(java.util.Calendar.MINUTE, minutes);
        calendar.add(java.util.Calendar.DATE, days);

        return calendar.getTime();
    }
}
