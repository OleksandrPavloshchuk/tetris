/**
 * Block.java   single Tetris block description
 */

package org.example.tetris;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

public class Block {

	public enum BlockColor {
		RED(0x990000, (byte) 2), GREEN(0x009900, (byte) 3), BLUE(0x000099,
				(byte) 4), YELLOW( 0xcccc33, (byte) 5 );
		private final Color color;
		private final byte value;

		private BlockColor(int color, byte value) {
			this.color = new Color(color);
			this.value = value;
		}
	}

	// cell status values (inner):
	private static final byte _N = 0;
	private static final byte _D = 1;
	// cell status values (outer):
	public static final byte CELL_EMPTY = _N;
	public static final byte CELL_DYNAMIC = _D;

	private static Random random = new Random();

	// current block state:
	private int shape = 0;
	private int frame = 0;
	private Point topLeft = new Point(Model.NUM_COLS >> 1, 0);
	private BlockColor color;

	public int getFrame() {
		return frame;
	}

	public Color getColor() {
		return color.color;
	}

	public byte getStaticValue() {
		return color.value;
	}

	public static Color getColorForStaticValue(byte b) {
		for (BlockColor item : BlockColor.values()) {
			if (b == item.value) {
				return item.color;
			}
		}
		return null;
	}

	public final void setState(int frame, Point topLeft) {
		this.frame = frame;
		this.topLeft = topLeft;
	}

	public final int getFramesCount() {
		return SHAPES[shape].length;
	}

	public final byte[][] getShape(int nFrame) {
		return SHAPES[shape][nFrame];
	}

	public final int getShapeWidth(int nFrame) {
		return SHAPES[shape][nFrame][0].length;
	}

	public final static synchronized Block createBlock() {
		// generate random number:
		int indexShape = random.nextInt(SHAPES.length);
		BlockColor blockColor = BlockColor.values()[random.nextInt(BlockColor
				.values().length)];
		return new Block(indexShape, blockColor);
	}

	private Block(int nShape, BlockColor blockColor) {
		shape = nShape;
		this.color = blockColor;
	}
	
	public Point getTopLeft() {
		return this.topLeft;
	}	

	// Shapes data:
	private static final byte[][][][] SHAPES = { { { { _D, _D }, // [][]
			{ _D, _D } } // [][]
			}, { { { _D, _D, _D, _D } // [][][][]
					}, { { _D }, // []
							{ _D }, // []
							{ _D }, // []
							{ _D } } // []
			}, { { { _N, _D, _D }, // [][]
					{ _D, _D, _N } // [][]
					}, { { _D, _N }, // []
							{ _D, _D }, // [][]
							{ _N, _D } } // []
			}, { { { _D, _D, _N }, // [][]
					{ _N, _D, _D } // [][]
					}, { { _N, _D }, // []
							{ _D, _D }, // [][]
							{ _D, _N } } // []
			}, { { { _N, _N, _D }, // []
					{ _D, _D, _D } // [][][]
					}, { { _D, _N }, // []
							{ _D, _N }, // []
							{ _D, _D } // [][]
					}, { { _D, _D, _D }, // [][][]
							{ _D, _N, _N } // []
					}, { { _D, _D }, // [][]
							{ _N, _D }, // []
							{ _N, _D } } // []
			}, { { { _D, _N, _N }, // []
					{ _D, _D, _D } // [][][]
					}, { { _D, _D }, // [][]
							{ _D, _N }, // []
							{ _D, _N } // []
					}, { { _D, _D, _D }, // [][][]
							{ _N, _N, _D } // []
					}, { { _N, _D }, // []
							{ _N, _D }, // []
							{ _D, _D } } // [][]
			}, { { { _N, _D, _N }, // []
					{ _D, _D, _D } // [][][]
					}, { { _D, _N }, // []
							{ _D, _D }, // [][]
							{ _D, _N } // []
					}, { { _D, _D, _D }, // [][][]
							{ _N, _D, _N } // []
					}, { { _N, _D }, // []
							{ _D, _D }, // [][]
							{ _N, _D } } // []
			} };
}
