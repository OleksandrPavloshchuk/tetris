/**
 * Model.java   algorithmes of Tetris separated from visual presentation
 *
 * @author      Alex Pavloshchuk
 * @version     0.2a September 28, 2002
 */
package org.alexp.tetris;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.lang.*;

import java.io.*;

public class Model {

  // block move values:
  public static final int MOVE_LEFT             = 0;
  public static final int MOVE_RIGHT            = 1;
  public static final int MOVE_DOWN             = 2;
  public static final int MOVE_ROTATE           = 3;

  // some constants in the model:
  public static final int NUM_COLS              = 10;   // number of columns in field
  public static final int NUM_ROWS              = 20;   // number of rows in field
  
  // game status constants:
  public static final int GAME_EMPTY            = 0;  
  public static final int GAME_ACTIVE           = 1;
  public static final int GAME_SUSPENDED        = 2;
  public static final int GAME_OVER             = 3;
  private int m_nStatusGame = GAME_EMPTY;
  
  // array of cell values:
  private byte[][] m_field = null;

  // active block:
  private Block m_block = null;
  
  // scores counter:
  private ScoresCounter m_counter = null;

  public Model( ScoresCounter counter ) {
    m_field = new byte[ NUM_ROWS ][ NUM_COLS ];
    m_counter = counter;
  }
  
/**
 * Reset all the field data
 */
  public void reset() {
      reset( false );       // call the inner method - reset the all data
  }

/**
 * Return status of the cell
 */
  public byte getCellStatus( int nRow, int nCol ) {
    return m_field[ nRow ][ nCol ];
  }
  public void setCellStatus( int nRow, int nCol, byte nStatus ) {
      m_field[ nRow ][ nCol ] = nStatus;
  }
  
  // Game status:
  public int getGameStatus() {
    return m_nStatusGame;
  }
  public void setGameStatus( int nStatus ) {
    m_nStatusGame = nStatus;
  }
  
  // Start the game:
  public void gameStart() {
    if( m_nStatusGame==GAME_ACTIVE ) return;
    m_nStatusGame = GAME_ACTIVE;
    m_block = Block.createBlock();    
  }
    
/**
 * Create and check the array of new data:
 */
  public synchronized void generateNewField(int nMove) {
      
    if( m_nStatusGame != GAME_ACTIVE ) return;

    // get the parameters of block:
    int nX = m_block.getLeftX();
    int nY = m_block.getTopY();
    int nFrame = m_block.getFrame();
    
    // Clear the old values:
    reset( true );

    // count new parameters:
    switch ( nMove ) {
      case MOVE_LEFT:   nX--; break;
      case MOVE_RIGHT:  nX++; break;
      case MOVE_DOWN:   nY++; break;
      case MOVE_ROTATE: 
        nFrame++; if (nFrame >= m_block.getFramesCount()) nFrame=0; break;
    }
    if( !isMoveValid( nY, nX, nFrame ) ) {
        // set old the block:
        isMoveValid( m_block.getTopY(), m_block.getLeftX(), m_block.getFrame() );
        
        if( nMove == MOVE_DOWN ) {
          
          // add the scores:
          m_counter.addScores();
          
          if (!newBlock() ) {
            m_nStatusGame = GAME_OVER;
            m_block = null; 
            reset( false );
            return;
          }
        }
        
        return;
    } else {
       // Make the new move:
       m_block.setState( nFrame, nY, nX ); 
    }
  }

  // ================================================
  // Helper functions:
  
/**
 * Reset the field data:
 * @param true - clear only dynamic data, false - clear all the data
 */
  private final void reset( boolean bDynamicDataOnly ) {
    for( int i=0; i<NUM_ROWS; i++ ) {
      for( int j=0; j< NUM_COLS; j++ ) {
        if( !bDynamicDataOnly || m_field[i][j]==Block.CELL_DYNAMIC ) {  
          m_field[i][j] = Block.CELL_EMPTY;
        }
      }
    }
  }
/**
 * Check the movement validity
 * @return true - movement is OK, false - some error
 */
  private final boolean isMoveValid( int nY, int nX, int nFrame ) {
    synchronized( m_field ) {  
      byte[][] shape = m_block.getShape( nFrame );
   
      // Check coords first:
      if( nY<0 ) return false;
      if( nX<0 ) return false;
      if( nY+shape.length>NUM_ROWS ) return false;
      if( nX+shape[0].length>NUM_COLS ) return false;
     
      // Check all the items in field:
      for( int i=0; i<shape.length; i++ ) {
        for( int j=0; j<shape[i].length; j++ ) {
          if( shape[i][j]+m_field[i+nY][j+nX] > Block.CELL_DYNAMIC ) return false;
        }
      }
   
      // All cell is correct - add the data:
      for( int i=0; i<shape.length; i++ ) {
        for( int j=0; j<shape[i].length; j++ ) {
          m_field[i+nY][j+nX] += shape[i][j];
        }
      }
      return true;
    }
  }
  
  /**
   * Create the new block:
   * @return true - block can be generated, 
   * @return false - can't generate the block - GAME OVER!
   */
  private synchronized boolean newBlock() {
            
    // set all the dynamic data as static:
    for (int i=0; i<m_field.length; i++ ) {
      for (int j=0; j<m_field[i].length; j++ ) {
        if ( getCellStatus( i, j )  == Block.CELL_DYNAMIC ) {
          setCellStatus( i, j , Block.CELL_STATIC );
        } 
      }
    }
    
    for( int i=0; i<m_field.length; i++ ) {
      boolean bFullRow = true;
      for( int j=0; j<m_field[i].length; j++ ) {  
        bFullRow &= ( getCellStatus( i, j ) == Block.CELL_STATIC );
      }
      if( bFullRow ) {
          shiftRows( i );
          
          // add lines to counter:
          m_counter.addLine();
      }
    }
    
    // Generate the new block:
    m_block = Block.createBlock();
    
    // Check the validity of new block:
    if( !isMoveValid( m_block.getTopY(), m_block.getLeftX(), m_block.getFrame() ) ) {
        // GAME IS OVER!
        m_counter.reset();        
        return false;
    }
    return true;
  }
  
  private synchronized final void shiftRows( int nToRow ) {
    if( nToRow>0 ) {
      for( int j=nToRow-1; j>=0; j-- ) {
        for( int m=0; m<m_field[j].length; m++ ) {
          setCellStatus( j+1, m, getCellStatus( j, m ) );
        }
      }
    }
    for( int j=0; j<m_field[0].length; j++ ) {
        setCellStatus( 0, j, Block.CELL_EMPTY );
    }
  }  
}
