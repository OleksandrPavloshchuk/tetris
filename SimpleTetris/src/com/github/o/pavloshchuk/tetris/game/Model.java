package com.github.o.pavloshchuk.tetris.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

public class Model implements Serializable {
	private static final long serialVersionUID = -166355688822702218L;

	public enum GameStatus {
		BEFORE_START, ACTIVE, PAUSED, OVER
	}

	public enum Move {
		LEFT, RIGHT, ROTATE, DOWN
	}

	private static final String TAG_DATA = "data";
	private static final String TAG_ACTIVE_BLOCK = "active-block";

	public static final int NUM_COLS = 10;
	public static final int NUM_ROWS = 20;

	private GameStatus gameStatus = GameStatus.BEFORE_START;

	// array of cell values:
	private final List<List<Integer>> field;

	private Block activeBlock = null;

	private ScoresCounter counter = null;
	private ScoresCounter highCounter = null;

	public Model() {
		field = createEmptyField();
	}

	private static final List<List<Integer>> createEmptyField() {
		final List<List<Integer>> result = new ArrayList<List<Integer>>(
				NUM_ROWS);
		for (int i = 0; i < NUM_ROWS; i++) {
			final List<Integer> row = new ArrayList<Integer>(NUM_COLS);
			for (int j = 0; j < NUM_COLS; j++) {
				row.add(Block.CELL_EMPTY);
			}
			result.add(row);
		}

		return result;
	}

	public void setCounter(ScoresCounter counter) {
		this.counter = counter;
	}

	public void setHighCounter(ScoresCounter counter) {
		this.highCounter = counter;
	}

	public boolean isGameActive() {
		return GameStatus.ACTIVE.equals(gameStatus);
	}

	public boolean isGameOver() {
		return GameStatus.OVER.equals(gameStatus);
	}

	public boolean isGameBeforeStart() {
		return GameStatus.BEFORE_START.equals(gameStatus);
	}

	public final int getCellStatus(final int row, final int col) {
		return field.get(row).get(col);
	}

	public void setCellStatus(final int row, final int col, final int status) {
		field.get(row).set(col, status);
	}

	public final boolean isCellDynamic(final int y, final int x) {
		return Block.CELL_DYNAMIC == getCellStatus(y, x);
	}

	public final boolean isCellEmpty(final int y, final int x) {
		return Block.CELL_EMPTY == getCellStatus(y, x);
	}

	public final void cleanCell(final int y, final int x) {
		setCellStatus(y, x, Block.CELL_EMPTY);
	}

	public synchronized void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void gameStart() {
		if (isGameActive()) {
			return;
		}
		setGameActive();
		cleanField();
		activeBlock = Block.createBlock();

	}

	public void setGameActive() {
		setGameStatus(GameStatus.ACTIVE);
	}

	public void setGamePaused() {
		setGameStatus(GameStatus.PAUSED);
	}

	public boolean isGamePaused() {
		return GameStatus.PAUSED.equals(gameStatus);
	}

	public synchronized void genereteNewField(Move move) {

		if (!isGameActive() || null == activeBlock) {
			return;
		}

		// get the parameters of block:
		Point newTopLeft = null;
		int nFrame = 0;
		if (null == activeBlock) {
			newTopLeft = new Point(0, 0);
		} else {
			newTopLeft = new Point(activeBlock.getTopLeft());
			nFrame = activeBlock.getFrame();
		}

		// Clear the old values:
		cleanDynamicData();

		// count new parameters:
		switch (move) {
		case LEFT:
			newTopLeft.setX(newTopLeft.getX() - 1);
			break;
		case RIGHT:
			newTopLeft.setX(newTopLeft.getX() + 1);
			break;
		case DOWN:
			newTopLeft.setY(newTopLeft.getY() + 1);
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
					// Game is over
					setGameStatus(GameStatus.OVER);

					activeBlock = null;
					// reset(false);
				}
			}

		} else {
			// Make the new move:
			activeBlock.setState(nFrame, newTopLeft);
		}
	}

	// ================================================
	// Helper functions:

	private final void cleanField() {
		iterateByCells(new CellProcessor() {
			@Override
			public boolean processCell(int y, int x) {
				cleanCell(y, x);
				return true;
			}
		});
	}

	public final void cleanDynamicData() {
		iterateByCells(new CellProcessor() {
			@Override
			public boolean processCell(int y, int x) {
				if (isCellDynamic(y, x)) {
					cleanCell(y, x);
				}
				return true;
			}
		});
	}

	private final boolean isMoveValid(Point newTopLeft, int nFrame) {
		synchronized (field) {
			int[][] shape = activeBlock.getShape(nFrame);

			if (newTopLeft.getY() < 0) {
				return false;
			}
			if (newTopLeft.getX() < 0) {
				return false;
			}
			if (newTopLeft.getY() + shape.length > NUM_ROWS) {
				return false;
			}
			if (newTopLeft.getX() + shape[0].length > NUM_COLS) {
				return false;
			}

			// Check all the items in field:
			for (int i = 0; i < shape.length; i++) {
				for (int j = 0; j < shape[i].length; j++) {
					int y = newTopLeft.getY() + i;
					int x = newTopLeft.getX() + j;
					if (Block.CELL_EMPTY != shape[i][j]
							&& Block.CELL_EMPTY != getCellStatus(y, x)) {
						return false;
					}
				}
			}

			// All cell is correct - add the data:
			for (int i = 0; i < shape.length; i++) {
				for (int j = 0; j < shape[i].length; j++) {
					int y = newTopLeft.getY() + i;
					int x = newTopLeft.getX() + j;
					if (Block.CELL_EMPTY != shape[i][j]) {
						setCellStatus(y, x, shape[i][j]);
					}
				}
			}
			return true;
		}
	}

	private synchronized boolean newBlock() {

		convertDynamicCellsToStatic();

		for (int i = 0; i < NUM_ROWS; i++) {
			boolean bFullRow = true;
			for (int j = 0; j < NUM_COLS; j++) {
				bFullRow &= ! isCellEmpty(i, j);
			}
			if (bFullRow) {
				shiftRows(i);

				// add lines to counter:
				counter.addLine();
			}
		}

		// Generate the new block:
		activeBlock = Block.createBlock();
		
		// If we can't place the newly created block, than game is over:  
		return isMoveValid(activeBlock.getTopLeft(), activeBlock.getFrame());
	}

	private void convertDynamicCellsToStatic() {
		final int blockStatus = activeBlock.getStaticValue();
		
		iterateByCells( new CellProcessor() {
			
			@Override
			public boolean processCell(int y, int x) {
				if( isCellDynamic(y, x) ) {
					setCellStatus(y, x, blockStatus);
				}				
				return true;
			}
		});
	}

	private synchronized final void shiftRows(int nToRow) {
		if (nToRow > 0) {
			for (int j = nToRow - 1; j >= 0; j--) {
				for (int m = 0; m < NUM_COLS; m++) {
					setCellStatus(j + 1, m, getCellStatus(j, m));
				}
			}
		}
		for (int j = 0; j < NUM_COLS; j++) {
			setCellStatus(0, j, Block.CELL_EMPTY);
		}
	}

	public int getActiveBlockResourceId() {
		return activeBlock.getResourceId();
	}

	public void storeTo(Bundle bundle) {
		bundle.putSerializable(TAG_ACTIVE_BLOCK, activeBlock);
		bundle.putIntArray(TAG_DATA, getIntArrayFromData());
	}

	public void restoreFrom(Bundle bundle) {
		activeBlock = Block.class
				.cast(bundle.getSerializable(TAG_ACTIVE_BLOCK));
		restoreDataFromIntArray(bundle.getIntArray(TAG_DATA));
	}

	private void restoreDataFromIntArray(int[] src) {
		if (null == src) {
			return;
		}
		for (int k = 0; k < src.length; k++) {
			int i = k / NUM_COLS;
			int j = k % NUM_COLS;
			setCellStatus(i, j, src[k]);
		}
	}

	private int[] getIntArrayFromData() {
		int[] result = new int[NUM_COLS * NUM_ROWS];
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				result[NUM_COLS * i + j] = getCellStatus(i, j);
			}
		}
		return result;
	}

	private boolean iterateByCells(CellProcessor cellProcessor) {
		synchronized (field) {
			for (int y = 0; y < NUM_ROWS; y++) {
				for (int x = 0; x < NUM_COLS; x++) {
					if (!cellProcessor.processCell(y, x)) {
						return false;
					}
				}
			}
			return false;
		}
	}

	private interface CellProcessor {
		boolean processCell(final int y, final int x);
	}

}
