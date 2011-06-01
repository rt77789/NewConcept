package org.xiaoe.test.demo.music;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.xiaoe.test.demo.lrcview.LrcView;
import org.xiaoe.test.demo.parser.LrcParser;
import org.xiaoe.test.demo.struct.Pair;
import org.xiaoe.test.demo.struct.Util;
import org.xiaoe.test.demo.util.ID3v2;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MusicPlayer extends Activity {

	private MediaPlayer english = null; // # MediaPlayer

	private SeekBar sb = null; // # SeekBar for displaying process.

	private TextView currentTime = null; // # TextView for displaying current
	// process position.

	private TextView totalTime = null; // # TextView for displaying total time
	// of the mp3 file.

	private Button rockButton = null;

	private LrcView lrcView = null;

	private LrcParser lrc = null;

	private int flushPeriod = 100; // # The period of flush.

	private myThread thread = null;

	private Map<Integer, String> stamps;

	private int[] timeSpots;

	private Map<Integer, TextView> lrcTextView;

	private ID3v2 id3v2;

	private Handler mHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// super.handleMessage(msg);
			if (msg.what == MESSAGE) {
				if (currentTime == null) {
					Log.d("Debug:", "tv == null.");
				} else {
					int minutes = (msg.arg1 / 1000) / 60;
					int seconds = (msg.arg1 / 1000) % 60;

					currentTime.setText(minutes + ":" + seconds);

					int index = searchTimeSpots(msg.arg1);
					if (lrcView.isCreated())
						lrcView.update(index);
				}

				if (sb == null) {
					Log.d("Debug:", "sb == null.");
				} else {
					int sMax = sb.getMax();
					sb.setProgress(msg.arg1 * sMax / msg.arg2);
				}
				
				
			}
		}
	};

	private final static int MESSAGE = 0x11;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rocking);

		Intent intent = getIntent();

		String value = intent.getStringExtra("mp3Dir");

		try {
			initialize(value);
		} catch (Exception e) {
			Log.d("initilaize(): ", Util.printStackTrace(e));
		}
		// Log.d("xiaoe", "onCreate(): thread new.");
	}

	private String getLrcDir(String filePath) {

		// Log.d("xiaoe", "filePath is [" + filePath + "]");

		if (filePath.endsWith(".mp3")) {
			return filePath.substring(0, filePath.length() - ".mp3".length())
					+ ".lrc";
		}
		return null;
	}

	private void initialize(String filePath) throws Exception {

		sb = (SeekBar) findViewById(R.id.seekBar1);

		if (sb == null) {
			Log.d("Debug:", "get sb == null.");
		}
		currentTime = (TextView) findViewById(R.id.textView1);
		totalTime = (TextView) findViewById(R.id.textView2);
		rockButton = (Button) findViewById(R.id.button1);

		lrcView = (LrcView) findViewById(R.id.view1);

		if (lrcView == null) {
			Log.d("xiaoe", "lrcView == null.");
		}

		english = new MediaPlayer();

		english.setDataSource(filePath);
		english.prepare();

		if (english == null) {
			Log.d("Debug:", "get english == null.");
		} else {
			english.seekTo(0);
		}

		if (currentTime == null) {
			Log.d("Debug:", "get current == null.");
		}

		if (totalTime == null) {
			Log.d("Debug:", "get totalTime == null.");
		} else {
			if (english != null) {
				int minutes = (english.getDuration() / 1000) / 60;
				int seconds = (english.getDuration() / 1000) % 60;
				totalTime.setText(minutes + ":" + seconds);
			} else {
				totalTime.setText(0 + ":" + 0);
			}

		}

		stamps = new TreeMap<Integer, String>();
		lrcTextView = new TreeMap<Integer, TextView>();

		String lrcDir = getLrcDir(filePath);

		if (lrcDir == null) {
			Log.d("xiaoe", "lrcDir is null");
			return;
		} else {
			Log.d("xiaoe", "lrcDir is [" + lrcDir + "]");
		}

		try {
			lrc = new LrcParser(lrcDir);
			while (lrc.hasNext()) {
				Pair<Integer, String> line = lrc.next();
				if (line == null)
					continue;

				stamps.put(line.first, line.second);
			}
		} catch (Exception e) {
			Log.d("xiaoe", "LrcParser exception.");
		}

		Log.d("xiaoe", "fillTimeSpots before.");
		fillTimeSpots();

		lrcView.setStamps(stamps);

		id3v2 = new ID3v2(filePath);

		lrcView.setBackgroundData(id3v2.getAPIC());
		Log.d("xiaoe", "setBackgroundData over.");

		// lrcView.update(-1);
	}

	// # Destroy the current object and the super class.
	@Override
	protected void onDestroy() {
		if (english != null) {
			english.stop();
		}
		if (thread != null && thread.isAlive()) {

		}
		super.onDestroy();
	}

	// # The parameter is necessary.
	public void rockHandler(View view) {
		if (english == null) {
			Log.d("MusicPlayer->rockHandler:", "english == null.");
		} else {
			if (english.isPlaying()) {
				rockButton.setText("Pause");

				english.pause();
				thread.over();
				thread = null;
			} else {
				english.start();
				rockButton.setText("Play");
				
				if (thread == null) {
					thread = new myThread();
					thread.start();
				} else if (thread.isAlive()) {
					thread.over();
					thread = new myThread();
					thread.start();
				}
			}
		}
	}

	class myThread extends Thread {
		private boolean flag = false;

		public void over() {
			this.flag = true;
		}

		@Override
		public void run() {
			for (int i = 0; i < Integer.MAX_VALUE; ++i) {
				if (flag) {
					break;
				}
				try {
					Thread.sleep(flushPeriod);
				} catch (InterruptedException e) {
					Log.d("xiaoe", "myThread->run: Thread.sleep exception.");
				}

				Message msg = new Message();
				msg.what = MESSAGE;

				if (english != null) {
					msg.arg1 = english.getCurrentPosition();
					msg.arg2 = english.getDuration();
				} else {
					Log.d("myThread->run:", "english == null.");
					msg.arg1 = 0;
					msg.arg2 = 1;
					rockButton.setText("Pause");
				}
				mHandle.sendMessage(msg);
			}
		}
	}

	/**
	 * Fill time spots.
	 */
	private void fillTimeSpots() {
		timeSpots = new int[stamps.size()];
		if (timeSpots == null) {
			Log
					.d("MusicPlayer->fillTimeSpots:",
							" timeSpots new returns null.");
		}
		int i = 0;
		for (Entry<Integer, String> p : stamps.entrySet()) {
			if (i > timeSpots.length) {
				Log.d("LrcParser->fillTimeSpots: ", "i > timeSpots.length.");
				break;
			}
			timeSpots[i++] = p.getKey();
		}
	}

	/**
	 * Get the Lrc info corresponding to parameter time.
	 * 
	 * @param time
	 * @return
	 */
	public String locateStamp(int time) {
		int index = searchTimeSpots(time);
		if (index >= 0 && index < timeSpots.length) {
			return stamps.get(timeSpots[index]);
		}
		return null;
	}

	public TextView locateTextView(int time) {
		int index = searchTimeSpots(time);
		if (index >= 0 && index < timeSpots.length) {
			return lrcTextView.get(timeSpots[index]);
		}
		return null;
	}

	public TextView prevTextView(int time) {
		int index = searchTimeSpots(time) - 1;
		if (index < 0)
			return null;
		if (index >= 0 && index < timeSpots.length) {
			TextView stamp = lrcTextView.get(timeSpots[index]);
			return stamp;
		}
		return null;
	}

	public TextView nextTextView(int time) {
		int index = searchTimeSpots(time) + 1;
		if (index >= timeSpots.length)
			return null;
		if (index >= 0 && index < timeSpots.length) {
			TextView stamp = lrcTextView.get(timeSpots[index]);
			return stamp;
		}
		return null;
	}

	/**
	 * Search index of time spots array corresponding to parameter time. It's a
	 * binary search process.
	 * 
	 * @param time
	 * @return
	 */
	private int searchTimeSpots(int time) {

		int left = 0, right = timeSpots.length - 1;

		if (right < left)
			return -1;

		if (time >= timeSpots[right])
			return right;
		if (time <= timeSpots[left])
			return left;

		while (left <= right) {
			int mid = (left + right) / 2;
			if (time < timeSpots[mid]) {
				right = mid - 1;
			} else if (time > timeSpots[mid]) {
				left = mid + 1;
			} else {
				return mid;
			}
		}
		if (left >= timeSpots.length)
			left = timeSpots.length - 1;
		while (left >= 0) {
			if (timeSpots[left] <= time)
				break;
			--left;
		}

		return left;
	}
}