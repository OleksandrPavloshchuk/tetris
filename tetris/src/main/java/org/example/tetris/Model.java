/**
 * Model.java   algorithmes of Tetris separated from visual presentation
 */
package org.example.tetris;

import java.awt.Color;
import java.awt.Point;

public class Model {

	public enum Move {
		LEFT, RIGHT, DOWN, ROTATE
	}

	public enum GameStatus {
		EMPTY, ACTIVE, SUSPENDED, OVER
	}

	// some constants in the model:
	public static final int NUM_COLS = 10; // number of columns in field
	public static final int NUM_ROWS = 20; // number of rows in field

	// game status constants:
	private GameStatus gameStatus = GameStatus.EMPTY;

	// array of cell values:
	private byte[][] field = null;

	// active block:
	private Block activeBlock = null;

	// scores counter:
	private ScoresCounter counter = null;

	public Model(ScoresCounter counter) {
		field = new byte[NUM_ROWS][NUM_COLS];
		this.counter = counter;
	}

	public boolean isGameActive() {
		return GameStatus.ACTIVE.equals(gameStatus);
	}

	public boolean isGameOver() {
		return GameStatus.OVER.equals(gameStatus);
	}

	public void reset() {
		reset(false); // call the inner method - reset the all data
	}

	public byte getCellStatus(int nRow, int nCol) {
		return field[nRow][nCol];
	}

	public void setCellStatus(int nRow, int nCol, byte nStatus) {
		field[nRow][nCol] = nStatus;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	// Start the game:
	public void gameStart() {
		if (isGameActive()) {
			return;
		}
		setGameActive();
		activeBlock = Block.createBlock();
	}

	public void setGameActive() {
		setGameStatus(GameStatus.ACTIVE);
	}
	
	public void setGamePaused() {
		setGameStatus(GameStatus.SUSPENDED);
	}

	public boolean isGamePaused() {
		return GameStatus.SUSPENDED.equals(gameStatus);
	}	

	/**
	 * Create and check the array of new data:
	 */
	public synchronized void generateNewField(Move move) {	
		
		if (!isGameActive()) {
			return;
		}

		// get the parameters of block:
		Point newTopLeft = new Point(activeBlock.getTopLeft());
		int nFrame = activeBlock.getFrame();

		// Clear the old values:
		reset(true);

		// count new parameters:
		switch (move) {
		case LEFT:
			newTopLeft.x--;
			break;
		case RIGHT:
			newTopLeft.x++;
			break;
		case DOWN:
			newTopLeft.y++;
			break;
		case ROTATE:
			nFrame++;
			if (nFrame >= activeBlock.getFramesCount())
				nFrame = 0;
			break;
		}
		if (!isMoveValid(newTopLeft, nFrame)) {

			// set old the block:
			isMoveValid(activeBlock.getTopLeft(), activeBlock.getFrame());

			if (Move.DOWN.equals(move)) {

				// add the scores:
				counter.addScores();

				if (!newBlock()) {
					setGameStatus(GameStatus.OVER);
					activeBlock = null;
					reset(false);
					return;
				}
			}

			return;
		} else {
			// Make the new move:
			activeBlock.setState(nFrame, newTopLeft);
		}
	}

	// ================================================
	// Helper functions:

	/**
	 * Reset the field data:
	 * 
	 * @param true - clear only dynamic data, false - clear all the data
	 */
	private final void reset(boolean bDynamicDataOnly) {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				if (!bDynamicDataOnly || field[i][j] == Block.CELL_DYNAMIC) {
					field[i][j] = Block.CELL_EMPTY;
				}
			}
		}
	}

	private final boolean isMoveValid(Point newTopLeft, int nFrame) {
		synchronized (field) {
			byte[][] shape = activeBlock.getShape(nFrame);

			if (newTopLeft.y < 0) {
				return false;
			}
			if (newTopLeft.x < 0) {
				return false;
			}
			if (newTopLeft.y + shape.length > NUM_ROWS) {
				return false;
			}
			if (newTopLeft.x + shape[0].length > NUM_COLS) {
				return false;
			}

			// Check all the items in field:
			for (int i = 0; i < shape.length; i++) {
				for (int j = 0; j < shape[i].length; j++) {
					int y = newTopLeft.y + i;
					int x = newTopLeft.x + j;
					if( Block.CELL_EMPTY!=shape[i][j] && Block.CELL_EMPTY!=field[y][x]) {
						return false;
					}
				}
			}

			// All cell is correct - add the data:
			for (int i = 0; i < shape.length; i++) {
				for (int j = 0; j < shape[i].length; j++) {
					int y = newTopLeft.y + i;
					int x = newTopLeft.x + j;
					if( Block.CELL_EMPTY!=shape[i][j]) {
						field[y][x] = shape[i][j];
					}
				}
			}
			return true;
		}
	}

	/**
	 * Create the new block:
	 * 
	 * @return true - block can be generated,
	 * @return false - can't generate the block - GAME OVER!
	 */
	private synchronized boolean newBlock() {

		// set all the dynamic data as static:
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				byte status = getCellStatus(i, j);
				if (status == Block.CELL_DYNAMIC) {
					status = activeBlock.getStaticValue();
					setCellStatus(i, j, status);
				}
			}
		}

		for (int i = 0; i < field.length; i++) {
			boolean bFullRow = true;
			for (int j = 0; j < field[i].length; j++) {
				byte status = getCellStatus(i, j);
				boolean isEmpty = Block.CELL_EMPTY == status;
				bFullRow &= !isEmpty;
			}
			if (bFullRow) {
				shiftRows(i);

				// add lines to counter:
				counter.addLine();
			}
		}

		// Generate the new block:
		activeBlock = Block.createBlock();

		// Check the validity of new block:
		if (!isMoveValid(activeBlock.getTopLeft(), activeBlock.getFrame())) {
			// GAME IS OVER!
			return false;
		}
		return true;
	}

	private synchronized final void shiftRows(int nToRow) {
		if (nToRow > 0) {
			for (int j = nToRow - 1; j >= 0; j--) {
				for (int m = 0; m < field[j].length; m++) {
					setCellStatus(j + 1, m, getCellStatus(j, m));
				}
			}
		}
		for (int j = 0; j < field[0].length; j++) {
			setCellStatus(0, j, Block.CELL_EMPTY);
		}
	}

	public Color getActiveBlockColor() {
		return activeBlock.getColor();
	}
}
