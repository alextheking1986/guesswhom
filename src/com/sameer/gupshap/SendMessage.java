/**
 * 
 */
package com.sameer.gupshap;

import static com.sameer.gupshap.CommonUtilities.SEND_URL;
import static com.sameer.gupshap.CommonUtilities.TAG_ID;
import static com.sameer.gupshap.CommonUtilities.TAG_MSG;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;




/**
 * @author sameer
 *
 */
public class SendMessage extends Activity {
	ProgressDialog pDialog;
	String user_id,user_msg;
	JSONParser jsonParser;
	/**
	 * @param args
	 */
	
	public void send(String id, String msg)
	{
		user_id =id;
		user_msg = msg;
	    jsonParser = new JSONParser();
	    new SendMsg().execute();
	}
	
	/**
	 * Background Async Task to Load all tracks under one album
	 * */
	class SendMsg extends AsyncTask<String, String, String> {
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}

		/**
		 * getting tracks json and parsing
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			String json =" sms bheja";
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			// post album id as GET parameter
			params.add(new BasicNameValuePair(TAG_ID, user_id));
			params.add(new BasicNameValuePair(TAG_MSG, user_msg));

			// getting JSON string from URL
			try
			{
				Log.i("before http request",params.toString());
				Log.i("before http request",SEND_URL);
				
			 json = jsonParser.makeHttpRequest(SEND_URL,"GET",params);
			}
			catch (Exception e)
			{
				Log.e("error",e.toString());
			}

			// Check your log cat for JSON reponse
			Log.d("Sending message: ", user_msg+"  ::to:: "+user_id+" ::json::: "+json);

		
			/*result = json;*/
			return json;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all tracks
			/*pDialog.dismiss();*/
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					
					
				}
			});

		}

	}	

}
