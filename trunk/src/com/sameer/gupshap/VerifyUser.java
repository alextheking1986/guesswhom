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

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author sameer
 *
 */
public class VerifyUser {
	
	// All the properties for the class
		//public static final String server="http://192.168.2.3";
		public static final String server="http://paradigmcraft.com/";
		public static final String PREFS_NAME = "GupFile";

	/**
	 * @param args
	 */
		
		/* return codes
		 * success =1  : user created
		 * success =2  : user already exists
		 * success =0  : error occured in creating user
		 */
	public boolean checkUser(String user,String pass)
	{
		  
				// Create a new HttpClient and Post Header
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(server+"/checkuser.php");


				JSONObject json = new JSONObject();
				String text = null;
				int succ = 0;
				String msg = null;
				JSONArray jArray=null;
				TextView tv ;
				// tv = (TextView)findViewById(R.id.editText1);

				try {
					// JSON data:
					json.put("name", user);
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
							if (succ == 2)
							{
							return true;
							}
							else
							{
								return false;
							}

						} catch (Exception e) {
							Log.e("error", "Error converting result " + e.toString());
						//	Toast.makeText(getBaseContext(), "Some Problem Occurred", Toast.LENGTH_LONG).show();
						}


					}




				} catch (Exception e) {
					// TODO Auto-generated catch block
			//		Toast.makeText(getBaseContext(), "Some Problem Occurred", Toast.LENGTH_LONG).show();
					Log.e("error", "Error occurred " + e.toString());
				}


				
			
		
		return false;
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
