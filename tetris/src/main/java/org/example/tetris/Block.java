/**
 * Block.java   single Tetris block description
 */

package org.example.tetris;

import java.awt.Color;
import java.util.Random;

public class Block {
	
	public enum BlockColor {
		RED( 0x990000, (byte) 2 ),
		GREEN( 0x009900, (byte) 3 ),
		BLUE( 0x000099, (byte) 4 )
		;
		private final Color color;
		private final byte value; 
		
		private BlockColor( int color, byte value ) {
			this.color = new Color( color );
			this.value = value;
		}
	}

	// cell status values (inner):
	private static final byte _N = 0;
	// private static final byte _S = 1;
	private static final byte _D = 2;
	// cell status values (outer):
	public static final byte CELL_EMPTY = _N;
	// public static final byte CELL_STATIC = _S;
	public static final byte CELL_DYNAMIC = _D;

	private static Random m_random = new Random();

	// current block state:
	private int m_nShape = 0;
	private int m_nFrame = 0;
	private int m_nTopY = 0;
	private int m_nLeftX = Model.NUM_COLS >> 1;
	
	private BlockColor color;

	// current block state:
	public int getFrame() {
		return m_nFrame;
	}

	public int getTopY() {
		return m_nTopY;
	}

	public int getLeftX() {
		return m_nLeftX;
	}
	
	public Color getColor() {
		return color.color;
	}
	
	public byte getStaticValue() {
		return color.value;
	}
	
	public static Color getColorForStaticValue( byte b ) {
		for( BlockColor item : BlockColor.values() ) {
			if( b == item.value ) {
				return item.color;
			}
		}
		return null;
	}

	public final void setState(int nFrame, int nTopY, int nLeftX) {
		m_nFrame = nFrame;
		m_nTopY = nTopY;
		m_nLeftX = nLeftX;
	}

	public final int getFramesCount() {
		return SHAPES[m_nShape].length;
	}

	public final byte[][] getShape(int nFrame) {
		return SHAPES[m_nShape][nFrame];
	}

	public final int getShapeWidth(int nFrame) {
		return SHAPES[m_nShape][nFrame][0].length;
	}

	public final static synchronized Block createBlock() {
		// generate random number:
		int indexShape = m_random.nextInt(SHAPES.length);
		BlockColor blockColor = BlockColor.values()[ m_random.nextInt( BlockColor.values().length ) ];
		return new Block(indexShape, blockColor );
	}

	private Block(int nShape, BlockColor blockColor ) {
		m_nShape = nShape;
		this.color = blockColor;
	}
  
  // Shapes data:
  private static final byte[][][][] SHAPES = {
    {{{ _D, _D },             // [][]
      { _D, _D }}             // [][]
    },       
    {{{ _D, _D, _D, _D }      // [][][][]
     },
     {{ _D },                  // []
      { _D },                  // []
      { _D },                  // []
      { _D }}                  // []
    },
    {{{ _N, _D, _D },          //   [][]
      { _D, _D, _N }           // [][]
     },
     {{ _D, _N },              // []
      { _D, _D },              // [][]
      { _N, _D }}              //   []
    },
    {{{ _D, _D, _N },          // [][]
      { _N, _D, _D }           //   [][]
     },
     {{ _N, _D },              //   []
      { _D, _D },              // [][]
      { _D, _N }}              // []
    },
    {{{ _N, _N, _D },          //     []
      { _D, _D, _D }           // [][][]
     },
     {{ _D, _N },              // []
      { _D, _N },              // []
      { _D, _D }               // [][]
     },
     {{ _D, _D, _D },          // [][][]
      { _D, _N, _N }           // []
     },
     {{ _D, _D },              // [][]
      { _N, _D },              //   []
      { _N, _D }}              //   []
    },
    {{{ _D, _N, _N },          // []
      { _D, _D, _D }           // [][][]
     },
     {{ _D, _D },              // [][]
      { _D, _N },              // []
      { _D, _N }               // []
    },
    {{ _D, _D, _D },           // [][][]
     { _N, _N, _D }            //     []
    },
    {{ _N, _D },               //   []
     { _N, _D },               //   []
     { _D, _D }}               // [][]
    },
   {{{ _N, _D, _N },           //   []
     { _D, _D, _D }            // [][][]
    },
    {{ _D, _N },               // []
     { _D, _D },               // [][]
     { _D, _N }                // []
    },
    {{ _D, _D, _D },           // [][][]
     { _N, _D, _N }            //   []
    },
    {{ _N, _D },               //   []
     { _D, _D },               // [][]
     { _N, _D }}               //   []
   }
  };
  
}
