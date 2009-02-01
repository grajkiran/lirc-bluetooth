package org.lirc.bt;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Main extends MIDlet implements CommandListener {
    public static final String DEFAULT_NAME = "LIRC Remote";

    // Display
    private Display display;

    // Main form
    private Displayable displayable;

    // For the message
    private Command exitCommand;
    private Command optionsCommand;

    private String url = null;

    private Sender sender ;

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
        if (sender != null) {
            sender.stop();
            sender = null;
        }
        sender = new Sender(this);
        sender.start();
    }

    public Main() {
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // TODO Auto-generated method stub

    }

    protected void pauseApp() {
        // TODO Auto-generated method stub

    }

    protected void startApp() throws MIDletStateChangeException {
        displayable = new RemoteControlCanvas(this);

        exitCommand = new Command("Exit", Command.EXIT, 1);
        displayable.addCommand(exitCommand);
        displayable.setCommandListener(this);

        optionsCommand = new Command("Search", Command.SCREEN, 1);
        displayable.addCommand(optionsCommand);
        displayable.setCommandListener(this);

        String url = Settings.getLastKnownURL();
        if (url != null) {
            changeServer(url);
        } else {
            commandAction(optionsCommand, displayable);
        }

        
    }

    public void commandAction(Command command, Displayable displayable) {
        if (displayable == this.displayable) {
            if (command == this.exitCommand) {
                exitMIDlet();
            } else if (command == this.optionsCommand) {
                if (sender != null) {
                    sender.stop();
                    sender = null;
                }
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
        if (code > 0 && code < 127) {
            sender.send(new String("KEY_" + (char) code));
        } else {
            sender.send(new String("KEY_" + Integer.toHexString(code).toUpperCase()));
        }
    }

    public void updateConnectionStatus(String title) {
        Display disp = Display.getDisplay(this);
        if (disp != null) {
            Displayable d = disp.getCurrent();
            if (d instanceof RemoteControlCanvas) {
                d.setTitle(title);
            }
        }
    }
}
