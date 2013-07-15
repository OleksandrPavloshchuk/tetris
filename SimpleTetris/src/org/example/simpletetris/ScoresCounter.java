/**
 * ScoresCounter    counting the scores eraned in Tetris
 */

package org.example.simpletetris;

import android.widget.TextView;

public class ScoresCounter {
	private int scores = 0;
	private int lines = 0;
	private int scoreDelta = 4;
	
	private final TextView status;
	
	public ScoresCounter( TextView status ) {
		this.status = status;
	}

	public void reset() {
		scores = 0;
		lines = 0;
		updateStatus();
	}

	public int getScores() {
		return scores;
	}

	public int getLines() {
		return lines;
	}

	public void addScores() {
		scores += scoreDelta;
		updateStatus();
	}

	public void addLine() {
		lines++;
		updateStatus();
	}
	

	private void updateStatus() {
		status.setText( String.format( "Lines: %d Scores: %d", lines, scores));
	}	
}
