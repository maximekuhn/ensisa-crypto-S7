package fr.uha.ensisa.crypto.ui.dateDisplay;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JPanel;

import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class DateDisplayPanel extends JPanel implements ActionListener {
	
	private MainWindow mainWindow;
	private MainWindowController controller;
	private JDatePanelImpl datePanel;
	
	public DateDisplayPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.controller = this.mainWindow.getController();
		
		// create date Panel
		UtilDateModel dateModel = new UtilDateModel();
        dateModel.setSelected(true);
        this.datePanel = new JDatePanelImpl(dateModel);

        // click action
        this.datePanel.addActionListener(this);

        // add date panel 
        this.add(this.datePanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.datePanel)) this.changeCalendarDate();
	}
	
	private void changeCalendarDate() {
		Date date = (Date) this.datePanel.getModel().getValue();
		controller.goToDate(date);
	}

}
