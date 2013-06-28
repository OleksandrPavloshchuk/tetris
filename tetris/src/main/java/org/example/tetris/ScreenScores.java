/**
 * ScreenScores.java   screen for scores 
 */

package org.example.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

// TODO: replace it on real Android by TextView
public class ScreenScores extends ScreenBase {

	private ScoresCounter m_counter;

	public ScreenScores(ScoresCounter counter) {
		m_counter = counter;
	}

	@Override
	public void paint(Graphics g) {
		
		Point p = getLocation();
		
		g.setColor(new Color( 0x009900 ));
		String str = "Lines: " + m_counter.getLines() + " Scores: " + m_counter.getScores();
		g.drawString(str, p.x, p.y + 20 );
	}

	@Override
	protected double getTop() {
		return 0.05;
	}

	@Override
	protected double getLeft() {
		return 0.5;
	}

	@Override
	protected double getWidth() {
		return 0.4;
	}

	@Override
	protected double getHeight() {
		return 0.115;
	}
}
