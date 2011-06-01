package org.xiaoe.test.demo.music;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Select the file in SD card.
 * 
 * @author aliguagua.zhengy
 * 
 */
public class FileSelector extends Activity {

	private final String MAIN_PATH = "/sdcard";
	private ProgressDialog pd = null;
	private LinearLayout fileSelectorPanel = null;
	private Handler handler = null;
	private Thread fileSelectorThread = null;

	/**
	 * Set progress bar visible/gone and find all 'mp3' files.
	 * 
	 * @author aliguagua.zhengy
	 * 
	 */
	class FileSelectorRunnable implements Runnable {
		String dir, fileName;

		FileSelectorRunnable(String dir, String fileName) {
			this.dir = dir;
			this.fileName = fileName;
		}

		FileSelectorRunnable() {
		}

		@Override
		public void run() {
			// # Display the progress bar.
			handler.post(new Runnable() {
				public void run() {
					pd.show();
				}
			});

			findFile(MAIN_PATH, MAIN_PATH);

			// # Don't display the progress bar.
			handler.post(new Runnable() {
				public void run() {
					pd.dismiss();
				}
			});
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initialize();
	}

	private void initialize() {

		fileSelectorPanel = (LinearLayout) findViewById(R.id.linearLayoutf);

		pd = new ProgressDialog(this);
		// # Set the progress dialog info.
		pd.setTitle("Loading");
		pd.setMessage("loading sdcard mp3 files...");
		pd.setIndeterminate(true);
		pd.setCancelable(true);

		if (fileSelectorPanel == null) {
			Log.d("xiaoe", "fileSelectorPanel is null.");
			return;
		}

		handler = new Handler();

		// # Start file selecting thread(display the progress bar).
		fileSelectorThread = new Thread(new FileSelectorRunnable());
		fileSelectorThread.start();
	}

	/**
	 * Text click listener used for starting new activity.
	 * 
	 * @author aliguagua.zhengy
	 * 
	 */
	class TextClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// # Get the mp3 file directory.
			String filePath = v.getContentDescription().toString();

			// # Create a new Intent object.
			Intent intent = new Intent();

			intent.putExtra("mp3Dir", filePath);

			// Jump from FileSelector to MusicPlayer.
			intent.setClass(FileSelector.this, MusicPlayer.class);
			// Start the new intent.
			FileSelector.this.startActivity(intent);
		}
	}

	/**
	 * Find all files which has a suffix ".mp3".
	 * 
	 * @param dir
	 * @param fileName
	 */
	private void findFile(String dir, String fileName) {
		File file = new File(dir);

		Log.d("xiaoe", "dir:" + dir);
		Log.d("xiaoe", "fileName:" + fileName);
		// Log.d("xiaoe", "seperator:" + File.separator);

		if (!file.exists()) {
			Log.d("xiaoe:", "FileSelector->findFile: " + dir + "isn't exists.");
			return;
		}
		// # This dir(file) is a directory or a file or else.
		if (file.isDirectory()) {

			// Log.d("xiaoe", dir + " is a directory.");

			String[] lists = file.list();
			if (lists != null) {
				for (String nextFile : lists) {
					findFile(dir + File.separator + nextFile, nextFile);
				}
			}
		} else if (file.isFile()) {
			// Log.d("xiaoe", dir + " is a file.");

			if (checkFileType(dir)) {

				handler.post(new FileSelectorRunnable(dir, fileName) {
					public void run() {
						insertView(dir, fileName);
					}
				});
			}
		} else {
			// Log.d("xiaoe", dir + " is else.");
		}
	}

	/**
	 * Insert a new TextView to the main page.
	 * 
	 * @param dir
	 * @param fileName
	 */
	private void insertView(String dir, String fileName) {
		TextView tv = new TextView(fileSelectorPanel.getContext());

		tv.setText(fileName);
		tv.setContentDescription(dir);
		tv.setClickable(true);
		tv.setOnClickListener(new TextClickListener());

		fileSelectorPanel.addView(tv);
	}

	/**
	 * Check file name whether end with a suffix ".mp3".
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean checkFileType(String fileName) {
		return fileName.endsWith(".mp3");
	}
}
