package Zoha.gui;

import javax.swing.SwingUtilities;


public class VLSMApplication {

    public static void main(String[] args) {
        Runnable displayWindow = () -> {
            MainFrame frame = new MainFrame();
        };

        SwingUtilities.invokeLater(displayWindow);
    }

}
