/**
 * Block.java   single Tetris block description
 */

package org.example.tetris;

import java.util.*;

import java.io.*;

public class Block {

  // cell status values (inner):
  private static final byte _N          = 0;
  private static final byte _S          = 1;
  private static final byte _D          = 2;
  // cell status values (outer):
  public static final byte CELL_EMPTY   = _N;
  public static final byte CELL_STATIC  = _S;
  public static final byte CELL_DYNAMIC = _D;
    
  private static Random m_random        = new Random();

  // current block state:
  private int m_nShape                  = 0;
  private int m_nFrame                  = 0;
  private int m_nTopY                   = 0;
  private int m_nLeftX                  = Model.NUM_COLS >> 1;

  // current block state:
  public int getFrame() { return m_nFrame; }
  public int getTopY() { return m_nTopY; }
  public int getLeftX() { return m_nLeftX; }

  public final void setState( int nFrame, int nTopY, int nLeftX ) {
    m_nFrame = nFrame; m_nTopY = nTopY; m_nLeftX = nLeftX;
  }

  public final int getFramesCount() {
      return SHAPES[m_nShape].length;
  }
  public final byte[][] getShape( int nFrame ) {
      return SHAPES[m_nShape][nFrame];
  }
  public final int getShapeWidth(int nFrame) {
      return SHAPES[m_nShape][nFrame][0].length;
  }
  
  public final static synchronized Block createBlock() {
    // generate random number:
    int indexShape = Math.abs( m_random.nextInt() % SHAPES.length );
    return new Block( indexShape );
  }
  
  private Block( int nShape ) {
      m_nShape = nShape;
  }
  
  // trace [[
  synchronized void print( PrintStream out ) {
    out.println( getClass().getName() + " [[");
    out.println( "shape = " + m_nShape );
    out.println( "frame = " + m_nFrame );
    out.println( "top Y = " + m_nTopY );
    out.println( "left X = " + m_nLeftX );    
    out.println( "]] " + getClass().getName());
  }
  // ]] trace
  
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
