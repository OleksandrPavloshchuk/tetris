/**
 * ScreenBase   base screen for ScreenScores and ScreenField
 */

package org.example.tetris;

import java.awt.Dimension;
import java.awt.Point;
// TODO: replace this "swing" graphics by Android graphics
import java.awt.Graphics;

public abstract class ScreenBase {

	public abstract void paint(Graphics g);

	protected abstract double getTop();

	protected abstract double getLeft();

	protected abstract double getWidth();

	protected abstract double getHeight();

	protected int m_colorBG;
	protected int m_colorFG;

	private Dimension canvasSize = new Dimension(0,0);

	protected ScreenBase(int colorFG, int colorBG) {
		m_colorFG = colorFG;
		m_colorBG = colorBG;
	}

	protected final Point getLocation() {
		double x = getLeft() * canvasSize.width;
		double y = getTop() * canvasSize.height;
		
		Point result = new Point( (int) x, (int) y);
		
		// TODO: remove trace
		System.out.println( "(" + this + ") location = " + result );		
		
		return result;
	}

	protected final Dimension getSize() {
		double width = getWidth() * canvasSize.width;
		double height = getHeight() * canvasSize.height;
		Dimension result = new Dimension( (int) width, (int) height);
		
		// TODO: remove trace
		System.out.println( "(" + this + ") size = " + result );
		
		return result;
	}
	
	public void setCanvasSize( Dimension canvasSize ) {
		this.canvasSize = canvasSize;
	}

}
