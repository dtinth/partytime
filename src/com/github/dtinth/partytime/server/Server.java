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
public class Server extends Observable implements Runnable, ConnectionDelegate {
    public static final int PORT = 7273;
    
    private String status = "Creating server...";
    private final List<Connection> connections = new LinkedList<Connection>();
    private ServerSocket server;
    
    /*
    private Timer timer = new Timer();
    private GameTimer gameTimer;
    
    private class GameTimer extends TimerTask {
        
        private int timeLeft;

        @Override
        public void run() {
            if (connections.isEmpty()) {
                setStatus("Waiting for players...");
                return;
            }
            timeLeft --;
            setStatus("Waiting for more players... [" + timeLeft + "]");
            if (timeLeft <= 0) {
     startGame();
            }
        }
        
        public void reset() {
            timeLeft = 30;
        }
        
    }
     */
    
    public void startGame() {
        List<Connection> list = new LinkedList<Connection>(connections);
        connections.clear();
        long time = System.currentTimeMillis() + 5000;
        for (Connection connection : list) {
            connection.go(time, list.size());
        }
        setStatus("Game started!");
    }
    
    @Override
    public void run() {
        /*
        gameTimer = new GameTimer();
        timer.schedule(gameTimer, 1000, 1000);
         */
        setStatus("Starting server...");
        try {
            server = new ServerSocket(PORT);
            setStatus("Listening on port " + PORT);
            while (true) {
                Socket socket = server.accept();
                Connection connection = new Connection(socket, this);
                new Thread(connection).start();
                // gameTimer.reset();
                connections.add(connection);
            }
        } catch (IOException ex) {
            setStatus("Cannot start server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void stop() {
        try {
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
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
