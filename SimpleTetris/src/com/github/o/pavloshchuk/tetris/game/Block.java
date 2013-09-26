package com.github.o.pavloshchuk.tetris.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.o.pavloshchuk.tetris.R;

public class Block implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum BlockColor {
		RED(R.drawable.red_block, 2), GREEN(R.drawable.green_block, 3), BLUE(
				R.drawable.blue_block, 4), YELLOW(R.drawable.yellow_block, 5), CYAN(
				R.drawable.cyan_block, 6), VIOLET(R.drawable.violet_block, 7);

		private final int value;
		private final int resourceId;

		private BlockColor(int resourceId, int value) {
			this.resourceId = resourceId;
			this.value = value;
		}
	}

	// cell status values
	public static final int CELL_EMPTY = 0;
	public static final int CELL_DYNAMIC = 1;

	private static Random random = new Random( System.currentTimeMillis());

	// current block state:
	private int shape = 0;
	private int frame = 0;
	private Point topLeft = new Point(Model.NUM_COLS / 2, 0);
	private BlockColor color;

	public int getFrame() {
		return frame;
	}

	public int getResourceId() {
		return color.resourceId;
	}

	public int getColor() {
		return color.value;
	}

	public static int getResourceIdForStaticValue(int n) {
		for (BlockColor item : BlockColor.values()) {
			if (n == item.value) {
				return item.resourceId;
			}
		}
		return -1; // color is not found
	}

	public final void setState(int frame, Point topLeft) {
		this.frame = frame;
		this.topLeft = topLeft;
	}

	public final int getFramesCount() {
		return Shape.values()[shape].frames.size();
	}

	public final int[][] getShape(int nFrame) {
		return Shape.values()[shape].getFrame(nFrame).get();
	}

	public final int getShapeWidth(int nFrame) {
		return Shape.values()[shape].getFrame(nFrame).width;
	}

	public final static synchronized Block createBlock() {
		// generate random number:
		int indexShape = random.nextInt(Shape.values().length);
		BlockColor blockColor = BlockColor.values()[random.nextInt(BlockColor
				.values().length)];
		Block result = new Block(indexShape, blockColor);
		// Set to the middle
		result.topLeft.setX(result.topLeft.getX()
				- Shape.values()[indexShape].getStartMiddleX());

		return result;

	}

	private Block(int nShape, BlockColor blockColor) {
		shape = nShape;
		this.color = blockColor;
	}

	public Point getTopLeft() {
		return this.topLeft;
	}

	private enum Shape {
		S1(1, "11.11"),
		S2(2, "1111:1.1.1.1"),
		S3(2, "010.111:10.11.10:111.010:01.11.01"),
		S4(2, "100.111:11.10.10:111.001:01.01.11"),
		S5(2, "001.111:10.10.11:111.100:11.01.01"),
		S6(2, "110.011:01.11.10"),
		S7(2, "011.110:10.11.01");
		
		private List<Frame> frames = new ArrayList<Frame>(4); 
		private final int startMiddleX;

		private Shape(int startMiddleX, String shapeStr ) {
			this.startMiddleX = startMiddleX;
			// Split the frames:
			final String[] strs = shapeStr.split("[:]");
			for( final String str : strs ) {
				frames.add( new Frame( str ) );
			}
		}

		private int getStartMiddleX() {
			return startMiddleX;
		}

		public Frame getFrame(int n) {
			return frames.get(n);
		}
	}

	private static class Frame {
		private int width = 0;
		private final List<int[]> data = new ArrayList<int[]>(4);

		private Frame(String frameStr) {
			final String[] strs = frameStr.split("[.]");
			for( final String str : strs ) {
				if( 0==width ) {
					width = str.length();
				}
				add( str );
			}
		}

		private Frame add(String rowStr) {
			int[] row = new int[rowStr.length()];
			for (int i = 0; i < rowStr.length(); i++) {
				row[i] = Byte.valueOf("" + rowStr.charAt(i));
			}
			data.add(row);
			return this;
		}

		private int[][] get() {
			int[][] result = new int[data.size()][];
			return data.toArray(result);
		}
	}
}
