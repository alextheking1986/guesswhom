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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatWindow extends Activity{

	/**
	 * @param args
	 */
	String otherUser,otherEmail,user_id,oth_msg,myId,myEmail,myName,tmp;
	static String user_msg="",my_msg="";
	String result;
	TextView tv;
	ImageView otherIv,myIv;
	EditText et;
	ProgressDialog pDialog;
	JSONParser jsonParser;
	DatabaseHandler db;
	List <String>msg_rec;
	
	public void displayMessage(String id)
	{
		EditText et1;
		et1= (EditText)findViewById(R.id.EditTextChatConversation);
		et1.setText("");
		 msg_rec = db.getChat(id);
        	  for(int i=0;i<msg_rec.size();i++)
        	  {
        		  et1.append(msg_rec.get(i)+"\n");
        	  }
 	         
     		 //et1.setText("\n"+msg_rec+"\n");
      
		
	}
	
	public void onClear(View view)
	{
		db.deleteChat(user_id);
		displayMessage((user_id));
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		//Creating database handler.....
        db = new DatabaseHandler(this);
        msg_rec = new ArrayList<String>();
		
		Intent i = getIntent();
        // Receiving the Data
         otherUser = i.getStringExtra("user_name");
         otherEmail = i.getStringExtra("user_email");
         user_id =i.getStringExtra("user_id");
         myName = i.getStringExtra("my_name");
         myEmail = i.getStringExtra("my_email");
         myId =i.getStringExtra("my_id");
        // msg_rec=i.getStringExtra("user_msg");
         displayMessage((user_id));
         
        // msg_rec = db.getChat(Integer.parseInt(user_id));
         tv = (TextView) findViewById(R.id.TextViewChatOtherUserDetails);
         tv.setText(otherUser);
         otherIv= (ImageView)findViewById(R.id.ImageViewChatOtherUserPicture);
         otherIv.setImageResource(R.drawable.face);
         /*myIv= (ImageView)findViewById(R.id.ImageViewChatMyPicture);
         myIv.setImageResource(R.drawable.face2);*/
         jsonParser = new JSONParser();
         user_msg= myId+";"+myName+";"+myEmail+";";
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void onSend(View view)
	{	 
		
		//SendMessage sm = null;
		et = (EditText)findViewById(R.id.EditTextChatMyMessage);
		user_msg += et.getText().toString();
		my_msg = et.getText().toString();
		 Log.d("Insert: ", "Inserting the send msg in DB ..");
	     db.addChat(new User(user_id,myName,my_msg,"S"));
		new SendMessage().execute();
		
	}
	
	
	
	//
	/**
	 * Background Async Task to Load all tracks under one album
	 * */
	class SendMessage extends AsyncTask<String, String, String> {

		

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChatWindow.this);
			pDialog.setMessage("Sending ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
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

		
			result = json;
			return json;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all tracks
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					/*ListAdapter adapter = new SimpleAdapter(
							TrackListActivity.this, tracksList,
							R.layout.list_item_tracks, new String[] { "album_id", TAG_ID, "track_no",
									TAG_NAME, TAG_DURATION }, new int[] {
									R.id.album_id, R.id.song_id, R.id.track_no, R.id.album_name, R.id.song_duration });
					// updating listview
					setListAdapter(adapter);
					
					// Change Activity Title with Album name
					setTitle(album_name);*/
					EditText et1;
					et = (EditText)findViewById(R.id.EditTextChatMyMessage);
					et.setText("");
					
					displayMessage((user_id));
					
					
				}
			});

		}

	}
	

}
