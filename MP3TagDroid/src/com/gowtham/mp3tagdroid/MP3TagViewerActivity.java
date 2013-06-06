package com.gowtham.mp3tagdroid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.ImageData;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MP3TagViewerActivity extends Activity implements OnClickListener {

	private static final int REQUEST_CODE = 1;
	private Button buttonLoadAudioFile, buttonSaveTags;
	private TextView textViewAudioFilePath, editTextTitle, editTextAlbum, editTextArtist, editTextGenre;
	private ImageView imageAlbumArt;
	
	private String audioFilePath;
	private MusicMetadataSet metadataSet;
	private IMusicMetadata metadata;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_tag_viewer);
		
		buttonLoadAudioFile = (Button) findViewById(R.id.buttonLoadAudioFile);
		buttonSaveTags = (Button) findViewById(R.id.buttonSave);
		
		textViewAudioFilePath = (TextView) findViewById(R.id.textViewAudioFilePath);
		editTextTitle = (TextView) findViewById(R.id.editTextTitle);
		editTextAlbum = (TextView) findViewById(R.id.editTextAlbum);
		editTextArtist = (TextView) findViewById(R.id.editTextArtist);
		editTextGenre = (TextView) findViewById(R.id.editTextGenre);
		imageAlbumArt = (ImageView) findViewById(R.id.imageViewAlbumArt);
		
		setListeners();
	}

	private void setListeners() {
		buttonLoadAudioFile.setOnClickListener(this);
		buttonSaveTags.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mp3_tag_viewer, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_load_audio_file:
			showFileBrowser();
			return true;
		case R.id.menu_about:
			showAbout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.buttonLoadAudioFile:
				showFileBrowser();
				break;
			case R.id.buttonSave:
				saveTags();
				break;
			default:
				break;
		}
	}

	private void showFileBrowser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		//intent.setData(Uri.parse("file://"));
		intent.setType("audio/mpeg");
		startActivityForResult(intent, REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(requestCode != REQUEST_CODE)
			return;
		if(resultCode == RESULT_OK) {
			loadTags(intent.getData().getPath());
		}
	}

	private void loadTagsInternal(String path) throws Exception {
		textViewAudioFilePath.setText(path);
		TagsReaderAsyncTask task = new TagsReaderAsyncTask();
		progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("Loading tags...");
		progressDialog.show();
		task.execute(new File(path));
	}
	
	private void showTags(IMusicMetadata metadata) {
		clearTags();
		editTextTitle.setText(metadata.getSongTitle());
		editTextAlbum.setText(metadata.getAlbum());
		editTextArtist.setText(metadata.getArtist());
		editTextGenre.setText(metadata.getGenreName());
		Vector<?> pictures = metadata.getPictures();
		if(!pictures.isEmpty()) {
			ImageData image = (ImageData)pictures.get(0);
			Bitmap bmp = BitmapFactory.decodeByteArray(image.imageData, 0, image.imageData.length);
			imageAlbumArt.setImageBitmap(bmp);
		}
	}
	
	private void clearTags() {
		editTextTitle.setText("");
		editTextAlbum.setText("");
		editTextArtist.setText("");
		editTextGenre.setText("");
		imageAlbumArt.setImageResource(android.R.color.transparent);
	}

	private void loadTags(String path) {
		try {
			audioFilePath = path;
			loadTagsInternal(path);
		}
		catch(Exception e) {
			error(e);
		}
	}
	
	private void error(Exception e) {
		Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
		AlertDialog.Builder b = new Builder(this);
		b.setTitle("ERROR: " + e.getMessage());
		b.setCancelable(true);
		b.create().show();
	}
	
	private void saveTags() {
		if(audioFilePath == null || metadata == null || metadataSet == null)
			return;
		try {
			saveTagsInternal();
		}
		catch(Exception e) {
			error(e);
		}
	}

	private void saveTagsInternal() throws Exception {
		progressDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle("Saving tags...");
		progressDialog.show();
		TagsWriterAsyncTask task = new TagsWriterAsyncTask();
		task.execute(null);
	}	
	
	private void copy(File src, File dst) throws IOException {
		InputStream in = null;
	    OutputStream out = null;
	    int len;
	    byte []buf;
	    
		try {
		    in = new FileInputStream(src);
		    out = new FileOutputStream(dst);
	
		    buf = new byte[1024];
		    while ((len = in.read(buf)) > 0)
		        out.write(buf, 0, len);
		}
		finally {
			if(in != null)
				in.close();
			if(out != null)
				out.close();
		}
	}
	
	private void move(File src, File dst) throws IOException {
		// First copy and then delete the source file if successful
		copy(src, dst);
		src.delete();
	}
	
	private void showAbout() {
		AlertDialog.Builder b = new Builder(this);
		b.setTitle("About MP3TagDroid 0.1");
		b.setMessage("This program is free software, you may re-distribute it as you like.\n\n" +
					 "Source code is available under GNU GPL @ https://github.com/gowthamgowtham/MP3TagDroid");
		
		b.create().show();
	}

	
	/** Manages reading tags asynchronously in the background 
	 * 
	 * @author Gowtham
	 */
	private class TagsReaderAsyncTask extends AsyncTask<File, Void, MusicMetadataSet> {

		private Exception e;
		
		@Override
		protected MusicMetadataSet doInBackground(File... files) {
			try {
				MyID3 id3 = new MyID3();
				metadataSet = id3.read(files[0]);
			}
			catch(Exception e) {
				this.e = e;
			}
			return metadataSet;
		}
		
		@Override
		protected void onPostExecute(MusicMetadataSet result) {
			progressDialog.dismiss();
			if(result == null) {
				error(getException());
			} else {
				metadataSet = result;
				metadata = metadataSet.getSimplified();
				showTags(metadata);
			}
		}
		
		public Exception getException() {
			return e;
		}
	}
	
	/** Manages writing tags asynchronously in the background
	 * 
	 * @author Gowtham
	 *
	 */
	private class TagsWriterAsyncTask extends AsyncTask<Void,Void,Object> {

		private Exception e;
		
		@Override
		protected void onPreExecute() {
			metadata.setSongTitle(editTextTitle.getText().toString().trim());
			metadata.setAlbum(editTextAlbum.getText().toString().trim());
			metadata.setArtist(editTextArtist.getText().toString().trim());
			metadata.setGenreName(editTextGenre.getText().toString().trim());			
		}
		
		@Override
		protected Object doInBackground(Void... arg0) {
			
			try {
				File inputFile = new File(audioFilePath);
				File cacheDir = getCacheDir();
				File tempFile = File.createTempFile(inputFile.getName(), "temp", cacheDir);
				new MyID3().write(inputFile, tempFile, metadataSet, metadata);
				
				move(tempFile, inputFile);
			} catch(Exception e) {
				this.e = e;
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			progressDialog.dismiss();
		}
		
		public Exception getException() {
			return this.e;
		}
	}
}
