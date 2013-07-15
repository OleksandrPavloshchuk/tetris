package org.example.simpletetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TetrisView tetrisView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tetrisView = TetrisView.class.cast(findViewById(R.id.tetris));
		
		tetrisView.setOnTouchListener( new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int direction = getDirection(v, event);
				String text = String.format("Direction: %d", direction );
				Toast toast = Toast.makeText( getApplicationContext(), text, Toast.LENGTH_SHORT );
				toast.show();
				return true;
			}
		});
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
}
