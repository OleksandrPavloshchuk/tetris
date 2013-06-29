/**
 * Main             main screen for Tetris game
 */

package org.example.tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.example.gui.swing.Activity;
import org.example.gui.swing.Bundle;
import org.example.gui.swing.Canvas;

@SuppressWarnings("serial")
public class Main extends Activity {

	private Ticker ticker;

	private JLabel scoresLabel = new JLabel();
	private JButton handleButton = new JButton("New Game");
	private final ScoresCounter counter = new ScoresCounter( scoresLabel );
	private final Model model = new Model(counter);
	private ScreenField screenField = new ScreenField( model );

	public Main() {
		setLayout(new BorderLayout());
		add(createHeader(), BorderLayout.NORTH );
		add(createCanvas(), BorderLayout.CENTER );
	}

	private JPanel createHeader() {
		JPanel result = new JPanel();
		result.setLayout(new BorderLayout());
		result.add(scoresLabel, BorderLayout.CENTER );
		handleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (model.isGameActive()) {
					// TODO: pause game
					handleButton.setText("Resume");
				} else {
					handleButton.setText("Pause");
					startNewGame();
				}
			}
		});
		result.add(handleButton, BorderLayout.WEST );
		
		JButton exitButton = new JButton( "Exit" );
		exitButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		result.add(exitButton, BorderLayout.EAST );
		

		return result;
	}
	
	private JPanel createCanvas() {
		JPanel result = new JPanel();
		
		result.setLayout( new BorderLayout() );
		result.add( screenField, BorderLayout.CENTER );
		
		JButton rotateButton = new JButton( "Rotate" );
		result.add( rotateButton, BorderLayout.NORTH );
		rotateButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.generateNewField(Model.Move.ROTATE);
				repaint();				}
		});
		
		JButton leftButton = new JButton( "Left" );
		result.add( leftButton, BorderLayout.WEST );
		leftButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.generateNewField(Model.Move.LEFT);
				repaint();				}
		});
		
		JButton rightButton = new JButton( "Right" );
		result.add( rightButton, BorderLayout.EAST );
		rightButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.generateNewField(Model.Move.RIGHT);
				repaint();				}
		});
		
		JButton downButton = new JButton( "Down" );
		result.add( downButton, BorderLayout.SOUTH );
		downButton.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.generateNewField(Model.Move.DOWN);
				repaint();				}
		});		
		
		return result;
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

}