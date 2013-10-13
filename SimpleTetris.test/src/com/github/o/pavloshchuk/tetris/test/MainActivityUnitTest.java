package com.github.o.pavloshchuk.tetris.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.TextView;

import com.github.o.pavloshchuk.tetris.MainActivity;
import com.github.o.pavloshchuk.tetris.game.Model;

public class MainActivityUnitTest extends ActivityUnitTestCase<MainActivity> {

	private MainActivity activity;

	public MainActivityUnitTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		final Intent intent = new Intent(getInstrumentation().getContext(),
				MainActivity.class);
		startActivity(intent, null, null);
		this.activity = getActivity();
	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	@SmallTest
	public void testBeforeStart() {
		assertEquals(Model.GameStatus.BEFORE_START,
				this.activity.getGameStatus());
		assertMessageText("Tetris\nTouch the screen to play\nPress \"Back\" for exit");
	}

	@SmallTest
	public void testBackAfterStart() {
		this.activity.startNewGame();
		this.activity.onBackPressed();
		assertEquals(Model.GameStatus.PAUSED, this.activity.getGameStatus());
		assertMessageText("Paused\nTouch the screen to resume\nPress \"Back\" for exit");
	}

	private void assertMessageText(String expected) {
		final TextView textView = TextView.class.cast(activity
				.findViewById(com.github.o.pavloshchuk.tetris.R.id.message));
		assertNotNull(textView);
		assertEquals(expected, textView.getText());

	}

}
