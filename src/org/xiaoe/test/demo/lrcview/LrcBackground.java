package org.xiaoe.test.demo.lrcview;

import java.io.FileNotFoundException;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class LrcBackground {

	private int backColor;
	private LrcAnimater ball;
	private Bitmap bitmap;
	private View view;
	private Paint paint;

	public LrcBackground(View view) throws FileNotFoundException {
		backColor = Color.parseColor("black");
		paint = new Paint();

		this.view = view;

		ball = new LrcAnimater(view);
	}

	public void setBackgroundData(byte[] data) {
		if (data == null) {
			Log.d("xiaoe", "setBackgroundData argument data == null");
		}
		
		//BitmapFactory.
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		if (bitmap == null) {
			Log.d("xiaoe", "BitmapFactory.decodeByteArray returns null");
		}
	}

	public void setStamps(Map<Integer, String> stamps) {
		ball.setStamps(stamps);
	}

	public void draw(Canvas canvas) {
		// this is the draw frame phase of the game
		if (canvas == null) {
			Log.d("xiaoe", "canvas == null");
		}

		if (bitmap != null) {
			
			int bitHeight = (view.getHeight() - bitmap.getHeight()) / 2;
			int bitWidth = (view.getWidth() - bitmap.getWidth()) / 2;
			
			canvas.drawBitmap(bitmap, bitWidth, bitHeight, paint);
		}
		else {
			canvas.drawColor(this.backColor);
		}
		ball.draw(canvas);
	}

	public void setCurrentLine(int index) {
		ball.setCurrentLine(index);
	}

	public void resize(int width, int height) throws FileNotFoundException {
		// called when the game view is resized
		ball = new LrcAnimater(view);

	}

	public void touch(float x, float y) {
		// called when the screen is touched
		// paddle.touch(x, y);
	}
}
