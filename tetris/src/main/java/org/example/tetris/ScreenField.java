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
		drawCells(gr);
	}

	private void drawCells(final Graphics gr) {
		model.iterateByField(new Model.FieldIterator() {
			@Override
			public boolean processCell(int y, int x) {
				drawCell(gr, y, x);
				return true;
			}
		});
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
		final byte status = model.getCellStatus(row, col);
		if (Block.CELL_EMPTY == status) {
			return;
		}
		Dimension cellSize = getCellSize();

		int x = col * cellSize.width;
		int y = row * cellSize.height;
		drawCell(gr, x, y, getColorFor(status));
	}

	private Color getColorFor(final byte status) {
		return Block.CELL_DYNAMIC == status ? model
				.getActiveBlockColor() : Block.getColorForStaticValue(status);
	}

	private void drawCell(Graphics gr, int x, int y, Color colorFG) {
		Dimension cellSize = getCellSize();
		gr.setColor(colorFG);
		gr.fillRoundRect(x + 1, y + 1, cellSize.width - 1, cellSize.height - 1,
				2, 2);
	}

}
