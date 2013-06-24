/**
 * ScreenScores.java   screen for scores 
 *
 * @author      Alex Pavloshchuk
 * @version     0.3a October 2, 2002
 */

package org.alexp.tetris;

import javax.microedition.lcdui.Graphics;

public class ScreenScores extends ScreenBase {
    
    private ScoresCounter m_counter;

    public ScreenScores( ScoresCounter counter,
        int nLeft, int nTop, int nWidth, int nHeight, int colorFG, int colorBG ) 
    {  
        super( nLeft, nTop, nWidth, nHeight, colorFG, colorBG );
        m_counter = counter;
    }
  
    public void paint( Graphics g ) {
        g.setColor( m_colorFG );
        
        String str = "Lines: " + m_counter.getLines();
        g.drawString( str, m_nLeft, m_nTop, Graphics.TOP|Graphics.LEFT );
        str = "Scores: " + m_counter.getScores();
        g.drawString( str, m_nLeft, m_nTop+10, Graphics.TOP|Graphics.LEFT );        
    }
}
