package fr.uha.ensisa.crypto;

import javax.swing.*;

import fr.uha.ensisa.crypto.ui.MainWindow;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        MainWindow mainWindow = new MainWindow();
    }
}
