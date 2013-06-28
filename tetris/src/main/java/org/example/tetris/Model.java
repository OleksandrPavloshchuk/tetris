/**
 * Model.java   algorithmes of Tetris separated from visual presentation
 */
package org.example.tetris;

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

	// Game status:
	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus( GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	// Start the game:
	public void gameStart() {
		if( isGameActive() ) {
			return;
		}
		setGameStatus( GameStatus.ACTIVE );
		activeBlock = Block.createBlock();
	}

	/**
	 * Create and check the array of new data:
	 */
	public synchronized void generateNewField(Move move) {
		if( !isGameActive()) {
			return;
		}

		// get the parameters of block:
		int nX = activeBlock.getLeftX();
		int nY = activeBlock.getTopY();
		int nFrame = activeBlock.getFrame();

		// Clear the old values:
		reset(true);

		// count new parameters:
		switch (move) {
		case LEFT:
			nX--;
			break;
		case RIGHT:
			nX++;
			break;
		case DOWN:
			nY++;
			break;
		case ROTATE:
			nFrame++;
			if (nFrame >= activeBlock.getFramesCount())
				nFrame = 0;
			break;
		}
		if (!isMoveValid(nY, nX, nFrame)) {
			
			// set old the block:
			isMoveValid(activeBlock.getTopY(), activeBlock.getLeftX(),
					activeBlock.getFrame());
			
			if( Move.DOWN.equals(move) ) {

				// add the scores:
				counter.addScores();

				if (!newBlock()) {
					setGameStatus( GameStatus.OVER );
					activeBlock = null;
					reset(false);
					return;
				}
			}

			return;
		} else {
			// Make the new move:
			activeBlock.setState(nFrame, nY, nX);
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

	/**
	 * Check the movement validity
	 * 
	 * @return true - movement is OK, false - some error
	 */
	private final boolean isMoveValid(int nY, int nX, int nFrame) {
		synchronized (field) {
			byte[][] shape = activeBlock.getShape(nFrame);

			// Check coords first:
			if (nY < 0)
				return false;
			if (nX < 0)
				return false;
			if (nY + shape.length > NUM_ROWS)
				return false;
			if (nX + shape[0].length > NUM_COLS)
				return false;

			// Check all the items in field:
			for (int i = 0; i < shape.length; i++) {
				for (int j = 0; j < shape[i].length; j++) {
					if (shape[i][j] + field[i + nY][j + nX] > Block.CELL_DYNAMIC)
						return false;
				}
			}

			// All cell is correct - add the data:
			for (int i = 0; i < shape.length; i++) {
				for (int j = 0; j < shape[i].length; j++) {
					field[i + nY][j + nX] += shape[i][j];
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
				if (getCellStatus(i, j) == Block.CELL_DYNAMIC) {
					setCellStatus(i, j, Block.CELL_STATIC);
				}
			}
		}

		for (int i = 0; i < field.length; i++) {
			boolean bFullRow = true;
			for (int j = 0; j < field[i].length; j++) {
				bFullRow &= (getCellStatus(i, j) == Block.CELL_STATIC);
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
		if (!isMoveValid(activeBlock.getTopY(), activeBlock.getLeftX(),
				activeBlock.getFrame())) {
			// GAME IS OVER!
			counter.reset();
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
}
