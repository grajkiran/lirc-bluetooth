package org.lirc.bt;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Main extends MIDlet implements CommandListener {

    public static final String NAME = "[x] BT Remote";

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
        displayable = new RemoteControlCanvas(this);
        displayable.setTitle(NAME);

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
            restore();
        }

        sender = new Sender(this);
        sender.start();
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
        if (sender != null) {
            sender.send(String.valueOf(code) + "\n");
        }
    }

    public void setStatus(String string) {
        Display.getDisplay(this).getCurrent().setTitle(string);
    }
    
    private void updateTitle(String prefix) {
        String title = Display.getDisplay(this).getCurrent().getTitle();
        //title = title.substring(prefix.length(), title.length());
        title = prefix + title;
        Display.getDisplay(this).getCurrent().setTitle(title);

    }

    public void setConnected(boolean connected) {
        //updateTitle(connected ? "[o] " : "[x] ");

    }
    
    public void setSignal() {
        //updateTitle("[.] ");
    }
}
