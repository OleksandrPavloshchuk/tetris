/**
 * Main             main screen for Tetris game
 *                  (based on Sokoban example code)
 *
 * @author          Alex Pavloshchuk
 * @version 0.3a    October 2, 2002.
 */

package org.alexp.tetris;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * Main class supports 
 */
public class Main extends MIDlet implements CommandListener {
    private Display         m_display;
    private CanvasMain      m_canvas;
    private Model           m_model;
    private ScoresCounter   m_counter;
    Ticker                  m_ticker;
    
    // Commands list:
    private Command     m_cmdExit   = new Command( "Exit", Command.EXIT, 60 );
    private Command     m_cmdNew    = new Command( "New Game", Command.SCREEN, 20 );
/*    
    private Command     m_cmdPause  = new Command( "Pause", Command.SCREEN, 21 );
    private Command     m_cmdBest   = new Command( "Best Results", Command.SCREEN, 22 );
    private Command     m_cmdHelp   = new Command( "Help", Command.SCREEN, 23 );
*/    
    
    // Constructor:
    public Main() {
        m_display = Display.getDisplay( this );
        
        m_counter = new ScoresCounter();
        m_model = new Model( m_counter );      
        m_canvas = new CanvasMain( this );        
    }
    
    // Standard MIDlet abstract methods implementation:
    public void startApp() {
                        
        m_canvas.addCommand( m_cmdNew );
/*        
        m_canvas.addCommand( m_cmdPause );
        m_canvas.addCommand( m_cmdBest );
        m_canvas.addCommand( m_cmdHelp );
*/        
        m_canvas.addCommand( m_cmdExit );
        m_canvas.setCommandListener( this );
        m_display.setCurrent( m_canvas );        
    }
    public void pauseApp() {
    }
    public void destroyApp( boolean bUnconditional ) {
    }
    
    // CommandListener implementation:
    public void commandAction( Command cmd, Displayable disp ) {
                
        if( cmd == m_cmdExit ) {
            destroyApp( false );
            notifyDestroyed();
        } else if ( cmd == m_cmdNew ) {
            if( m_model.getGameStatus() != Model.GAME_ACTIVE ) {
                m_model.gameStart();
                m_ticker = new Ticker( this );
                m_ticker.start();
            } 
        } else {            
        }
    }
    
    public Model getModel() {
        return m_model;
    }
    public ScoresCounter getScoresCounter() {
        return m_counter;
    }
    public Canvas getCanvas() {
        return m_canvas;
    }
}

class CanvasMain extends Canvas {
    
    // RIM specified data:
    // - field params:
    private static final int s_colorBG = 0xFFFFFF;  // - background color
    private static final int s_colorFG = 0x000000;  // - foreground color
    private static final int s_fieldX = 5;          // - left border of field
    private static final int s_fieldY = 5;          // - top border of field
    private static final int s_fieldW = 74;         // - field width
    private static final int s_fieldH = 144;        // - field height
    // - score params:
    private static final int s_scoresX = 100;       // - left border of field
    private static final int s_scoresY = 5;         // - top border of field
    private static final int s_scoresW = 40;        // - field width
    private static final int s_scoresH = 80;        // - field height
    
    
    private Main m_app;
    private ScreenField  m_scrField;
    private ScreenScores m_scrScores;
    
    public CanvasMain( Main app ) {
        super();
        m_app = app;
        m_scrField = new ScreenField( m_app.getModel(), 
            s_fieldX, s_fieldY, s_fieldW, s_fieldH, s_colorFG, s_colorBG );
        m_scrScores = new ScreenScores( app.getScoresCounter(),
            s_scoresX, s_scoresY, s_scoresW, s_scoresH, s_colorFG, s_colorBG );
    }
    
    protected void keyPressed( int nKeyCode ) {
        Model model = m_app.getModel();
        
        if( model.getGameStatus() != Model.GAME_ACTIVE ) {
            super.keyPressed( nKeyCode );
            return;
        }
        
        synchronized( m_scrField ) {
            int nAction = this.getGameAction( nKeyCode );
            switch( nAction ) {
                case Canvas.FIRE:
                    model.generateNewField( Model.MOVE_ROTATE ); 
                    repaint(); return;
                case Canvas.DOWN:
                    model.generateNewField( Model.MOVE_RIGHT ); 
                    repaint(); return;
                case Canvas.UP:
                    model.generateNewField( Model.MOVE_LEFT ); 
                    repaint(); return;
            }
        }
        super.keyPressed( nKeyCode );
    }
    protected void keyRepeated( int nKeyCode ) {
        synchronized( m_scrField ) {
            int nAction = this.getGameAction( nKeyCode );
            switch( nAction ) {
                case Canvas.UP:
                case Canvas.DOWN:
                    keyPressed( nKeyCode );
            }
        }
    }
       
    public void paint( Graphics gr ) {
        int nClipX = gr.getClipX();
        int nClipY = gr.getClipY();
        int nClipW = gr.getClipWidth();
        int nClipH = gr.getClipHeight();
        
        // show the background:
        gr.setColor( s_colorBG );
        gr.fillRect( nClipX, nClipY, nClipW, nClipH );   
        
        // show the field screen:
        m_scrField.paint( gr );
        // show the scores screen:
        m_scrScores.paint( gr );
    } 
}
