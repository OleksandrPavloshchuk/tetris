/**
 * Main             main screen for Tetris game
 */

package org.example.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import org.example.gui.swing.Activity;
import org.example.gui.swing.Bundle;
import org.example.gui.swing.Canvas;
import org.example.gui.swing.Menu;
import org.example.gui.swing.MenuItem;

/**
 * Main class supports
 */
@SuppressWarnings("serial")
public class Main extends Activity {
	private static final int ID_NEW_GAME = Menu.FIRST + 2;
	private static final int ID_EXIT = Menu.FIRST + 3;

	private CanvasMain canvas;
	private Model model;
	private ScoresCounter counter;
	private Ticker ticker;
	
	public Main() {
		counter = new ScoresCounter();
		model = new Model(counter);
		canvas = new CanvasMain();		
	}

	@Override
	public void setCanvasSize(Dimension size) {
		canvas.setSize(size);
	}

	@Override
	public void onCreate(Bundle storedState) {
		super.onCreate(storedState);

		// TODO: swing related...
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// TODO: eliminate listeners on Android

		menu.add(0, ID_NEW_GAME, "New Game", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startNewGame();
			}
		});
		menu.add(1, ID_EXIT, "Exit", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getId()) {
		case ID_NEW_GAME:
			startNewGame();
			return true;
		case ID_EXIT:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private final void startNewGame() {
		if (!model.isGameActive()) {
			model.gameStart();
			ticker = new Ticker(this);
			ticker.start();
		}
	}

	private final void finish() {
		// TODO: skip this method in the real Android application
	}

	public Model getModel() {
		return model;
	}

	public ScoresCounter getScoresCounter() {
		return counter;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		switch (code) {
		case KeyEvent.VK_UP:
			model.generateNewField(Model.Move.ROTATE);
			repaint();
			return;
		case KeyEvent.VK_RIGHT:
			model.generateNewField(Model.Move.RIGHT);
			repaint();
			return;
		case KeyEvent.VK_LEFT:
			model.generateNewField(Model.Move.LEFT);
			repaint();
			return;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	private class CanvasMain extends Canvas {

		private final ScreenField screenField;
		private final ScreenScores screenScores;

		private CanvasMain() {
			super();
			screenField = new ScreenField(model);
			screenScores = new ScreenScores(counter);
		}

		@Override
		public void setSize(Dimension size) {
			super.setSize(size);
			screenField.setCanvasSize(size);
			screenScores.setCanvasSize(size);
		}

		@Override
		public void paint(Graphics gr) {

			// show the background:
			gr.setColor(Color.white);

			Dimension size = getSize();
			gr.fillRect(0, 0, size.width, size.width);

			// show the field screen:
			screenField.paint(gr);
			// show the scores screen:
			screenScores.paint(gr);
		}
	}
}