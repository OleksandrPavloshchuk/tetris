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
	private final JButton handleButton = new JButton("New Game");
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
		handleButton.addActionListener(new ActionListener() {
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
		});
		result.add(handleButton, BorderLayout.WEST);

		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (null != ticker) {
					ticker.cancel(true);
				}
				finish();
				System.exit(0);
			}
		});
		result.add(exitButton, BorderLayout.EAST);

		return result;
	}

	private JPanel createCanvas() {
		JPanel result = new JPanel();

		result.setLayout(new BorderLayout());
		result.add(screenField, BorderLayout.CENTER);

		JButton rotateButton = new JButton("Rotate");
		result.add(rotateButton, BorderLayout.NORTH);
		rotateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doMove(Model.Move.ROTATE);
			}
		});

		JButton leftButton = new JButton("Left");
		result.add(leftButton, BorderLayout.WEST);
		leftButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doMove(Model.Move.LEFT);
			}
		});

		JButton rightButton = new JButton("Right");
		result.add(rightButton, BorderLayout.EAST);
		rightButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doMove(Model.Move.RIGHT);
			}
		});

		JButton downButton = new JButton("Down");
		result.add(downButton, BorderLayout.SOUTH);
		downButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				doMove(Model.Move.DOWN);
			}
		});

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
		if( !model.isGameActive() ) {
			return;
		}
		
		int keyCode = event.getKeyCode();
		
		switch( keyCode ) {
		case KeyEvent.VK_UP:
			doMove(Model.Move.ROTATE); break;
		case KeyEvent.VK_LEFT:	
			doMove(Model.Move.LEFT); break;
		case KeyEvent.VK_RIGHT:	
			doMove(Model.Move.RIGHT); break;
		case KeyEvent.VK_DOWN:	
			doMove(Model.Move.DOWN); break;
		}
	}

}
