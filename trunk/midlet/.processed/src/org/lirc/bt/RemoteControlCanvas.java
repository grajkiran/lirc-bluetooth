package org.lirc.bt;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class RemoteControlCanvas extends Canvas {
    int x = 0;
    int y = 0;
    int code = 0;

    Image i;
    
    private final Main main;

    public RemoteControlCanvas(Main main) {
        super();
        this.main = main;
        try {
            i = Image.createImage("/t.png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void paint(Graphics g) {
        // TODO Auto-generated method stub
        g.setColor(255, 0, 0);

        int lastX = x;
        int lastY = y;
        switch (code) {
        case UP:
            y -= 5;
            break;
        case DOWN:
            y += 5;
            break;
        case LEFT:
            x -= 5;
            break;
        case RIGHT:
            x += 5;
            break;
        case FIRE:
            g.setColor(0, 255, 0);
        }
        // g.drawString(String.valueOf(code), x, y, 0);
        if (i != null) {
            g.drawImage(i, 0, 0, 0);
            i = null;
        }

        g.drawLine(lastX, lastY, x, y);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.microedition.lcdui.Canvas#keyPressed(int)
     */
    protected void keyPressed(int keyCode) {
        code = getGameAction(keyCode);
        main.sendEvent(code);
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
