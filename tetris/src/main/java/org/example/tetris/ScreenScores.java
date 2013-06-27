/**
 * ScreenScores.java   screen for scores 
 */

package org.example.tetris;

import java.awt.Color;
import java.awt.Graphics;

public class ScreenScores extends ScreenBase {
    
    private ScoresCounter m_counter;

    public ScreenScores( ScoresCounter counter,
        int nLeft, int nTop, int nWidth, int nHeight, int colorFG, int colorBG ) 
    {  
        super( nLeft, nTop, nWidth, nHeight, colorFG, colorBG );
        m_counter = counter;
    }
  
    @Override
    public void paint( Graphics g ) {
        g.setColor( new Color( m_colorFG ) );
        
        String str = "Lines: " + m_counter.getLines();
        g.drawString( str, m_nLeft, m_nTop );
        str = "Scores: " + m_counter.getScores();
        g.drawString( str, m_nLeft, m_nTop+10 );        
    }
}
