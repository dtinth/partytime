package com.github.dtinth.partytime.server;

import javax.swing.JFrame;
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
        
        Server server = new Server();
        ServerUI ui = new ServerUI(server);
        
        System.out.print(":1");
        SwingUtilities.invokeLater(ui);
        System.out.print(":2");
        new Thread(server).start();
        System.out.print(":3");
        
    }
}
