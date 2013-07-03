package org.example.gui.swing;

import java.awt.event.KeyListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Activity extends JPanel implements KeyListener {
	
	public Activity() {
		super();
	}
	
	public void onCreate( Bundle storedState ) {
		// For compatibility
	}
}
