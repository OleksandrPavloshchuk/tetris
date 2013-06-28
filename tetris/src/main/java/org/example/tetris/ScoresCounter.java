/**
 * ScoresCounter    counting the scores eraned in Tetris
 */

package org.example.tetris;

public class ScoresCounter
{
    private int scores = 0;
    private int lines = 0;
    private int scoreDelta = 4;
    
    public void reset() {
        scores = 0; lines = 0;
    }
    public int getScores() { 
        return scores; 
    }
    public int getLines() { 
        return lines; 
    }    
    public void addScores() {
        scores += scoreDelta;
    }
    public void addLine() {
        lines++;
    }
}
