package org.lirc.bt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

public class Sender {
    private final Main main;
    
    OutputStream os;
    InputStream is;
    StreamConnection con;

    public Sender(Main main) {
        this.main = main;
    }
    
    public void init() {
        String url = main.getUrl();
        StreamConnection conn = null;

        try {
            conn = (StreamConnection) Connector.open(url);
            os = conn.openOutputStream();
            is = conn.openInputStream();
        } catch (IOException e) {
            Alert al = new Alert("Failed", "Failed to connect to service",
                    null, AlertType.ERROR);
            al.setTimeout(2000);
            // TODO close the connection 
            conn = null;
            os = null;
            is = null;
        }
    }
    
    public void send(String data) {
        if (os != null) {
            try {
                os.write(data.getBytes());
                os.flush();
            } catch (IOException e) {
                Alert al = new Alert("Failed", "Failed to send",
                        null, AlertType.ERROR);
                al.setTimeout(2000);
            }
        }
    }
    
    
}
 
