package org.example.tetris;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.example.gui.swing.Activity;
import org.example.gui.swing.Menu;

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
		
		final JPanel content = new JPanel();
		content.setLayout( new BorderLayout() );
		content.add(activity, BorderLayout.CENTER);
		
		Menu menu = new Menu();
		activity.onCreateOptionsMenu( menu );
		content.add(menu, BorderLayout.NORTH);
		
		setContentPane(content);
		activity.onCreate(null);
		setSize(Activity.CONTAINER_SIZE);
		setVisible(true);
	}
}