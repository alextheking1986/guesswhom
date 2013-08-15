package com.sameer.gupshap;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	
	
	EditText et_name,et_email,et_pass;
	String user,pass,email,name;

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	
	//preference file for persistent storage
	public static final String PREFS_NAME = "GupFile";
	
	// sever hostname
	public static final String server="http://paradigmcraft.com/";
	//public static final String server="http://192.168.2.3";
	
	VerifyUser verify; // class for verfiying a user

	//Game music files
	public static final int sniper = R.raw.sniper;
	//public static final int S2 = R.raw.s2;
	//public static final int S3 = R.raw.s3;

	
	// Connection detector class
	ConnectionDetector cd;
	
	private static SoundPool soundPool;
	private static HashMap soundPoolMap;

	/** Populate the SoundPool*/
	public static void initSounds(Context context) {
    soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
	soundPoolMap = new HashMap(3);     

	soundPoolMap.put( sniper, soundPool.load(context, R.raw.sniper, 1) );
/*	soundPoolMap.put( S2, soundPool.load(context, R.raw.s2, 2) );
	soundPoolMap.put( S3, soundPool.load(context, R.raw.s3, 3) );
*/	}
	
	
	 /** Play a given sound in the soundPool */
	 public static void playSound(Context context, int soundID) {
		 
		 try{
	            boolean mStartPlaying = true;
	            MediaPlayer  mPlayer=null;
	            if (mStartPlaying==true){
	                mPlayer = new MediaPlayer();

	                Uri uri = Uri.parse("android.resource://com.sameer.gupshap/" + soundID);
	                mPlayer.setDataSource(context,uri);
	                mPlayer.prepare();
	                mPlayer.start();
	            } 
	            else{
	                mPlayer.release();
	                mPlayer = null;
	            }
	            mStartPlaying = !mStartPlaying;
	        }
	        catch (IOException e){
	            Log.e("error", "prepare() failed");
	        }

	 }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//by passing network error
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		//done
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent=cd.isConnectingToInternet();
		if (!isInternetPresent)
		{
			showAlertDialog(MainActivity.this, "No Internet Connection",
					"You don't have internet connection.", false); 
		}else
		{
			//adding sounds 
			initSounds(MainActivity.this);
			playSound(MainActivity.this, sniper);
			
			
			 // Restore preferences
		       SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		       String name = settings.getString("name", null);
		       String pass = settings.getString("password", null);
		       String email = settings.getString("email", null);
		       
       
		       verify = new VerifyUser();

		       //if the user has already created a login id, getting it shared file
		       if(name != null)
		       {
		    	   //Toast.makeText(getApplicationContext(),"User ::"+name+" pass::"+pass, Toast.LENGTH_LONG).show();
		    	   ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
		   		mDialog.setMessage("Logging in...");
		   		mDialog.setCancelable(true);
		   		mDialog.show();
		   		if (verify.checkUser(name, pass))
		   		{
		   		mDialog.dismiss();
		     //   Toast.makeText(getApplicationContext(),"User ::"+name+" pass::"+pass, Toast.LENGTH_LONG).show();
		    	   Intent nextScreen = new Intent(getApplicationContext(), PlayScreen.class);
		    	 //Sending data to another Activity
				nextScreen.putExtra("name", name);
				nextScreen.putExtra("pass", pass);
				nextScreen.putExtra("email", email);
		    	   startActivity(nextScreen);
		   		}
		   		else
		   		{
		   			mDialog.dismiss();
		   		}
		       }

		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}



	public void onSignUp(View view)
	{
		

		ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
		mDialog.setMessage("Registering...");
		mDialog.setCancelable(true);
		mDialog.show();
	
		String user,pass,email;
		int status;

		et_name= (EditText)findViewById(R.id.reg_name);
		et_pass= (EditText)findViewById(R.id.reg_password);
		et_email=(EditText)findViewById(R.id.reg_email);
 
	
		//getting data from form 
		user=et_name.getText().toString();
		pass=et_pass.getText().toString();
		email=et_email.getText().toString();
		if(user.length()<3)
		{
			Toast.makeText(getApplicationContext(),"USER NAME MUST BE OF ATLEAST 3 CHARACTERS", Toast.LENGTH_LONG).show();
		}
		else if (pass.length()<3)
		{
			Toast.makeText(getApplicationContext(),"PASSWORD MUST BE OF ATLEAST 3 CHARACTERS", Toast.LENGTH_LONG).show();
		}
		else if(email.length()<7)
		{
			Toast.makeText(getApplicationContext(),"ENTER A VALID EMAIL ID", Toast.LENGTH_LONG).show();
		}
		else
		{
		//Toast.makeText(getApplicationContext(),"User ::"+user+" pass::"+pass, Toast.LENGTH_LONG).show();

		status=createUser(user, pass,email);
		mDialog.dismiss();
		if (status == 0)
		{
			Log.i("error", "Unable to create user");
			Toast.makeText(getApplicationContext(),"Unable to create user,please try again !!", Toast.LENGTH_LONG).show();
		}
		else if(status == 1)
		{
			Log.i("info", "user created successfully");
			Toast.makeText(getApplicationContext(),"User created successfully", Toast.LENGTH_LONG).show();
	
			// All objects are from android.context.Context
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putString("name", user);
		    editor.putString("password", pass);
		    editor.putString("email", email);

		    // Commit the edits!
		    editor.commit();
		    Intent nextScreen = new Intent(getApplicationContext(), PlayScreen.class);
	    	 //Sending data to another Activity
			nextScreen.putExtra("name", user);
			nextScreen.putExtra("pass", pass);
			nextScreen.putExtra("email", email);
	    	   startActivity(nextScreen);

		}
		else 
		{
			Log.i("info", "User already exists, try using some other name");
			Toast.makeText(getApplicationContext(),"User already exists, try using some other name", Toast.LENGTH_LONG).show();
		}



		}
	
		

		//Sending data to another Activity
		//     nextScreen.putExtra("name", inputName.getText().toString());
		//     nextScreen.putExtra("email", inputEmail.getText().toString());

		//     Log.e("n", inputName.getText()+"."+ inputEmail.getText());
		/*Intent nextScreen = new Intent(getApplicationContext(), Signup.class);
		startActivity(nextScreen);*/

	}

	public boolean checkScreen()
	{
		
		
		
		return false;		
	}
	

	public void onPress(View view)
	{

		verify = new VerifyUser();

		ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
		mDialog.setMessage("Logging in...");
		mDialog.setCancelable(true);
		mDialog.show();
		
		
		boolean status;

		et_name= (EditText)findViewById(R.id.reg_name);
		et_pass= (EditText)findViewById(R.id.reg_password);
		et_email=(EditText)findViewById(R.id.reg_email);

		
		//getting data from form 
		user=et_name.getText().toString();
		pass=et_pass.getText().toString();
		email=et_pass.getText().toString();
		
		
		

		//status=createUser(user, pass);
		status = verify.checkUser(user,pass);
		mDialog.dismiss();
		if (status == false)
		{
			Log.i("error", "Unable to Login");
			Toast.makeText(getApplicationContext(),"Unable to login,check username/password !!", Toast.LENGTH_LONG).show();
		}
		else if(status == true)
		{
			Log.i("info", "user logged in successfully");
			Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_LONG).show();

			// All objects are from android.context.Context
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("name", user);
			editor.putString("password", pass);
			editor.putString("email", email);

			// Commit the edits!
			editor.commit();

			//Toast.makeText(getApplicationContext(),"User ::"+name+" pass::"+pass, Toast.LENGTH_LONG).show();
			Intent nextScreen = new Intent(getApplicationContext(), PlayScreen.class);
			nextScreen.putExtra("name", user);
			nextScreen.putExtra("pass", pass);
			editor.putString("email", email);
			startActivity(nextScreen);

		}
		else 
		{
			Log.i("info", "Incorrect username / password");
			Toast.makeText(getApplicationContext(),"Incorrect username / password", Toast.LENGTH_LONG).show();
		}
		
	}
	
	public int createUser(String name,String pass,String email)
	{  
		/*AsyncTask<String,Void,InputStream> ist;
		GetJSONFromUrl getJson = null;
		ist=getJson.execute(server+"/post.php");*/
		
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(server+"/post.php");


		JSONObject json = new JSONObject();
		String text = null;
		int succ = 0;
		String msg = null;
		JSONArray jArray=null;
		TextView tv ;
		// tv = (TextView)findViewById(R.id.editText1);

		try {
			// JSON data:
			json.put("name", name);
			json.put("pass", pass);
			json.put("email", email);

			//	json.put("first", "no");

			JSONArray postjson=new JSONArray();
			postjson.put(json);

			// Post the data:
			httppost.setHeader("json",json.toString());
			httppost.getParams().setParameter("jsonpost",postjson);

			// Execute HTTP Post Request
			System.out.print(json);
			HttpResponse response = httpclient.execute(httppost);
			//String responseString = getHttpResponseContent(response);
			// for JSON:
			InputStream is = response.getEntity().getContent();

			String temp="";



			if(response != null)
			{
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(
							is, "iso-8859-1"), 8);
					StringBuilder sb = new StringBuilder();
					String js = "";
					String line = null;
					JSONObject json_data=null;

					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
						temp=line;

					}
					is.close();
					js=temp;
					Log.i("info",js);

					JSONObject jobject = new JSONObject(js);

					Log.i("info",js);
					succ=jobject.getInt("success");
					msg=jobject.getString("message");

				   	Toast.makeText(getBaseContext(), ""+msg, Toast.LENGTH_LONG).show();
					Log.i("info","success::"+succ+" Message::"+msg);
					return succ;

				} catch (Exception e) {
					Log.e("error", "Error converting result " + e.toString());
					Toast.makeText(getBaseContext(), "Some Problem Occurred", Toast.LENGTH_LONG).show();
				}


			}




		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(getBaseContext(), "Some Problem Occurred", Toast.LENGTH_LONG).show();
			Log.e("error", "Error occurred " + e.toString());
		}


		return 0;
	}

	
	
	
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting alert dialog icon
		alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	public void postData() throws JSONException{  
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://192.168.2.3/post.php");
		JSONObject json = new JSONObject();
		String text = null;
		TextView tv ;
		// tv = (TextView)findViewById(R.id.editText1);

		try {
			// JSON data:
			json.put("name", "Syed Sameer");
			json.put("position", 123);
			json.put("avail", 1);
			json.put("text", "I am here");
			//	json.put("first", "no");

			JSONArray postjson=new JSONArray();
			postjson.put(json);

			// Post the data:
			httppost.setHeader("json",json.toString());
			httppost.getParams().setParameter("jsonpost",postjson);

			// Execute HTTP Post Request
			System.out.print(json);
			HttpResponse response = httpclient.execute(httppost);

			// for JSON:
			if(response != null)
			{
				InputStream is = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				text = sb.toString();
			}

			/*	String fd_id = null;
			String fd_name = null;
			JSONArray jArray=null;
			 */
			/*	try{
	      	jArray = new JSONArray(text);
	      	JSONObject json_data=null;

	      	for(int i=0;i<jArray.length();i++){
					json_data = jArray.getJSONObject(i);
					fd_id=json_data.getString("name");
					fd_name=json_data.getString("position");
	      	}

			}catch(JSONException e1){
				Toast.makeText(getBaseContext(), "No Person Found", Toast.LENGTH_LONG).show();
			}catch (ParseException e1){
				e1.printStackTrace();
			}*/

			//tv.setText(text);

		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}


	
	
	
		
}
