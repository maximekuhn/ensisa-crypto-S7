package fr.uha.ensisa.dateDisplay;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JPanel;

import fr.uha.ensisa.crypto.ui.MainWindow;
import fr.uha.ensisa.crypto.ui.MainWindowController;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
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
        
        // add date panel 
        this.add(this.datePanel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
	}

}
