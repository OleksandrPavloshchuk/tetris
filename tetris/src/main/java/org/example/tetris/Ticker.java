/**
 * Ticker.java  refreshes the game screen with the specified speed
 *
 * @author      Alex Pavloshchuk
 * @version     0.2a September 10, 2002
 */

package org.alexp.tetris;

import java.util.*;
import javax.microedition.lcdui.*;

public class Ticker extends Thread {
    private Main m_app = null;
    private int m_nDelay = 200;
    
    Ticker(Main app ) {
        m_app = app;
    }
    
    public void run() {
        try {
            Model model = m_app.getModel();
            Canvas canvas = m_app.getCanvas();
            int nGameStatus = model.getGameStatus();
            while( nGameStatus != Model.GAME_OVER ) {
                synchronized( model ) {
                    if( nGameStatus != Model.GAME_ACTIVE ) continue;
            
                    sleep( m_nDelay );
                    model.generateNewField( Model.MOVE_DOWN );
                    canvas.repaint();
                }
            }
        } catch ( InterruptedException ex ) {
        }
    }
    
    public void setDelay( int nDelay ) {
        m_nDelay = nDelay;
    }
    public int getDelay() {
        return m_nDelay;
    }
}
