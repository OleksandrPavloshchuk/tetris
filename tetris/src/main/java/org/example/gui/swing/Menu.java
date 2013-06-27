package org.example.gui.swing;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class Menu extends JMenuBar {
  public static final int FIRST = 100;

	private JMenu contextMenu = new JMenu("...");

	public Menu() {
		super();
		add(contextMenu);
	}

	public void add(int order, int id, String text,
			ActionListener actionListener) {
		final JMenuItem menuItem = new JMenuItem(text);
		menuItem.addActionListener(actionListener);
		contextMenu.add(menuItem);
	}
}
