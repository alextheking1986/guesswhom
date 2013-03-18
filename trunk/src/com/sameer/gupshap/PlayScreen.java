/**
 * 
 */
package com.sameer.gupshap;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author sameer
 *
 */
public class PlayScreen extends Activity {
	String name,pass;
	TextView tv;
	/** Sound variables */
	private SoundPool sounds;
	private int sExplosion;
	
	
	public static final String PREFS_NAME = "GupFile";
	//public static final String server="http://192.168.2.3";
	public static final String server="http://paradigmcraft.com/";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playscreen);
        
        Intent i = getIntent();
        // Receiving the Data
         name = i.getStringExtra("name");
         pass = i.getStringExtra("pass");
         tv = (TextView)findViewById(R.id.username);
         Log.i("info",name);
         tv.setText(name);
         
         sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
         sExplosion = sounds.load(PlayScreen.this, R.raw.sniper, 1);
       //  sounds.play(sExplosion, 1.0f, 1.0f, 0, 0, 1.5f);
    }
	

	/**
	 * @param args
	 */
		
	public void onPlay(View view)
	{
		
	}
	
	public void onInstructions(View view)
	{
		
	}
	
	public void onExit(View view)
	{
		//this.finish();
		finish();
	}

}
