package org.example.simpletetris;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TetrisView extends View {
	
	// TODO: temporary!!!!
	private final Random _random = new Random(System.currentTimeMillis());
	private static final int _ROWS = 20;
	private static final int _COLUMNS = 10;
	private final int[][] _data = new int[_ROWS][_COLUMNS];
	private static final int[] _COLORS = { Color.WHITE, 0xff008040, 0xff804000,
		0xff000080, 0xff0080cc, 0xffcc0040, 0xffcccc00 };
	
	
	private static final int BLOCK_OFFSET = 2;		
	private static final int FRAME_OFFSET = 10;	
	
	private final Paint paint = new Paint();
	
	private int width;
	private int height;
	private int cellWidth;
	private int cellHeight;
	
	public TetrisView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public TetrisView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		Random  random = new Random( System.currentTimeMillis() );
		for( int y=0; y<_data.length; y++ ) {
			for( int x=0; x<_data[y].length; x++ ) {
				int n = random.nextInt(_COLORS.length);
				_data[y][x] = n;
			}
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor( Color.GRAY );
		canvas.drawRect( 0, 0, width, height, paint);
		paint.setColor( Color.WHITE );
		canvas.drawRect( FRAME_OFFSET, FRAME_OFFSET, width - FRAME_OFFSET, height - FRAME_OFFSET, paint);
		
		for( int y=0; y<_data.length; y++ ) {
			for( int x=0; x<_data[y].length; x++ ) {
				// Draw cell:
				int color = _COLORS[ _data[y][x] ];
				paint.setColor(color);
				float left = FRAME_OFFSET + cellWidth * x + BLOCK_OFFSET;
				float top = FRAME_OFFSET + cellHeight * y + BLOCK_OFFSET;
				float right = FRAME_OFFSET + cellWidth * (x + 1) - BLOCK_OFFSET;
				float bottom = FRAME_OFFSET + cellHeight * (y + 1) - BLOCK_OFFSET;
				RectF rect = new RectF( left, top, right, bottom);
				canvas.drawRoundRect(rect, 4, 4, paint);
			}
		}
		
	}	
	
	// TODO: reset cell size
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		cellWidth = (width - 2 * FRAME_OFFSET ) / _COLUMNS;
		cellHeight = (height - 2 * FRAME_OFFSET ) / _ROWS;
	}	

}
