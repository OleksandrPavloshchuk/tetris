package org.example.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Ticker extends Thread {
	
	private final Context context;
	private final SurfaceHolder holder;
	private GameScreen gameScreen;
	private boolean isRunning;
	private Canvas canvas;

	public Ticker( Context context, SurfaceHolder holder, GameScreen gameScreen ) {
		this.context = context;
		this.holder = holder;
		this.gameScreen = gameScreen;
		this.isRunning = false;
	}
	
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	@Override
	public void run() {
		super.run();
		while( isRunning ) {
			canvas = holder.lockCanvas();
			if( null!=canvas ) {
				gameScreen.doDraw( canvas );
				holder.unlockCanvasAndPost(canvas);
			}
			
		}
	}
}
