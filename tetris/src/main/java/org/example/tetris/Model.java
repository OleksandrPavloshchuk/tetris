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
		ACTIVE, SUSPENDED, OVER
	}

	// some constants in the model:
	public static final int NUM_COLS = 10; // number of columns in field
	public static final int NUM_ROWS = 20; // number of rows in field

	// game status constants:
	private GameStatus gameStatus = GameStatus.OVER;

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

	public byte getCellStatus(int nRow, int nCol) {
		return field[nRow][nCol];
	}

	private void setCellStatus(int nRow, int nCol, byte nStatus) {
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
		counter.reset();
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

	public synchronized void generateNewField(Move move) {

		if (!isGameActive()) {
			return;
		}
		Point newTopLeft = new Point(activeBlock.getTopLeft());
		int frame = activeBlock.getFrame();

		resetMovingBlock();

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
			frame = activeBlock.getNextFrame(frame);
			break;
		}
		if (isMoveValid(newTopLeft, frame)) {
			activeBlock.setState(frame, newTopLeft);
			return;
		}

		// Check the old block
		isMoveValid();

		if (Move.DOWN.equals(move)) {
			counter.addScores();
			if (!newBlock()) {
				setGameStatus(GameStatus.OVER);
				activeBlock = null;
				reset();
				return;
			}
		}
	}

	public interface FieldIterator {
		boolean processCell(int y, int x);
	}

	private abstract class EmptyCellSetter implements FieldIterator {

		protected abstract boolean doesSatisfy(byte value);

		@Override
		public final boolean processCell(int y, int x) {
			if (doesSatisfy(getCellStatus(y, x))) {
				setCellStatus(y, x, Block.CELL_EMPTY);
			}
			return true;
		}
	}

	public boolean iterateByField(FieldIterator iterator) {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				if (!iterator.processCell(i, j)) {
					return false;
				}
			}
		}
		return true;
	}

	public final void reset() {
		iterateByField(new EmptyCellSetter() {
			@Override
			protected boolean doesSatisfy(byte value) {
				return true;
			}
		});
	}

	public final void resetMovingBlock() {
		iterateByField(new EmptyCellSetter() {
			@Override
			protected boolean doesSatisfy(byte value) {
				return Block.CELL_DYNAMIC == value;
			}
		});
	}

	private static final boolean isValid(Point p) {
		return 0 <= p.x && 0 <= p.y;
	}

	private static final boolean isValid(Point p, byte[][] s) {
		return NUM_ROWS >= p.y + s.length && NUM_COLS >= p.x + s[0].length;
	}

	private final boolean isMoveValid() {
		return isMoveValid(activeBlock.getTopLeft(), activeBlock.getFrame());
	}

	private final boolean isMoveValid(Point newTopLeft, int frame) {

		synchronized (field) {
			byte[][] shape = activeBlock.getShape(frame);
			if (!isValid(newTopLeft)) {
				return false;
			}
			if (!isValid(newTopLeft, shape)) {
				return false;
			}

			// Check all the items in field:
			for (int i = 0; i < shape.length; i++) {
				for (int j = 0; j < shape[i].length; j++) {
					int y = newTopLeft.y + i;
					int x = newTopLeft.x + j;
					if (Block.CELL_EMPTY != shape[i][j]
							&& Block.CELL_EMPTY != field[y][x]) {
						return false;
					}
				}
			}

			// All cell is correct - add the data:
			for (int i = 0; i < shape.length; i++) {
				for (int j = 0; j < shape[i].length; j++) {
					int y = newTopLeft.y + i;
					int x = newTopLeft.x + j;
					if (Block.CELL_EMPTY != shape[i][j]) {
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
		iterateByField(new FieldIterator() {

			@Override
			public boolean processCell(int y, int x) {
				byte status = getCellStatus(y, x);
				if (Block.CELL_DYNAMIC == status) {
					status = activeBlock.getStatusValue();
					setCellStatus(y, x, status);
				}
				return true;
			}
		});

		for (int i = 0; i < field.length; i++) {
			boolean bFullRow = true;
			for (int j = 0; j < field[i].length; j++) {
				boolean isEmpty = Block.CELL_EMPTY == getCellStatus(i, j);
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
		if (!isMoveValid()) {
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
