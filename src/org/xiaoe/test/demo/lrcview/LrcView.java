package org.xiaoe.test.demo.lrcview;

import java.io.FileNotFoundException;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * LrcView, a panel for displaying the lrc info automaticlly.
 * 
 * @author aliguagua.zhengy
 * 
 */
public class LrcView extends SurfaceView implements SurfaceHolder.Callback {

	private LrcBackground core;
	private SurfaceHolder holder;

	public LrcView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// register our interest in hearing about changes to our surface
		holder = getHolder();
		if (holder == null) {
			Log.d("xiaoe", "holder == null.");
		}
		holder.addCallback(this);

		Surface surface = holder.getSurface();
		if (surface == null) {
			Log.d("xiaoe", "surface == null.");
		} else {
			Log.d("xiaoe", "surface.isValid: " + surface.isValid());
			Log.d("xiaoe", "holder.isCreating: " + holder.isCreating());
		}

		setFocusable(true);

		try {
			core = new LrcBackground(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("xiaoe", "new GameCore exception.");
		}
	}
	
	public void setBackgroundData(byte[] data) {
		core.setBackgroundData(data);
	}

	public void setStamps(Map<Integer, String> stamps) {
		core.setStamps(stamps);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// core.touch(event.getX(), event.getY());
		return true;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// set things up, figure out how big the game view is
		// try {
		// core.resize(width, height);
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// Log.d("xiaoe", "core.resize exception.");
		// }
	}

	public void surfaceCreated(SurfaceHolder holder) {
//		update(-1);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// stop the game loop
		// thread.interrupt();
	}

	public boolean isCreated() {
		return holder != null ? (holder.getSurface() != null ? holder
				.getSurface().isValid() : false) : false;
	}

	public void update(int index) {

		core.setCurrentLine(index);

		Canvas canvas = null;

		try {
			canvas = holder.lockCanvas();
			core.draw(canvas);
		} finally {
			if (canvas != null)
				holder.unlockCanvasAndPost(canvas);
		}
	}
}
