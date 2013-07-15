package org.example.simpletetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class TetrisView extends View {		
	
	private static final int DELAY = 400;

	private RedrawHandler redrawHandler = new RedrawHandler();

	private static final int BLOCK_OFFSET = 2;
	private static final int FRAME_OFFSET = 10;

	private final Paint paint = new Paint();

	private int width;
	private int height;
	private Dimension cellSize = null;	
	private Model model = null;
	private long lastMove = 0;

	public TetrisView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TetrisView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	
	public void update( Model.Move move ) {
		if( null==model || !model.isGameActive() ) {
			return;
		}
        long now = System.currentTimeMillis();

        if (now - lastMove > DELAY) {
        	model.generateNewField(move);
    		invalidate();
            lastMove = now;
        }
		redrawHandler.sleep(DELAY);
	}

	private void drawCell(Canvas canvas, int row, int col) {

		byte nStatus = model.getCellStatus(row, col);
		
		if( Block.CELL_EMPTY!=nStatus ) {
			int color = Block.CELL_DYNAMIC==nStatus ? model.getActiveBlockColor() :
				Block.getColorForStaticValue(nStatus);
			drawCell(canvas, col, row, color);
		}
	}

	private void drawCell(Canvas canvas, int x, int y, int colorFG) {
		paint.setColor(colorFG);
		float top = FRAME_OFFSET + y * cellSize.getHeight() + BLOCK_OFFSET;
		float left = FRAME_OFFSET + x * cellSize.getWidth() + BLOCK_OFFSET;
		float bottom = FRAME_OFFSET + (y + 1) * cellSize.getHeight() - BLOCK_OFFSET;
		float right = FRAME_OFFSET + (x + 1) * cellSize.getWidth() - BLOCK_OFFSET;
		RectF rect = new RectF(left,top,right,bottom);
		
		canvas.drawRoundRect(rect, 4, 4 , paint );
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawFrame(canvas);
		if( null==model) {
			return;
		}
		
		// draw all the cells:
		for (int i = 0; i < Model.NUM_ROWS; i++) {
			for (int j = 0; j < Model.NUM_COLS; j++) {
				drawCell(canvas, i, j);
			}
		}		
	}

	private void drawFrame(Canvas canvas) {
		paint.setColor(Color.GRAY);
		canvas.drawRect(0, 0, width, height, paint);
		paint.setColor(Color.WHITE);
		canvas.drawRect(FRAME_OFFSET, FRAME_OFFSET, width - FRAME_OFFSET,
				height - FRAME_OFFSET, paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		int cellWidth = (width - 2 * FRAME_OFFSET) / Model.NUM_COLS;
		int cellHeight = (height - 2 * FRAME_OFFSET) / Model.NUM_ROWS;
		this.cellSize = new Dimension(cellWidth, cellHeight);
	}
	
	class RedrawHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			TetrisView.this.update( Model.Move.DOWN);
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};
}
