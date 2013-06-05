package com.gowtham.mp3tagdroid;

import java.io.File;
import java.util.Vector;

import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.ImageData;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MP3TagViewerActivity extends Activity implements OnClickListener {

	private static final int REQUEST_CODE = 1;
	private Button buttonLoadAudioFile;
	private TextView textViewAudioFilePath, editTextTitle, editTextAlbum, editTextArtist, editTextGenre;
	private ImageView imageAlbumArt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_tag_viewer);
		
		buttonLoadAudioFile = (Button) findViewById(R.id.buttonLoadAudioFile);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mp3_tag_viewer, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.buttonLoadAudioFile:
				Toast.makeText(this, "Hey", Toast.LENGTH_LONG).show();
				showFileBrowser();
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
		MyID3 id3 = new MyID3();
		
		MusicMetadataSet meta = id3.read(new File(path));
		IMusicMetadata metadata = meta.getSimplified();
		showTags(metadata);
	}
	
	private void showTags(IMusicMetadata metadata) {
		clearTags();
		editTextTitle.setText(metadata.getSongTitle());
		editTextAlbum.setText(metadata.getAlbum());
		editTextArtist.setText(metadata.getArtist());
		editTextGenre.setText(metadata.getGenreName());
		Vector pictures = metadata.getPictures();
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
			loadTagsInternal(path);
		}
		catch(Exception e) {
			error(e);
		}
	}
	
	private void error(Exception e) {
		Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
		t.show();
	}
	
	
}
