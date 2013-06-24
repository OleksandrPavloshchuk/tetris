/**
 * ScreenField  visual presentation of the Tetris model
 *
 * @author      Alex Pavloshchuk
 * @version     0.3a October 2, 2002
 */

package org.alexp.tetris;

// import javax.microedition.midlet.*;
import javax.microedition.lcdui.Graphics;

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
    m_nCellW = ( nWidth -  (MARGIN<<1) ) / m_model.NUM_COLS;
    m_nCellH = ( nHeight - (MARGIN<<1) ) / m_model.NUM_ROWS;
  }
     
/**
 * Paint the screen in apropriate graphics
 * @param a_graphics the graphics
 */
  public void paint( Graphics gr ) {
      
    // draw the frame:
    int x = m_nLeft - MARGIN;
    int y = m_nTop - MARGIN;
    int width = (MARGIN << 1) + m_nCellW * m_model.NUM_COLS;
    int height = (MARGIN << 1) + m_nCellH * m_model.NUM_ROWS;
    gr.setColor( m_colorBG );
    gr.fillRect( x, y, width, height );
    gr.setColor( m_colorFG );
    gr.drawRect( x, y, width, height );    

    // draw all the cells:
    for( int i=0; i<m_model.NUM_ROWS; i++ ) {
        for( int j=0; j<m_model.NUM_COLS; j++ ) {
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

/* draw lines: 
        a_graphics.setColor( _colorFG );
        a_graphics.drawRect( x, y, _cellWidth , _cellHeight );
        a_graphics.setColor( _colorBG );
        a_graphics.fillRect( x + 1, y + 1, _cellWidth - 1, _cellHeight - 1 );
*/
        
        gr.setColor( m_colorBG );
        gr.fillRect( x, y, m_nCellW, m_nCellH );
        break;

      case Block.CELL_DYNAMIC:
      case Block.CELL_STATIC:
        gr.setColor( m_colorBG );
        gr.drawRect( x, y, m_nCellW , m_nCellH );
        gr.setColor( m_colorFG );
        gr.fillRect( x+1, y+1, m_nCellW-1, m_nCellH-1 );
        break;
    }

  }

}
