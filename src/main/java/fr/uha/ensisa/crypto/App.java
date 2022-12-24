package fr.uha.ensisa.crypto;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import fr.uha.ensisa.crypto.model.Agenda;
import fr.uha.ensisa.crypto.ui.MainWindow;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        MainWindow mainWindow = new MainWindow(Agenda.getInstance());
    }
}
