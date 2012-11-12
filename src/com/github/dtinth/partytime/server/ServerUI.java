package com.github.dtinth.partytime.server;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

    public ServerUI(Server server) {
        this.server = server;
        initComponents();
        update();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.server.addObserver(this);
    }

    @Override
    public void update(Observable o, Object o1) {
        update();
    }

    private void update() {
        label.setText(server.getStatus());
        tableModel.fireTableDataChanged();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setTitle("Party Time Server!");
        
        label = new JLabel("Status will display here...");
        add(label, BorderLayout.NORTH);
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
        public Object getValueAt(int row, int col) {
            Connection conn = null;
            try { conn = connections.get(row); } catch (Exception e) { }
            if (conn == null) return null;
            if (col == 0) return conn + "";
            if (col == 1) return conn.getStatus();
            return "";
        }
    }
    
}
