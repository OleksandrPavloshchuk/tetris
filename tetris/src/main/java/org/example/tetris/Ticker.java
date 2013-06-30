/**
 * Ticker.java  refreshes the game screen with the specified speed
 */

package org.example.tetris;

import javax.swing.SwingWorker;

public class Ticker extends SwingWorker<Void, Model.Move> {

	private boolean pause = false;
	private Main action = null;
	private int delay = 400;

	Ticker(Main action) {
		this.action = action;
	}

	public void setDelay(int nDelay) {
		delay = nDelay;
	}

	public int getDelay() {
		return delay;
	}
	
	public void setPause( boolean pause ) {
		this.pause = pause;
	}

	@Override
	protected Void doInBackground() throws Exception {

		Model model = action.getModel();
		while (!model.isGameOver()) {
			if( isCancelled() ) {
				return null;
			}
			if( !pause ) {
				Thread.sleep(delay);
				action.doMove(Model.Move.DOWN);
			}
		}
		return null;
	}
}
