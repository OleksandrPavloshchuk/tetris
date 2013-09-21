package com.github.o.pavloshchuk.tetris.game;

import java.io.Serializable;

import android.os.Bundle;
import android.widget.TextView;

public class ScoresCounter implements Serializable {
	private static final long serialVersionUID = -1761429120586168951L;
	
	private static final String TAG_SCORES = "scores";
	private static final String TAG_LINES = "lines";
	
	private int scores = 0;
	private int lines = 0;
	private int scoreDelta = 4;
	
	private final TextView status;
	private final String format;
	
	public ScoresCounter( TextView status, String format ) {
		this.status = status;
		this.format = format;
	}

	public void reset() {
		scores = 0;
		lines = 0;
		updateView();
	}

	public int getScores() {
		return scores;
	}

	public int getLines() {
		return lines;
	}
	
	public void setLines( int lines ) {
		this.lines = lines;
	}
	
	public void setScores( int scores ) {
		this.scores = scores;
	}

	public void addScores() {
		scores += scoreDelta;
		updateView();
	}

	public void addLine() {
		lines++;
		updateView();
	}
	

	public void updateView() {
		status.setText( String.format( format, lines, scores));
	}

	public void storeTo(Bundle bundle) {
		bundle.putInt(TAG_LINES, lines);
		bundle.putInt(TAG_SCORES, scores);
	}

	public void restoreFrom(Bundle bundle) {
		this.lines = bundle.getInt(TAG_LINES);
		this.scores = bundle.getInt(TAG_SCORES);
	}	
}
