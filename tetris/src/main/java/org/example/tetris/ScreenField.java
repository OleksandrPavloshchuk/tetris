/**
 * ScreenField  visual presentation of the Tetris model
 */

package org.example.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ScreenField extends JPanel {

	static private final int MARGIN = 2;
	private static final Color COLOR_BACKGROUND = new Color(0xeeffcc);

	private final Model model;

	public ScreenField(Model model) {
		this.model = model;
	}

	@Override
	public void paint(Graphics gr) {
		drawFrame(gr);
		
		// draw all the cells:
		for (int i = 0; i < Model.NUM_ROWS; i++) {
			for (int j = 0; j < Model.NUM_COLS; j++) {
				drawCell(gr, i, j);
			}
		}
		
	}

	private void drawFrame(Graphics gr) {
		Dimension size = getSize();
		gr.setColor(COLOR_BACKGROUND);
		gr.fillRect(0, 0, size.width, size.height);
	}

	private Dimension getCellSize() {
		Dimension size = getSize();
		int cellWidth = (size.width - (MARGIN << 1)) / Model.NUM_COLS;
		int cellHeight = (size.height - (MARGIN << 1)) / Model.NUM_ROWS;
		return new Dimension(cellWidth, cellHeight);
	}

	private void drawCell(Graphics gr, int row, int col) {

		byte nStatus = model.getCellStatus(row, col);

		Dimension cellSize = getCellSize();

		int x = col * cellSize.width ;
		int y = row * cellSize.height;
		
		if( Block.CELL_EMPTY!=nStatus ) {
			Color color = Block.CELL_DYNAMIC==nStatus ? model.getActiveBlockColor() :
				Block.getColorForStaticValue(nStatus);
			drawCell(gr, x, y, color);
		}
	}

	private void drawCell(Graphics gr, int x, int y, Color colorFG) {
		
		gr.setColor(COLOR_BACKGROUND);
		Dimension cellSize = getCellSize();		
		gr.drawRect(x, y, cellSize.width, cellSize.height);
		gr.setColor(colorFG);
		gr.fillRect(x + 1, y + 1, cellSize.width - 1, cellSize.height - 1);
	}

}
