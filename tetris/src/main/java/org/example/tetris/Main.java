/**
 * Main             main screen for Tetris game
 */

package org.example.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.example.gui.swing.Activity;
import org.example.gui.swing.Bundle;
import org.example.gui.swing.Canvas;
import org.example.gui.swing.Menu;
import org.example.gui.swing.MenuItem;

/**
 * Main class supports 
 */
@SuppressWarnings("serial")
public class Main extends Activity implements KeyListener {
    private static final int ID_NEW_GAME = Menu.FIRST + 2;
	private static final int ID_EXIT = Menu.FIRST + 3;
	
    private CanvasMain      m_canvas;
    private Model           m_model;
    private ScoresCounter   m_counter;
    Ticker                  m_ticker;
    
    @Override
    public void onCreate( Bundle storedState ) {
    	super.onCreate(storedState);
    	
        m_counter = new ScoresCounter();
        m_model = new Model( m_counter );      
        m_canvas = new CanvasMain( this );
        
        // TODO: swing related...
        setLayout( new BorderLayout());
        add( m_canvas, BorderLayout.CENTER );
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	
    	// TODO: eliminate listeners on Android
    	
    	menu.add( 0, ID_NEW_GAME, "New Game", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startNewGame();
			}
		} );
    	menu.add( 1, ID_EXIT, "Exit", new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		} );
		return super.onCreateOptionsMenu(menu);
	} 
    
    @Override
	public boolean onOptionsItemSelected( MenuItem item ) {
    	switch( item.getId() ) {
    	case ID_NEW_GAME:
    		startNewGame();
    		return true;
    	case ID_EXIT:
    		finish();
    		return true;
    	}
		return super.onOptionsItemSelected(item);
	}    
    
    private final void startNewGame() {
        if( m_model.getGameStatus() != Model.GAME_ACTIVE ) {
            m_model.gameStart();
            m_ticker = new Ticker( this );
            m_ticker.start();
        }    	
    }
    
    private final void finish() {
    	// TODO: skip this method in the real Android application
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

	@Override
	public void keyTyped(KeyEvent e) {
		int code = e.getKeyCode();
	     switch( code ) {
         case KeyEvent.VK_UP:
             m_model.generateNewField( Model.MOVE_ROTATE ); 
             repaint(); return;
         case KeyEvent.VK_RIGHT:
             m_model.generateNewField( Model.MOVE_RIGHT ); 
             repaint(); return;
         case KeyEvent.VK_LEFT:
             m_model.generateNewField( Model.MOVE_LEFT ); 
             repaint(); return;
     }		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}

@SuppressWarnings("serial")
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
           
    @Override
    public void paint( Graphics gr ) {
        
        // show the background:
        gr.setColor( new Color( s_colorBG ) );
        
        Dimension size = getSize();
        gr.fillRect( 0, 0, size.width, size.width );   
        
        // show the field screen:
        m_scrField.paint( gr );
        // show the scores screen:
        m_scrScores.paint( gr );
    }
}

