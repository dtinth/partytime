package com.github.dtinth.partytime.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Pangsakulyanont
 */
public class Server extends Observable implements Runnable {
    
    private String status = "Creating server...";
    private final List<Connection> connections = new LinkedList<Connection>();
    private ServerSocket server;
    private final ConnectionDelegate connectionDelegate = new ServerConnectionDelegate();
    
    private int port;
    
    public Server(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public Thread start() {
        Thread thread = new Thread(this);
        thread.start();
        return thread;
    }
    
    public void startGame() {
        List<Connection> list = new LinkedList<Connection>(connections);
        connections.clear();
        long time = System.currentTimeMillis() + 5000;
        for (Connection connection : list) {
            connection.go(time, list.size());
        }
        //setStatus("Game will start in 5 seconds... Listening on port " + port);
        setChanged();
        notifyObservers();
    }
    
    @Override
    public void run() {
        setStatus("Starting server...");
        try {
            server = new ServerSocket(port);
            while (true) {
                setStatus("Listening on port " + port);
                Socket socket = server.accept();
                Connection connection = new Connection(socket, connectionDelegate);
                new Thread(connection).start();
                // gameTimer.reset();
                connections.add(connection);
            }
        } catch (Exception ex) {
            setStatus("Cannot start server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void stop() {
        try {
            server.close();
            System.out.println("Server is closed!");
            setChanged();
            notifyObservers();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isClosed() {
        return server.isClosed();
    }
    
    private void setStatus(String status) {
        this.status = status;
        setChanged();
        notifyObservers();
    }

    public String getStatus() {
        return status;
    }

    public List<Connection> getConnections() {
        return connections;
    }
    
    private class ServerConnectionDelegate implements ConnectionDelegate {

        @Override
        public void closed(Connection connection) {
            connections.remove(connection);
            setChanged();
            notifyObservers();
        }

        @Override
        public void statusChanged(Connection connection) {
            setChanged();
            notifyObservers();
        }

    }
    
}
