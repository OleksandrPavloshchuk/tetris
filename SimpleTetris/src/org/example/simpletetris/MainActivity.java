package org.example.simpletetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TetrisView tetrisView = null;
	private TextView scoresView = null;
	private ScoresCounter scoresCounter = null;
	private Model model = new Model();

	private TextView startView = null;
	private TextView overView = null;

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

		startView = TextView.class.cast(findViewById(R.id.start));
		startView.setText(getApplicationContext()
				.getString(R.string.mode_ready));

		overView = TextView.class.cast(findViewById(R.id.over));
		overView.setText(getApplicationContext().getString(R.string.mode_over));

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
			overView.setVisibility(View.INVISIBLE);
			startView.setVisibility(View.INVISIBLE);
			scoresCounter.reset();
			model.gameStart();
			tetrisView.update(Model.Move.DOWN);
		}
	}

	public void endGame() {
		overView.setVisibility(View.VISIBLE);
	}

}
