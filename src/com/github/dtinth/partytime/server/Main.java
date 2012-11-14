package com.github.dtinth.partytime.server;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Thai Pangsakulyanont
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String portText = JOptionPane.showInputDialog("What port?", "7273");
        
        if (portText == null || portText.isEmpty()) {
            return;
        }
        
        int port = 0;
        
        try {
            port = Integer.parseInt(portText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error", "Invalid port", JOptionPane.ERROR_MESSAGE, null);
        }
        
        Server server = new Server(port);
        ServerUI ui = new ServerUI(server);
        
        SwingUtilities.invokeLater(ui);
        server.start();
        
    }
}
