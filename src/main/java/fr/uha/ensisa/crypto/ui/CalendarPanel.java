package fr.uha.ensisa.crypto.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CalendarPanel extends JScrollPane {
    JPanel grid;
    public CalendarPanel(){
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setPreferredSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
        this.setBorder(BorderFactory.createLineBorder(new Color(50,50,50)));
        grid = new JPanel();
        grid.setLayout(new GridLayout(24,7,0,0));
        grid.setPreferredSize(new Dimension(this.getWidth(),24*70));
        Random r = new Random();
        for (int i =0; i<168; i++){
            if (r.nextInt(10)<3) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Test event"));
                panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 110)));
                panel.setBackground(new Color(100 + r.nextInt(100), 100 + r.nextInt(100), 100 + r.nextInt(100)));
                grid.add(panel);

            }
            else {
                JPanel panel = new JPanel();
                panel.setBackground(new Color(50,50,50));
                panel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 110)));
                grid.add(panel);
            }
        }
        this.setViewportView(grid);
        grid.setBackground(new Color(50,50,50));
        //this.add(grid);

    }
}
