/**
 * ScoresCounter    counting the scores eraned in Tetris
 */

package org.example.tetris;

import javax.swing.JLabel;

public class ScoresCounter {
	private int scores = 0;
	private int lines = 0;
	private int scoreDelta = 4;
	
	private final JLabel status;
	
	public ScoresCounter( JLabel status ) {
		this.status = status;
	}

	public void reset() {
		scores = 0;
		lines = 0;
		updateStatus();
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
		status.setText( String.format( " Lines: %d Scores: %d", lines, scores));
	}	
}
