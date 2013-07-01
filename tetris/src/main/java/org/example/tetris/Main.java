/**
 * Main             main screen for Tetris game
 */

package org.example.tetris;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.example.gui.swing.Activity;
import org.example.gui.swing.Bundle;

@SuppressWarnings("serial")
public class Main extends Activity {

	private Ticker ticker;

	private final JLabel scoresLabel = new JLabel();
	private JButton handleButton;
	private final ScoresCounter counter = new ScoresCounter(scoresLabel);
	private final Model model = new Model(counter);
	private final ScreenField screenField = new ScreenField(model);

	public Main() {
		setLayout(new BorderLayout());
		add(createHeader(), BorderLayout.NORTH);
		add(createCanvas(), BorderLayout.CENTER);
	}

	private JPanel createHeader() {
		JPanel result = new JPanel();
		result.setLayout(new BorderLayout());
		result.add(scoresLabel, BorderLayout.CENTER);
		result.add(
				handleButton = createButton("New Game", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						if (model.isGameActive()) {
							model.setGamePaused();
							handleButton.setText("Resume");
						} else if (model.isGamePaused()) {
							model.setGameActive();
							handleButton.setText("Pause");
						} else {
							handleButton.setText("Pause");
							startNewGame();
						}
					}
				}), BorderLayout.WEST);
		result.add(createButton("Exit", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (null != ticker) {
					ticker.cancel(true);
				}
				finish();
				System.exit(0);
			}
		}), BorderLayout.EAST);

		return result;
	}

	private JPanel createCanvas() {
		JPanel result = new JPanel();

		result.setLayout(new BorderLayout());
		result.add(screenField, BorderLayout.CENTER);

		result.add(createButton("Rotate", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doMove(Model.Move.ROTATE);
			}
		}), BorderLayout.NORTH);

		result.add(createButton("Left", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doMove(Model.Move.LEFT);
			}
		}), BorderLayout.WEST);

		result.add(createButton("Right", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doMove(Model.Move.RIGHT);
			}
		}), BorderLayout.EAST);

		result.add(createButton("Down", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doMove(Model.Move.DOWN);
			}
		}), BorderLayout.SOUTH);

		return result;
	}

	private static JButton createButton(String text, ActionListener l) {
		final JButton result = new JButton(text);
		result.addActionListener(l);
		return result;
	}

	void doMove(Model.Move move) {
		model.generateNewField(move);
		if (model.isGameActive()) {
			screenField.invalidate();
			scoresLabel.invalidate();
			repaint();
		} else if (model.isGameOver()) {
			JOptionPane.showMessageDialog(this, "GAME OVER!");

			// TODO: show "game over" message
			ticker = null;
			handleButton.setText("New Game");
		}
	}

	@Override
	public void onCreate(Bundle storedState) {
		super.onCreate(storedState);
	}

	private final void startNewGame() {
		if (!model.isGameActive()) {
			model.gameStart();
			// TODO: use android related handler and messages
			ticker = new Ticker(this);
			ticker.execute();
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

	public void keyPressed(KeyEvent event) {
		if (!model.isGameActive()) {
			return;
		}

		int keyCode = event.getKeyCode();

		switch (keyCode) {
		case KeyEvent.VK_UP:
			doMove(Model.Move.ROTATE);
			break;
		case KeyEvent.VK_LEFT:
			doMove(Model.Move.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			doMove(Model.Move.RIGHT);
			break;
		case KeyEvent.VK_DOWN:
			doMove(Model.Move.DOWN);
			break;
		}
	}

}
