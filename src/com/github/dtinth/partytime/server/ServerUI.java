package com.github.dtinth.partytime.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Thai Pangsakulyanont
 */
public class ServerUI extends JFrame implements Observer, Runnable {
    
    private final Server server;
    private JLabel label;
    private JTable table;
    private ConnectionsTableModel tableModel;

    private StartAction startAction = new StartAction();
    
    public ServerUI(Server server) {
        this.server = server;
        initComponents();
        update();
        addWindowListener(new ServerWindowListener());
        this.server.addObserver(this);
    }

    @Override
    public void update(Observable o, Object o1) {
        update();
    }

    private void update() {
        label.setText(server.getStatus());
        tableModel.fireTableDataChanged();
        if (!server.getConnections().isEmpty()) {
            startAction.putValue(Action.NAME, "Start Game");
            startAction.setEnabled(true);
        } else {
            startAction.putValue(Action.NAME, "Waiting for Players");
            startAction.setEnabled(false);
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setTitle("Party Time Server!");
        
        JButton button = new JButton(startAction);
        
        label = new JLabel("Status will display here...");
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label);
        panel.add(button, BorderLayout.EAST);
        
        add(panel, BorderLayout.NORTH);
        tableModel = new ConnectionsTableModel();
        
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(400, 300);
        add(scrollPane);
        
        pack();
    }

    @Override
    public void run() {
        setVisible(true);
    }
    
    private class StartAction extends AbstractAction {
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            server.startGame();
        }
    }
    
    private class ConnectionsTableModel extends AbstractTableModel {
        
        List<Connection> connections = server.getConnections();
        
        public ConnectionsTableModel() {
        }

        @Override
        public int getRowCount() {
            return connections.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int i) {
            if (i == 0) return "Client IP Address";
            if (i == 1) return "Status";
            return super.getColumnName(i);
        }

        @Override
        public Object getValueAt(int row, int col) {
            Connection conn = null;
            try { conn = connections.get(row); } catch (Exception e) { }
            if (conn == null) return null;
            if (col == 0) return conn + "";
            if (col == 1) return conn.getStatus();
            return "";
        }
    }
    
    private class ServerWindowListener extends WindowAdapter {

        @Override
        public void windowClosed(WindowEvent we) {
            System.out.println("Received stop event");
            server.stop();
        }

        @Override
        public void windowClosing(WindowEvent we) {
            dispose();
        }
        
    }
    
}
