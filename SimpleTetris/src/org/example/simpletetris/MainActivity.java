package org.example.simpletetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TetrisView tetrisView = null;
	private TextView scoresView = null;
	private ScoresCounter scoresCounter = null;
	private Model model = new Model();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tetrisView = TetrisView.class.cast(findViewById(R.id.tetris));
		tetrisView.setModel(model);
		tetrisView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!model.isGameActive()) {
					startNewGame();
					return false;
				}

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

				return false;
			}
		});

		scoresView = TextView.class.cast(findViewById(R.id.scores));
		scoresCounter = new ScoresCounter(scoresView);
		model.setCounter(scoresCounter);
	}

	private int getDirection(View v, MotionEvent event) {
		// Normalize x,y between 0 and 1
		float x = event.getX() / v.getWidth();
		float y = event.getY() / v.getHeight();

		// Direction will be [0,1,2,3] depending on quadrant
		int direction = 0;
		direction = (x > y) ? 1 : 0;
		direction |= (x > 1 - y) ? 2 : 0;

		// 0 - left, 1 - rotate, 2 - down, 3 - right

		return direction;
	}

	public void doMove(Model.Move move) {
		if (model.isGameActive()) {
			tetrisView.update(move);
			scoresView.invalidate();
			if (model.isGameOver()) {
				Toast toast = Toast.makeText(getApplicationContext(),
						"GAME OVER!", Toast.LENGTH_SHORT);
				toast.show();
				scoresCounter.reset();
			}
		}
	}

	public final void startNewGame() {
		if (!model.isGameActive()) {
			model.gameStart();
		}
	}

}
