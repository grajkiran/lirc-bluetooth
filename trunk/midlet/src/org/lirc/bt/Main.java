package org.lirc.bt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Main extends MIDlet implements CommandListener {

    // Display
    private Display display;
    // Main form
    private Displayable displayable;
    // For the message
    private Command exitCommand;
    
    OutputStream os;
    StreamConnection con;

    public Main() {
        // TODO Auto-generated constructor stub
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // TODO Auto-generated method stub

    }

    protected void pauseApp() {
        // TODO Auto-generated method stub

    }

    protected void startApp() throws MIDletStateChangeException {
        
        displayable = new RemoteControlCanvas();
        
        exitCommand = new Command("Exit", Command.EXIT, 1);
        displayable.addCommand(exitCommand);
        displayable.setCommandListener(this);
        
        // Get display for drawing
        display = Display.getDisplay(this);
        display.setCurrent(displayable);
        
        connect();
        
    }
    
    public void commandAction(Command command, Displayable displayable) {
        if (displayable == this.displayable) {
            if (command == this.exitCommand) {
                exitMIDlet();
            } 
        }
    }
    
    public void exitMIDlet() {
        display.setCurrent(null);
        try {
            con.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        notifyDestroyed();
    }

    private void connect() {
        try {
            String url = "btspp://0014A48F535A:1";

            con = 
                (StreamConnection) Connector.open(url);

            os = con.openOutputStream();

            ((RemoteControlCanvas) displayable).setStream(os);
           
        } catch ( IOException e ) {
            System.err.print(e.toString());
        }   
    }
}
