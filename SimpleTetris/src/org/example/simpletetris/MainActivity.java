package org.example.simpletetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String ICICLE_TAG = "simple-tetris";

	private TetrisView tetrisView = null;
	private TextView scoresView = null;
	private ScoresCounter scoresCounter = null;
	private Model model = new Model();

	private TextView messageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tetrisView = TetrisView.class.cast(findViewById(R.id.tetris));
		tetrisView.setModel(model);
		tetrisView.setActivity(this);
		tetrisView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (model.isGameOver() || model.isGameReady()) {
					startNewGame();
					return false;
				} else if (model.isGameActive()) {

					int direction = getDirection(v, event);
					switch (direction) {
					case 0: // left
						doMove(Model.Move.LEFT);
						break;
					case 1: // rotate
						doMove(Model.Move.ROTATE);
						break;
					case 2: // down
						doMove(Model.Move.DOWN);
						break;
					case 3: // right
						doMove(Model.Move.RIGHT);
						break;
					}

					return true;
				} else {
					// Paused state
					return true;
				}
			}
		});

		scoresView = TextView.class.cast(findViewById(R.id.scores));
		scoresCounter = new ScoresCounter(scoresView);
		model.setCounter(scoresCounter);

		messageView = TextView.class.cast(findViewById(R.id.message));

		// Restore the state:
		if (null != savedInstanceState) {
			onRestoreInstanceState(savedInstanceState);
		} else {
			messageView.setText(getApplicationContext().getString(
					R.string.mode_ready));
		}

	}

	private int getDirection(View v, MotionEvent event) {
		// Normalize x,y between 0 and 1

		float x = event.getX() / v.getWidth();
		float y = event.getY() / v.getHeight();

		int direction;
		if (y > x) {
			direction = (x > 1 - y) ? 2 : 0;
		} else {
			direction = (x > 1 - y) ? 3 : 1;
		}
		return direction;
	}

	public void doMove(Model.Move move) {
		if (model.isGameActive()) {
			tetrisView.update(move);
			scoresView.invalidate();
		}
	}

	public final void startNewGame() {
		if (!model.isGameActive()) {
			messageView.setVisibility(View.INVISIBLE);
			scoresCounter.reset();
			model.gameStart();
			tetrisView.update(Model.Move.DOWN);
		}
	}

	public void endGame() {
		messageView.setVisibility(View.VISIBLE);
		messageView
				.setText(getApplicationContext().getText(R.string.mode_over));
	}

	public void pauseGame() {
		model.setGamePaused();
		messageView.setVisibility(View.VISIBLE);
		messageView.setText(getApplicationContext()
				.getText(R.string.mode_pause));
	}

	@Override
	protected void onPause() {
		super.onPause();
		pauseGame();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Bundle bundle = new Bundle();
		model.storeTo(bundle);
		scoresCounter.storeTo(bundle);
		outState.putBundle(ICICLE_TAG, bundle);
	}

	@Override
	protected void onRestoreInstanceState(Bundle inState) {
		super.onSaveInstanceState(inState);
		Bundle bundle = inState.getBundle(ICICLE_TAG);
		if (null != bundle) {
			model.restoreFrom(bundle);
			scoresCounter.restoreFrom(bundle);
		}
		pauseGame();

	}

}
