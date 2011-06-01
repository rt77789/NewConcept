package org.xiaoe.test.demo.lrcview;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;

public class LrcAnimater {

	private PointF position;
	private Paint paint;

	private String[] stamps;
	private Integer[] timeLine;
	private int currentIndex;
	private View view;

	public LrcAnimater(View view) throws FileNotFoundException {

		// # Set the painter's attributes.
		paint = new Paint();
		paint.setTextSize(12);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(5);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setTypeface(Typeface.MONOSPACE);

		paint.setTextAlign(Align.CENTER);
		position = new PointF(0, 0);

		this.view = view;
	}

	/**
	 * Set the lrc lines into stamps.
	 * 
	 * @param stamps
	 */
	public void setStamps(Map<Integer, String> stamps) {
		int i = 0;
		this.stamps = new String[stamps.size()];
		this.timeLine = new Integer[stamps.size()];
		for (Entry<Integer, String> entry : stamps.entrySet()) {
			this.stamps[i] = entry.getValue();
			this.timeLine[i++] = entry.getKey();
		}
	}

	/**
	 * Set the current index.
	 * 
	 * @param index
	 */
	public void setCurrentLine(int index) {
		currentIndex = index;
	}

	/**
	 * Redraw the screen(this lrcView).
	 * 
	 * @param canvas
	 */
	void draw(Canvas canvas) {

		float indent = 2;
		float spacing = paint.getFontSpacing() + indent;
		
		position.y = view.getHeight() / 2;
		position.x = view.getWidth() / 2;
		float offsetY = 0;
		if (currentIndex < stamps.length && currentIndex >= 0) {
			paint.setColor(Color.RED);

			List<String> slices = autoFolding(stamps[currentIndex]);
			int j = 0;
			for(String slice : slices) {
				canvas.drawText(slice, position.x, position.y + spacing * j++, paint);
			}
			offsetY = slices.size() * spacing;
		}

		paint.setColor(Color.WHITE);
		float upOffsetY = 0;
		for (int i = currentIndex - 1; i >= 0; --i) {
			if (i >= stamps.length)
				continue;
			List<String> slices = autoFolding(stamps[i]);
			int j = slices.size();
			for(String slice : slices) {
				canvas.drawText(slice, position.x, position.y - upOffsetY - spacing * j--, paint);
			}
			upOffsetY += slices.size() * spacing;
		}

		for (int i = currentIndex + 1; i < stamps.length; ++i) {
			if (i < 0)
				continue;
			List<String> slices = autoFolding(stamps[i]);
			int j = 0;
			for(String slice : slices) {
				canvas.drawText(slice, position.x, position.y + offsetY + spacing * j++, paint);
			}
			offsetY += slices.size() * spacing;
		}
	}

	private List<String> autoFolding(String line) {
		List<String> res = new LinkedList<String>();

		float[] wids = new float[line.length()];
		int num = paint.getTextWidths(line, wids);
		if (num != line.length()) {
			Log.d("xiaoe", "num != line.length()");
		}
		int wsum = 0;

		for (float wid : wids) {
			wsum += Math.ceil(wid);
		}

		if (wsum <= view.getWidth()) {
			res.add(line);
			// Log.d("xiaoe", "slice:" + line);
		} else {
			int i = 0;
			wsum = 0;
			for (i = 0; i < wids.length; ++i) {
				if (wsum + Math.ceil(wids[i]) > view.getWidth()) {
					break;
				}
				wsum += Math.ceil(wids[i]);
			}
			int ii = --i;
			// # right first ' ' or ',' is the end.
			for(; ii >= 0; --ii) {
				if(line.charAt(ii) == ' ' || line.charAt(ii) == ',') {
					break;
				}
			}
			if(ii >= 0) {
				i = ii;
			}
			// # i at least 1.
			if (i <= 0) {
				i = 1;
			}
			String slice = line.substring(0, i);
			res.add(slice);
		//	Log.d("xiaoe", "slice:" + slice);
			res.addAll(autoFolding(line.substring(i+1)));
		}

		return res;
	}
}
