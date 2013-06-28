/**
 * ScreenScores.java   screen for scores 
 */

package org.example.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class ScreenScores extends ScreenBase {

	private ScoresCounter m_counter;

	public ScreenScores(ScoresCounter counter, int colorFG, int colorBG) {
		super(colorFG, colorBG);
		m_counter = counter;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(m_colorFG));
		
		Point p = getLocation();

		String str = "Lines: " + m_counter.getLines() + " Scores: " + m_counter.getScores();
		g.drawString(str, p.x, p.y);
	}

	@Override
	protected double getTop() {
		return 0.015;
	}

	@Override
	protected double getLeft() {
		return 0.1;
	}

	@Override
	protected double getWidth() {
		return 0.8;
	}

	@Override
	protected double getHeight() {
		return 0.11;
	}
}
