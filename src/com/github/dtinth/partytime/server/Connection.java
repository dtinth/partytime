package com.github.dtinth.partytime.server;

import com.sun.org.apache.xml.internal.utils.ObjectStack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thai Pangsakulyanont
 */
public class Connection implements Runnable {
    public static final int SYNC_TIMES = 50;
    
    private final Socket socket;
    private final ConnectionDelegate delegate;
    private String status = "Initializing...";
    
    private BufferedReader reader;
    private PrintWriter writer;
    private long offset;

    public Connection(Socket socket, ConnectionDelegate delegate) {
        this.socket = socket;
        this.delegate = delegate;
    }

    @Override
    public void run() {
        boolean keep = false;
        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new PrintWriter(this.socket.getOutputStream(), true);
            setStatus("Client accepted.");
            
            long minRoundTrip = 0;
            long offset = 0;
            
            for (int i = 1; i < SYNC_TIMES; i ++) {
                setStatus("Syncing: " + i + " / " + SYNC_TIMES);
                writer.println("time?");
                writer.println("status:Syncing " + i + " / " + SYNC_TIMES);
                long sent = System.currentTimeMillis();
                long time = Long.parseLong(reader.readLine());
                long received = System.currentTimeMillis();
                long roundTrip = received - sent;
                long theirTime = time + roundTrip / 2;
                if (i == 1 || roundTrip < minRoundTrip) {
                    minRoundTrip = roundTrip;
                    // their time = our time + offset
                    offset = theirTime - received;
                }
            }
            writer.println("synced");
            writer.println("status:Waiting for other players...");
            keep = true;
            this.offset = offset;
            setStatus("Client synchronized. Offset: " + offset);
        } catch (IOException ex) {
            setStatus("Read error.");
            ex.printStackTrace();
        } catch (Exception e) {
            setStatus(e.toString());
            e.printStackTrace();
        } finally {
            if (!keep) close();
        }
    }
    
    public void go(long time, int players) {
        writer.println("status:" + players + "-person play");
        writer.println("play:" + (time + offset));
        close();
    }
    
    public void close() {
        try { socket.close(); } catch (Exception e) {}
        delegate.closed(this);
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        if (!status.equals(this.status)) {
            this.status = status;
            this.delegate.statusChanged(this);
        }
    }

    @Override
    public String toString() {
        return socket.getInetAddress().getHostAddress();
    }
    
}
