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

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.model.Calendar;
import fr.uha.ensisa.crypto.model.Event;
import fr.uha.ensisa.crypto.model.EventTable;

public class CalendarPanel extends JScrollPane {
    private JPanel grid;
    private Date selectedDate;
    public CalendarPanel() {
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        this.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        selectedDate = new Date();
        grid = new JPanel();
        grid.setLayout(new GridLayout(24, 7, 0, 0));
        grid.setPreferredSize(new Dimension(this.getWidth(), 24 * 50));
        refreshGrid();
        this.setViewportView(grid);
        grid.setBackground(new Color(50, 50, 50));
    }

    public void refreshGrid(){

        Agenda agenda = Agenda.getInstance();
        Collection<Calendar> calendars = agenda.getAllCalendars();

        grid.removeAll();
        Date monday = Date.from(selectedDate.toInstant() // On convertit vers une LocalDate pour pouvoir récupérer le lundi puis reconversion en Date
                .atZone(ZoneId.systemDefault())
                .toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay().atZone(ZoneId.systemDefault())
                .toInstant());



        for (int i=0; i<168; i++){
            int day = i%7;
            int hour = i/7;
            Date current = addDaysHours(monday,day,hour);
            ArrayList<Event> events = new ArrayList<Event>();
            for (Calendar calendar:calendars) {
                EventTable eventTable = calendar.getEventTable();
                Event event = eventTable.search(current);
                if(event != null) {
                    events.add(event);
                    System.out.println("Found event at "+current.toString());
                }

            }
            JPanel panel = new JPanel();
            StringBuilder tooltip = new StringBuilder("<html>");
            if (events.size()>0) {
                for (Event e: events) {
                    panel.add(new JLabel(e.getEvent()));
                    tooltip.append("<center><strong>").append(e.getEvent()).append("</center></strong><br>");
                    tooltip.append(e.getDescription()).append("<br>");
                    tooltip.append("Lieu: ").append(e.getLocation()).append("<br>");
                    tooltip.append("Durée: ").append(e.getDuration()).append("<br>");


                }
                tooltip.append("</html>");
                panel.setToolTipText(tooltip.toString());
                panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 110)));
                panel.setBackground(new Color(200,100,150));

            }
            else {
                JLabel label = new JLabel(
                        "<html><center>"+
                                current.getDate()+"/"+(current.getMonth()+1)+"/"+(1900+current.getYear())+
                                "<br>"+current.getHours()+":00<br>"+
                                "</center></html>"
                );
                panel.setBackground(new Color(50,50,50));
                label.setForeground(new Color(80,80,80));

                panel.add(label);

                panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 110)));
            }
            grid.add(panel);
        }
        revalidate();
    }

    public void setSelectedDate(Date date){
        selectedDate = date;
        refreshGrid();
    }

    private Date addDaysHours(Date current,int days,int hours) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(java.util.Calendar.HOUR_OF_DAY, hours);
        calendar.add(java.util.Calendar.DATE, days);

        return calendar.getTime();
    }
}
