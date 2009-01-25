package org.lirc.bt;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public class RemoteControlCanvas extends Canvas {
    int x = 0;
    int y = 0;
    int code = 0;

    private final Main main;

    public RemoteControlCanvas(Main main) {
        super();
        this.main = main;
        setTitle(Main.NAME);
    }

    protected void paint(Graphics g) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.microedition.lcdui.Canvas#keyPressed(int)
     */
    protected void keyPressed(int keyCode) {
        main.sendEvent(keyCode);
        repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.microedition.lcdui.Canvas#keyRepeated(int)
     */
    protected void keyRepeated(int keyCode) {
        keyPressed(keyCode);
    }

}
