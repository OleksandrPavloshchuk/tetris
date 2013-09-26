package com.github.o.pavloshchuk.tetris;

import java.io.Serializable;

public class Dimension implements Serializable {
	private static final long serialVersionUID = 7387070051132481218L;
	
	private final int width;
	private final int height;
	
	public Dimension( int width, int height ) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	
}
