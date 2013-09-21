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

	// cell status values (outer):
	public static final int CELL_EMPTY = 0;
	public static final int CELL_DYNAMIC = 1;

	private static Random random = new Random();

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

	public int getStaticValue() {
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
		return Shape.values()[shape].frameCount;
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
		S1(1, 1) {
			@Override
			public Frame getFrame(int n) {
				return new Frame(2).add("11").add("11");
			}

		},
		S2(2, 2) {

			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(4).add("1111");
				case 1:
					return new Frame(1).add("1").add("1").add("1").add("1");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		S3(4, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("010").add("111");
				case 1:
					return new Frame(2).add("10").add("11").add("10");
				case 2:
					return new Frame(3).add("111").add("010");
				case 3:
					return new Frame(2).add("01").add("11").add("01");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		S4(4, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("100").add("111");
				case 1:
					return new Frame(2).add("11").add("10").add("10");
				case 2:
					return new Frame(3).add("111").add("001");
				case 3:
					return new Frame(2).add("01").add("01").add("11");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		S5(4, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("001").add("111");
				case 1:
					return new Frame(2).add("10").add("10").add("11");
				case 2:
					return new Frame(3).add("111").add("100");
				case 3:
					return new Frame(2).add("11").add("01").add("01");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		S6(2, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("110").add("011");
				case 1:
					return new Frame(2).add("01").add("11").add("10");
				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		},
		S7(2, 1) {
			@Override
			public Frame getFrame(int n) {
				switch (n) {
				case 0:
					return new Frame(3).add("011").add("110");
				case 1:
					return new Frame(2).add("10").add("11").add("01");

				}
				throw new IllegalArgumentException("Invalid frame number: " + n);
			}
		};
		private final int frameCount;
		private final int startMiddleX;

		private Shape(int frameCount, int startMiddleX) {
			this.frameCount = frameCount;
			this.startMiddleX = startMiddleX;
		}

		private int getStartMiddleX() {
			return startMiddleX;
		}

		public abstract Frame getFrame(int n);
	}

	private static class Frame {
		private final int width;

		private Frame(int width) {
			this.width = width;
		}

		private final List<int[]> data = new ArrayList<int[]>(4);

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
