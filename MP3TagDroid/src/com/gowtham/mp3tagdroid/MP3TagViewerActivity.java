package com.gowtham.mp3tagdroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MP3TagViewerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_tag_viewer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mp3_tag_viewer, menu);
		return true;
	}

}
