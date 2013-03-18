/**
 * 
 */
package com.sameer.gupshap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author sameer
 * Signup screen 
 */
public class Signup extends Activity {

	// All the properties for the class
	//public static final String server="http://192.168.2.3";
	public static final String server="http://paradigmcraft.com/";
	public static final String PREFS_NAME = "GupFile";
	//String server="http://paradigmcraft.com/";


	/**
	 * @param args
	 */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
	}

	public void onSignUp(View view)
	{
		ProgressDialog mDialog = new ProgressDialog(Signup.this);
		mDialog.setMessage("Registering...");
		mDialog.setCancelable(true);
		mDialog.show();
		EditText et1,et2;
		String user,pass;
		int status;

		et1= (EditText)findViewById(R.id.editText1);
		et2= (EditText)findViewById(R.id.editText2);

		//getting data from form 
		user=et1.getText().toString();
		pass=et2.getText().toString();

		status=createUser(user, pass);
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

		    // Commit the edits!
		    editor.commit();
		    Intent nextScreen = new Intent(getApplicationContext(), PlayScreen.class);
	    	 //Sending data to another Activity
			nextScreen.putExtra("name", user);
			nextScreen.putExtra("pass", pass);
	    	   startActivity(nextScreen);

		}
		else 
		{
			Log.i("info", "User already exists, try using some other name");
			Toast.makeText(getApplicationContext(),"User already exists, try using some other name", Toast.LENGTH_LONG).show();
		}




	}

	public int createUser(String name,String pass)
	{  
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

					//		      	Toast.makeText(getBaseContext(), ""+msg, Toast.LENGTH_LONG).show();
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

}
