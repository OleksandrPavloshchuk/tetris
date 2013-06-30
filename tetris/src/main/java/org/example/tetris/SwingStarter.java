package org.example.tetris;

import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.example.gui.swing.Activity;

@SuppressWarnings("serial")
public class SwingStarter extends JFrame {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					new SwingStarter("tetris for swing", new Main() );
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});
	}
	
	private SwingStarter( String title, Activity activity ) {
		super( title );
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		
		setContentPane( activity );
		activity.onCreate(null);
		setSize( 400, 600 );
		if( KeyListener.class.isInstance(activity) ) {
			KeyListener l = KeyListener.class.cast(activity);
			addKeyListener(l);
		}
		setVisible(true);
	}
}
