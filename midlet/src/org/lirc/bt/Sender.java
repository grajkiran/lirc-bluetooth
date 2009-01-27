package org.lirc.bt;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class Sender extends Thread {
    private final Main main;
    private StreamConnection conn;
    private OutputStream os;
    private boolean connected = false;
    private static final String DEFAULT_NAME = "LIRC Remote";
    private int timeout = 1000;

    public Sender(Main main) {
        this.main = main;
    }

    public synchronized void send(String data) {
        if (os != null) {
            try {
                os.write(data.getBytes());
                os.flush();
                setConnected(true);
            } catch (IOException e) {
                cleanup();
            }
        } else {
            setConnected(false);
        }
    }

    private void cleanup() {
        try {
            if (os != null)
                os.close();
            if (conn != null)
                conn.close();
        } catch (IOException e) {
        }
        conn = null;
        os = null;
        setConnected(false);
    }

    public void run() {
        while (true) {
            if (conn == null && main.getUrl() != null) {
                try {
                    conn = (StreamConnection) Connector.open(main.getUrl());
                    os = conn.openOutputStream();
                    setConnected(true);
                } catch (IOException e) {
                    cleanup();
                }
            } else {
                send("nop");
            }

            try {
                sleep(this.timeout);
            } catch (InterruptedException e) {
            }
        }
    }

    private void setConnected(boolean connected) {
        if (connected != this.connected) {
            this.connected = connected;
            this.timeout = this.connected ? 5000 : 1000;
            main.updateConnectionStatus((this.connected ? "[o]" : "[x]") + " "
                    + DEFAULT_NAME);
        }
    }
}
