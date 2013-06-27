/**
 * ScreenField  visual presentation of the Tetris model
 */

package org.example.tetris;

import java.awt.Color;
import java.awt.Graphics;

public class ScreenField  extends ScreenBase {

  private Model m_model = null;

  // geometric sizes:
  private int m_nCellW            = 0;
  private int m_nCellH            = 0;
  static private final int MARGIN = 2;

/**
 * Constructor
 * @param a_model model to represent Tetris data
 */
  public ScreenField( Model model, int nLeft, int nTop, int nWidth, int nHeight,
    int colorFG, int colorBG ) 
  {
    super( nLeft+MARGIN, nTop+MARGIN, nWidth, nHeight, colorFG, colorBG );
    m_model = model;
    m_nCellW = ( nWidth -  (MARGIN<<1) ) / Model.NUM_COLS;
    m_nCellH = ( nHeight - (MARGIN<<1) ) / Model.NUM_ROWS;
  }
     
/**
 * Paint the screen in apropriate graphics
 * @param a_graphics the graphics
 */
  @Override
  public void paint( Graphics gr ) {
      
    // draw the frame:
    int x = m_nLeft - MARGIN;
    int y = m_nTop - MARGIN;
    int width = (MARGIN << 1) + m_nCellW * Model.NUM_COLS;
    int height = (MARGIN << 1) + m_nCellH * Model.NUM_ROWS;
    gr.setColor( new Color( m_colorBG ) );
    gr.fillRect( x, y, width, height );
    gr.setColor( new Color( m_colorFG ) );
    gr.drawRect( x, y, width, height );    

    // draw all the cells:
    for( int i=0; i<Model.NUM_ROWS; i++ ) {
        for( int j=0; j<Model.NUM_COLS; j++ ) {
            drawCell( gr, i, j ); 
        }
    }
    
  }

/**
 * Draw the cell
 * @param a_graphics graphics to draw
 * @param a_status the cell status: empty, static or dynamic figure
 * @param a_row row of the cell
 * @param a_col column of the cell
 */
  private void drawCell( Graphics gr, int nRow, int nCol ) {
      
    byte nStatus = m_model.getCellStatus( nRow, nCol );

    int x = nCol * m_nCellW + m_nLeft;
    int y = nRow * m_nCellH + m_nTop;

    switch ( nStatus ) {

      case Block.CELL_EMPTY:
        
        gr.setColor( new Color( m_colorBG ) );
        gr.fillRect( x, y, m_nCellW, m_nCellH );
        break;

      case Block.CELL_DYNAMIC:
      case Block.CELL_STATIC:
        gr.setColor( new Color( m_colorBG ) );
        gr.drawRect( x, y, m_nCellW , m_nCellH );
        gr.setColor( new Color( m_colorFG ) );
        gr.fillRect( x+1, y+1, m_nCellW-1, m_nCellH-1 );
        break;
    }

  }

}
