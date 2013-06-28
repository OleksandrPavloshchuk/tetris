/**
 * Ticker.java  refreshes the game screen with the specified speed
 */

package org.example.tetris;

import org.example.gui.swing.Canvas;

public class Ticker extends Thread {
	private Main m_app = null;
	private int m_nDelay = 400;

	Ticker(Main app) {
		m_app = app;
	}

	@Override
	public void run() {
		try {
			Model model = m_app.getModel();
			Canvas canvas = m_app.getCanvas();
			while (!model.isGameOver()) {
				synchronized (model) {
					if (!model.isGameActive()) {
						continue;
					}

					sleep(m_nDelay);
					model.generateNewField(Model.Move.DOWN);
					canvas.repaint();
				}
			}
		} catch (InterruptedException ex) {
		}
	}

	public void setDelay(int nDelay) {
		m_nDelay = nDelay;
	}

	public int getDelay() {
		return m_nDelay;
	}
}
