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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MP3TagViewerActivity extends Activity implements OnClickListener {

	private static final int REQUEST_CODE = 1;
	private Button buttonLoadAudioFile, buttonSaveTags;
	private TextView textViewAudioFilePath, editTextTitle, editTextAlbum, editTextArtist, editTextGenre, textViewAlbumArtLabel;
	private ImageView imageAlbumArt;
	
	private String audioFilePath;
	private MusicMetadataSet metadataSet;
	private IMusicMetadata metadata;
	private ProgressDialog progressDialog;
	private boolean deleteAlbumArt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_tag_viewer);
		
		buttonLoadAudioFile = (Button) findViewById(R.id.buttonLoadAudioFile);
		buttonSaveTags = (Button) findViewById(R.id.buttonSave);
		
		textViewAudioFilePath = (TextView) findViewById(R.id.textViewAudioFilePath);
		textViewAlbumArtLabel = (TextView) findViewById(R.id.textViewAlbumArtLabel);
		editTextTitle = (TextView) findViewById(R.id.editTextTitle);
		editTextAlbum = (TextView) findViewById(R.id.editTextAlbum);
		editTextArtist = (TextView) findViewById(R.id.editTextArtist);
		editTextGenre = (TextView) findViewById(R.id.editTextGenre);
		imageAlbumArt = (ImageView) findViewById(R.id.imageViewAlbumArt);
		
				
		
		setListeners();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
//		SharedPreferences pref = getSharedPreferences("tag_details", MODE_PRIVATE);
//		String savedPath = pref.getString("audio_file_path", null);
//		if(savedPath == null)
//			return;
//		audioFilePath = savedPath;
//		if(!new File(audioFilePath).exists())
//			return;
//		// After loading the audio file again, overwrite the tags in UI with user set values
//		loadTags(audioFilePath);
//		if(pref.contains("title"))
//			editTextTitle.setText(pref.getString("title", ""));
//		if(pref.contains("album"))
//			editTextAlbum.setText(pref.getString("album", ""));
//		if(pref.contains("artist"))
//			editTextArtist.setText(pref.getString("artist", ""));
//		if(pref.contains("genre"))
//			editTextGenre.setText(pref.getString("genre", ""));
//		if(pref.contains("delete_album_art_image"))
//			deleteAlbumArt = pref.getBoolean("delete_album_art_image", false);
//		
//		Editor editor = pref.edit();
//		editor.clear();
//		editor.commit();
//		
//		Toast.makeText(this, "Prefs loaded", Toast.LENGTH_LONG).show();
//		if(deleteAlbumArt)
//			markAlbumArtForDeletion();
//		
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
//		SharedPreferences pref = getSharedPreferences("tag_details", MODE_PRIVATE);
//		Editor editor = pref.edit();
//		
//		editor.putString("audio_file_path", audioFilePath);
//		if(editTextTitle.getText().length()!=0)
//			editor.putString("title", editTextTitle.getText().toString());
//		if(editTextAlbum.getText().length()!=0)
//			editor.putString("album", editTextAlbum.getText().toString());
//		if(editTextArtist.getText().length()!=0)
//			editor.putString("artist", editTextArtist.getText().toString());
//		if(editTextGenre.getText().length()!=0)
//			editor.putString("genre", editTextGenre.getText().toString());
//		
//		editor.putBoolean("delete_album_art_image", deleteAlbumArt);
//		editor.commit();
	}

	private void setListeners() {
		buttonLoadAudioFile.setOnClickListener(this);
		buttonSaveTags.setOnClickListener(this);
		registerForContextMenu(imageAlbumArt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mp3_tag_viewer, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if( v.getId() == R.id.imageViewAlbumArt ) {
			AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(R.string.album_art_context_menu_title);
			getMenuInflater().inflate(R.menu.album_art_context_menu, menu);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_remove_album_art:
			markAlbumArtForDeletion();
			break;
		case R.id.menu_revert_album_art:
			revertDeletedAlbumArt();
			break;
		default:
			break;
		}
		return true;
	}

	private void revertDeletedAlbumArt() {
		deleteAlbumArt = false;
		imageAlbumArt.getDrawable().setAlpha(255);
	}

	private void markAlbumArtForDeletion() {
		deleteAlbumArt = true;
		imageAlbumArt.getDrawable().setAlpha(64);
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
			textViewAlbumArtLabel.setVisibility(View.VISIBLE);
			imageAlbumArt.setVisibility(View.VISIBLE);
			ImageData image = (ImageData)pictures.get(0);
			Bitmap bmp = BitmapFactory.decodeByteArray(image.imageData, 0, image.imageData.length);
			imageAlbumArt.setImageBitmap(bmp);
		}
	}
	
	private void clearTags() {
		deleteAlbumArt = false;
		editTextTitle.setText("");
		editTextAlbum.setText("");
		editTextArtist.setText("");
		editTextGenre.setText("");
		textViewAlbumArtLabel.setVisibility(View.INVISIBLE);
		imageAlbumArt.setVisibility(View.INVISIBLE);
		//imageAlbumArt.setImageResource(android.R.color.transparent);
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
		task.execute((Void[])null);
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
	private class TagsReaderAsyncTask extends AsyncTask<File, String, MusicMetadataSet> {

		private Exception e;
		
		@Override
		protected MusicMetadataSet doInBackground(File... files) {
			try {
				publishProgress("Reading audio file...");
				MyID3 id3 = new MyID3();
				metadataSet = id3.read(files[0]);
				
				publishProgress("Done");
			}
			catch(Exception e) {
				this.e = e;
			}
			return metadataSet;
		}
		
		@Override
		protected void onProgressUpdate(String... msgs) {
			progressDialog.setMessage(msgs[0]);
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
	private class TagsWriterAsyncTask extends AsyncTask<Void,String,Object> {

		private Exception e;
		
		@Override
		protected void onPreExecute() {
			metadata.setSongTitle(editTextTitle.getText().toString().trim());
			metadata.setAlbum(editTextAlbum.getText().toString().trim());
			metadata.setArtist(editTextArtist.getText().toString().trim());
			metadata.setGenreName(editTextGenre.getText().toString().trim());
			if(deleteAlbumArt)
				metadata.clearPictures();
		}
		
		@Override
		protected Object doInBackground(Void... arg0) {
			
			try {
				publishProgress("Preparing files...");
				File inputFile = new File(audioFilePath);
				File cacheDir = getCacheDir();
				File tempFile = File.createTempFile(inputFile.getName(), "temp", cacheDir);
				
				publishProgress("Compiling and writing tags...");
				new MyID3().write(inputFile, tempFile, metadataSet, metadata);
				
				publishProgress("Moving file");
				move(tempFile, inputFile);
				
				publishProgress("Done");
			} catch(Exception e) {
				this.e = e;
			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String... msg) {
			progressDialog.setMessage(msg[0]);
		}
		
		@Override
		protected void onPostExecute(Object result) {
			progressDialog.dismiss();
			if(e != null)
				error(e);
		}
		
		public Exception getException() {
			return this.e;
		}
	}
}
