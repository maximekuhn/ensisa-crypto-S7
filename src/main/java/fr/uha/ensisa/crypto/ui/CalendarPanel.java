package fr.uha.ensisa.crypto.ui;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.model.Calendar;
import fr.uha.ensisa.crypto.model.Event;
import fr.uha.ensisa.crypto.model.EventTable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class CalendarPanel extends JScrollPane {
    JPanel grid;
    Date selectedDate;
    public CalendarPanel(){
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        selectedDate = new Date();//(2022,11,23,5,0);

        Date testEvent = new Date(122,11,23,5,0);
        Agenda a = Agenda.getInstance();
        try {
            a.createCalendar("ohohoh");
            a.getCalendar("ohohoh").getEventTable().addEvent(new Event(testEvent, 2, "Evenement", "un evenement interessant", "Batiment I"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        this.setPreferredSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        this.setBorder(BorderFactory.createLineBorder(new Color(50,50,50)));
        grid = new JPanel();
        grid.setLayout(new GridLayout(24,7,0,0));
        grid.setPreferredSize(new Dimension(this.getWidth(),24*40));
        refreshGrid();
        this.setViewportView(grid);
        grid.setBackground(new Color(50,50,50));
        //this.add(grid);

    }

    public void refreshGrid(){
        Agenda agenda = Agenda.getInstance();
        Collection<Calendar> calendars = agenda.getAllCalendars();
        grid.removeAll();

        Date monday = Date.from(selectedDate.toInstant() // On convertit vers une LocalDate pour pouvoir récupérer le lundi puis reconversion en Date
                .atZone(ZoneId.systemDefault())
                .toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay().atZone(ZoneId.systemDefault())
                .toInstant());
//        monday = new Date(2022,12,22,3,0);

        for (int i=0; i<168; i++){
            int day = i%7;
            int hour = i/7;
            Date current = addDaysHours(monday,day,hour);
            ArrayList<Event> events = new ArrayList<Event>();
            for (Calendar calendar:calendars) {
                EventTable eventTable = calendar.getEventTable();
                Event event = eventTable.search(current);
                if(event != null)
                    events.add(event);
            }
            JPanel panel = new JPanel();
            String tooltip = "<html>";
            if (events.size()>0) {

                for (Event e: events) {
                    panel.add(new JLabel(e.getEvent()));
                    tooltip+="<strong>"+e.getEvent()+"</strong><br>";
                    tooltip+=e.getDescription()+"<br>";
                    tooltip+=e.getLocation()+"<br>";

                }
                tooltip+="</html>";
                panel.setToolTipText(tooltip);
                panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 110)));
                panel.setBackground(new Color(200,100,150));

            }
            else {
                panel.setBackground(new Color(50,50,50));
                panel.add(new JLabel(current.getDate()+"/"+current.getMonth()+"/"+1900+current.getYear()+" "+current.getHours()+":00"));

                panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 110)));
            }
            grid.add(panel);
        }
    }

    public void setSelectedDate(Date date){
        selectedDate = date;
    }

    private Date addDaysHours(Date current,int days,int hours) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(java.util.Calendar.HOUR_OF_DAY, hours);
        calendar.add(java.util.Calendar.DATE, days);

        return calendar.getTime();
    }
}
