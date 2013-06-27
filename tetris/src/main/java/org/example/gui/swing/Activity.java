package org.example.gui.swing;

import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Activity extends JPanel {
  
	public static final Dimension CONTAINER_SIZE = new Dimension( 160, 200 );
	
	public Activity() {
		super();
	}
	
	public void onCreate( Bundle storedState ) {
		// For compatibility
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// For compatibility
		return true;
	}	
	
	public boolean onOptionsItemSelected( MenuItem menuItem ) {
		// For compatibility
		return true;
	}
}
