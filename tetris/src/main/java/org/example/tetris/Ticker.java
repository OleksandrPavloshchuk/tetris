/**
 * Ticker.java  refreshes the game screen with the specified speed
 */

package org.example.tetris;

import org.example.gui.swing.Canvas;

public class Ticker extends Thread {
    private Main m_app = null;
    private int m_nDelay = 200;
    
    Ticker(Main app ) {
        m_app = app;
    }
    
    @Override
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
