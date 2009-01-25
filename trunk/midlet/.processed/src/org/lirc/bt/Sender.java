package org.lirc.bt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class Sender extends Thread {
    private final Main main;
    private StreamConnection conn;
    OutputStream os;
    InputStream is;

    public Sender(Main main) {
        this.main = main;
    }

    public synchronized void send(String data) {
        if (os != null) {
            try {
                main.setSignal();
                os.write(data.getBytes());
                os.flush();
                main.setConnected(true);
            } catch (IOException e) {
                cleanup();
                return;
            }
        }
        
        main.setConnected(false);
    }

    private void cleanup() {
        try {
            if (os != null)
                os.close();
            if (is != null)
                is.close();
            if (conn != null)
                conn.close();
        } catch (IOException e1) {
        }
        conn = null;
        os = null;
        is = null;
        main.setConnected(false);
    }

    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            if (conn == null && main.getUrl() != null) {
                try {
                    conn = (StreamConnection) Connector.open(main.getUrl());
                    os = conn.openOutputStream();
                    is = conn.openInputStream();
                    main.setConnected(true);
                } catch (IOException e) {
                    cleanup();
                }
            } else {
                send("nop");
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
