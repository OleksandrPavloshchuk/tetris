package org.example.simpletetris.game;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = 8719795709864859393L;
	
	private int x;
	private int y;
	
	public Point( Point that ) {
		this( that.x, that.y );
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX( int x ) {
		this.x = x;
	}
	public void setY( int y ) {
		this.y = y;
	}

}
