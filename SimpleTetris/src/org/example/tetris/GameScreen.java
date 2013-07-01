package org.example.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameScreen extends SurfaceView implements SurfaceHolder.Callback {

	private Context context;
	private Bitmap bitmap;
	private Ticker ticker;

	public GameScreen(Context context, AttributeSet attrSet) {
		super(context, attrSet);
		this.context = context;
		this.bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.future_city_19);

		SurfaceHolder surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		ticker = new Ticker(context, holder, this);

		ticker.setRunning(true);

		ticker.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		ticker.setRunning(false);

		boolean retry = true;
		while (retry) {
			try {
				ticker.join();
				retry = false;
			} catch (Exception e) {
				Log.v("Exception Occured", e.getMessage());
			}
		}
	}

	void doDraw(Canvas canvas) {
		canvas.drawColor(Color.GREEN);
		canvas.drawBitmap(bitmap, 50, 50, null);
	}
}
