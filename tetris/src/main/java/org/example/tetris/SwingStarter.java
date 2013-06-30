package org.example.tetris;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

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
					new SwingStarter("tetris for swing", new Main());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});
	}

	private SwingStarter(String title, Activity activity) {
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setContentPane(activity);
		addKeyListener(activity);
		activity.onCreate(null);
		setSize(400, 600);
		bindKeyboardEventListner(activity);
		setVisible(true);
	}

	private void bindKeyboardEventListner(final Activity activity) {
		long eventMask = AWTEvent.KEY_EVENT_MASK;

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			public void eventDispatched(AWTEvent event) {
				if( !KeyEvent.class.isInstance(event) ) {
					return;
				}
				KeyEvent keyEvent = KeyEvent.class.cast(event);		
				if( KeyEvent.KEY_PRESSED!=keyEvent.getID() ) {
					return;
				}				
				activity.keyPressed(keyEvent);
			}
		}, eventMask);
	}
}
