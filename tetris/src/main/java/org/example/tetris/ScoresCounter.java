/**
 * ScoresCounter    counting the scores eraned in Tetris
 */

package org.example.tetris;

public class ScoresCounter
{
    private int m_nScores = 0;
    private int m_nLines = 0;
    private int m_nScoreDelta = 4;
    
    public void reset() {
        m_nScores = 0; m_nLines = 0;
    }
    public int getScores() { 
        return m_nScores; 
    }
    public int getLines() { 
        return m_nLines; 
    }    
    public void addScores() {
        m_nScores += m_nScoreDelta;
    }
    public void addLine() {
        m_nLines++;
    }
}
