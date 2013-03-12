package com.sameer.gupshap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	// flag for Internet connection status
    Boolean isInternetPresent = false;
 
    // Connection detector class
	ConnectionDetector cd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    
    public void onSignUp(View view)
    {
    	Intent nextScreen = new Intent(getApplicationContext(), Signup.class);
    	 
        //Sending data to another Activity
   //     nextScreen.putExtra("name", inputName.getText().toString());
   //     nextScreen.putExtra("email", inputEmail.getText().toString());

   //     Log.e("n", inputName.getText()+"."+ inputEmail.getText());

        startActivity(nextScreen);
    	
    }
    
    
    public void onPress(View view)
    {/*
    	
    	
    	
    	
    	
    	TextView t;
    	String name;
    	String result = null;
		InputStream is = null;
		StringBuilder sb=null;
		JSONArray jArray=null;
		
		
    	 cd = new ConnectionDetector(getApplicationContext());
    	 isInternetPresent=cd.isConnectingToInternet();
    	 if (!isInternetPresent)
    	 {
    		 showAlertDialog(MainActivity.this, "No Internet Connection",
                     "You don't have internet connection.", false); 
    	 }
    	 else
    	 {
    		 ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
             mDialog.setMessage("Registering...");
             mDialog.setCancelable(true);
             mDialog.show();
             
    		 t =(TextView) findViewById(R.id.editText1);
    	     name=(String) t.getText();
    	     Log.i("info","sending name to db"+name);	
    		 
    		 
    		 TextView tv ;
    		 tv = (TextView)findViewById(R.id.editText1);
    		 
    	
		//http post
				try{
					HttpClient httpclient = new DefaultHttpClient();
				//	HttpPost httppost = new HttpPost("http://127.0.0.1/food.php");
					HttpPost httppost = new HttpPost("http://192.168.2.3/food.php");
					
					
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					
					//food_id":"1","food_name":"apple"
			        nameValuePairs.add(new BasicNameValuePair("food_id", "1"));
			        nameValuePairs.add(new BasicNameValuePair("food_name", "apple"));

					
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					is = entity.getContent();
				}catch(Exception e){
					Log.e("log_tag", "Error in http connection"+e.toString());
				}
			
				//convert response to string
				try{
					BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
					sb = new StringBuilder();
					sb.append(reader.readLine() + "\n");
					String line="0";
			     
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					
					is.close();
					
					result=sb.toString();
					
					
				}catch(Exception e){
					Log.e("log_tag", "Error converting result "+e.toString());
				}

				//paring data
				int fd_id;
				String fd_name = null;
				try{
		      	jArray = new JSONArray(result);
		      	JSONObject json_data=null;
		      	
		      	for(int i=0;i<jArray.length();i++){
						json_data = jArray.getJSONObject(i);
						fd_id=json_data.getInt("food_id");
						fd_name=json_data.getString("food_name");
		      	}
		      	
				}catch(JSONException e1){
					Toast.makeText(getBaseContext(), "No Food Found", Toast.LENGTH_LONG).show();
				}catch (ParseException e1){
					e1.printStackTrace();
				}
		
		try {
			postData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	     mDialog.dismiss();
		
    	
   
    	//tv.setText("hello world");
    	
    
    	 }
    */}
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
