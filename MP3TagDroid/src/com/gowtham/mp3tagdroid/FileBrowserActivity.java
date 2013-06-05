package com.gowtham.mp3tagdroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

public class FileBrowserActivity extends Activity {

	private ListView listViewEntries;
	private TextView textViewPath;
	
	public FileBrowserActivity() {
		textViewPath = (TextView) findViewById(R.id.textViewPath);
		listViewEntries = (ListView) findViewById(R.id.listViewFiles);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_browser);
		setDir("/");
	}

	private void setDir(String dir) {
		textViewPath.setText(dir);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_browser, menu);
		return true;
	}

}
