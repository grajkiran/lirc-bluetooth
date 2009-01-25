package org.lirc.bt;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
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
    private Command optionsCommand;

    private String url = null;

    private Sender sender;

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void changeServer(String url) {
        this.url = url;
        restore();
        sender = new Sender(this);
        sender.init();
    }

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

        // displayable = new RemoteControlCanvas();

        displayable = new RemoteControlCanvas(this);

        exitCommand = new Command("Exit", Command.EXIT, 1);
        displayable.addCommand(exitCommand);
        displayable.setCommandListener(this);

        optionsCommand = new Command("Options", Command.OK, 1);
        displayable.addCommand(optionsCommand);
        displayable.setCommandListener(this);

        restore();
    }

    public void commandAction(Command command, Displayable displayable) {
        if (displayable == this.displayable) {
            if (command == this.exitCommand) {
                exitMIDlet();
            } else if (command == this.optionsCommand) {
                DeviceDiscovery d = new DeviceDiscovery(this);
                d.discover();
            }
        }
    }

    public void exitMIDlet() {
        display.setCurrent(null);
        notifyDestroyed();
    }

    public void restore() {
        // Get display for drawing
        display = Display.getDisplay(this);
        display.setCurrent(displayable);

    }

    public void sendEvent(int code) {
        // TODO Auto-generated method stub
        sender.send(String.valueOf(code) + "\n");
    }
}
