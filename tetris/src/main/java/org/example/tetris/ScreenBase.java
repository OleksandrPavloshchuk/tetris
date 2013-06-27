/**
 * ScreenBase   base screen for ScreenScores and ScreenField
 */

package org.example.tetris;

// TODO: replace this "swing" graphics by Android graphics
import java.awt.Graphics;

public abstract class ScreenBase
{
    protected int m_nLeft;
    protected int m_nTop;
    protected int m_nWidth;
    protected int m_nHeight;
    protected int m_colorBG;
    protected int m_colorFG;
    
    protected ScreenBase( int nLeft, int nTop, int nWidth, int nHeight,
        int colorFG, int colorBG ) {
        m_nLeft = nLeft; m_nTop = nTop; m_nWidth = nWidth; m_nHeight = nHeight;
        m_colorFG = colorFG; m_colorBG = colorBG;
    }
    public abstract void paint( Graphics g );
}
