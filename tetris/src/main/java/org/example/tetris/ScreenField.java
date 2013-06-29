/**
 * ScreenField  visual presentation of the Tetris model
 */

package org.example.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class ScreenField extends ScreenBase {

	static private final int MARGIN = 2;
	private static final Color COLOR_BACKGROUND = new Color(0xeeffcc);

	private final Model model;
	private Dimension cellSize = null;

	public ScreenField(Model model) {
		this.model = model;
	}

	@Override
	public void setCanvasSize(Dimension canvasSize) {
		super.setCanvasSize(canvasSize);
		this.cellSize = calculateCellSize();
	}

	@Override
	protected double getTop() {
		return 0.125;
	}

	@Override
	protected double getLeft() {
		return 0.125;
	}

	@Override
	protected double getWidth() {
		return 0.75;
	}

	@Override
	protected double getHeight() {
		return 0.75;
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
		Point p = getLocation();
		int x = p.x - MARGIN;
		int y = p.y - MARGIN;
		final Dimension size = getSize();
		final int width = 2 * MARGIN + size.width;
		final int height = 2 * MARGIN + size.height;
		gr.setColor(COLOR_BACKGROUND);
		gr.fillRect(x, y, width, height);
		gr.setColor(new Color(0x336633));
		gr.drawRect(x, y, width, height);
	}

	private Dimension calculateCellSize() {
		Dimension size = getSize();
		int cellWidth = (size.width - (MARGIN << 1)) / Model.NUM_COLS;
		int cellHeight = (size.height - (MARGIN << 1)) / Model.NUM_ROWS;
		return new Dimension(cellWidth, cellHeight);
	}

	private void drawCell(Graphics gr, int row, int col) {

		byte nStatus = model.getCellStatus(row, col);

		Point p = getLocation();

		int x = col * cellSize.width + p.x;
		int y = row * cellSize.height + p.y;

		switch (nStatus) {

		case Block.CELL_EMPTY:
			gr.setColor(COLOR_BACKGROUND);
			gr.fillRect(x, y, cellSize.width, cellSize.height);
			break;
		case Block.CELL_DYNAMIC:
			drawCell(gr, x, y, model.getActiveBlockColor());
			break;
		default:
			drawCell(gr, x, y, Block.getColorForStaticValue(nStatus));
		}

	}

	private void drawCell(Graphics gr, int x, int y, Color colorFG) {
		gr.setColor(COLOR_BACKGROUND);
		gr.drawRect(x, y, cellSize.width, cellSize.height);
		gr.setColor(colorFG);
		gr.fillRect(x + 1, y + 1, cellSize.width - 1, cellSize.height - 1);
	}

}
