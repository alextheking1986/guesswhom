/**
 * 
 */
package com.sameer.gupshap;

import static com.sameer.gupshap.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.sameer.gupshap.CommonUtilities.EXTRA_MESSAGE;
import static com.sameer.gupshap.CommonUtilities.SENDER_ID;
import static com.sameer.gupshap.CommonUtilities.GAME_REQUEST;
import static com.sameer.gupshap.CommonUtilities.GAME_ACCEPT;
import static com.sameer.gupshap.CommonUtilities.GAME_REJECT;
import static com.sameer.gupshap.CommonUtilities.GAME_ACK;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gcm.GCMRegistrar;

/**
 * @author sameer
 * 
 */
public class PlayScreen extends Activity {

	// Connection detector
	ConnectionDetector cd;
	// albums JSON url
	private static final String URL_USERS = "http://paradigmcraft.com/gcm_server_php/getUsers.php";
	public static final String PREFS_NAME = "GupFile";
	// public static final String server="http://192.168.2.3";
	public static final String server = "http://paradigmcraft.com/";
	// ALL JSON node names
	private static final String TAG_ID = "gcm_regid";
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG = "play";
	// Progress Dialog
	private ProgressDialog pDialog;
	// Creating JSON Parser object
	JSONParser jsonParser = new JSONParser();
	// user lists
	ArrayList<HashMap<String, String>> usersList;
	// albums JSONArray
	JSONArray users = null;
	// variable declaration
	static String name;
	static String email;
	String pass;
	// registration id with GCM
	String regId, othID, temp;
	// Views
	TextView tv;
	TextView lblMessage;
	/** Sound variables */
	/*
	 * private SoundPool sounds; private int sExplosion;
	 */
	// Some classes
	DatabaseHandler db;
	SendMessage sms;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	// Alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	/****************************************************************************************************/

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);

		// Creating database .....
		db = new DatabaseHandler(this);
		sms = new SendMessage();

		// getting data from previous screen
		Intent i = getIntent();
		// Receiving the Data
		name = i.getStringExtra("name");
		pass = i.getStringExtra("pass");
		email = i.getStringExtra("email");

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		// registering for recieving messages
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			// Registration is not present, register now with GCM
			GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				// Toast.makeText(getApplicationContext(),
				// "Already registered with GCM", Toast.LENGTH_LONG).show();
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, name, email, regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}

		// tv = (TextView)findViewById(R.id.username);
		Log.i("info", name);
		// tv.setText(name);

		// Hashmap for ListView
		usersList = new ArrayList<HashMap<String, String>>();

		// Loading Albums JSON in Background Thread
		new LoadUsers().execute();

		// get listview
		ListView lv = (ListView) findViewById(android.R.id.list);
		/**
		 * Listview item click listener TrackListActivity will be lauched by
		 * passing album id
		 * */
		lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// on selecting a single person
				// ChatWindow will be launched
				final String user_name = ((TextView) view
						.findViewById(R.id.user_name)).getText().toString();
				final String user_email = ((TextView) view
						.findViewById(R.id.user_email)).getText().toString();
				final String user_id = ((TextView) view
						.findViewById(R.id.user_id)).getText().toString();
				// Creating alert Dialog with two Buttons

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						PlayScreen.this);

				// Setting Dialog Title
				alertDialog.setTitle("START THE GAME ...");

				// Setting Dialog Message
				alertDialog.setMessage("Do you want to challenge " + user_name
						+ " ?");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.tick);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								Intent i = new Intent(getApplicationContext(),
										ChatWindow.class);
								sms.send(user_id, regId + ";" + name + ";"
										+ email + ";" + "#game#chlng#");
								Toast.makeText(getApplicationContext(),
										"Challenge sent", Toast.LENGTH_SHORT)
										.show();
								// send album id to tracklist activity to get
								// list of songs under that album
								i.putExtra("user_email", user_email);
								i.putExtra("user_name", user_name);
								i.putExtra("user_id", user_id);
								i.putExtra("my_email", email);
								i.putExtra("my_name", name);
								i.putExtra("my_id", regId);

								// startActivity(i);

							}
						});
				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								Toast.makeText(getApplicationContext(),
										"HAW !!, You seem Afraid...",
										Toast.LENGTH_SHORT).show();
								dialog.cancel();
							}
						});

				// Showing Alert Message
				alertDialog.show();

			}
		});

	}

	/**
	 * @param args
	 */

	public void onPlay(View view) {

	}

	public void onInstructions(View view) {

	}

	public void onExit(View view) {
		// this.finish();
		finish();
	}

	/**
	 * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);

			// String newMessage = "";
			// Toast.makeText(getApplicationContext(), "New Message: " +
			// newMessage, Toast.LENGTH_LONG).show();
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */
			
			// Message Recieved

			final String user_name;
			final String user_email;
			final String user_id;
			String text = "";
			String msg[];
			msg = newMessage.split(";");
			user_id = msg[0];
			user_name = msg[1];
			user_email = msg[2];
			for (int i = 3; i < msg.length; i++) {
				text += msg[i];
			}
			// checking if the message recieved is a game request
			if (text.equals(GAME_REQUEST)) {
				// GAME REQUEST RECIEVED
				Log.d(TAG, "Game request recieved from ::" + user_name);

				// Displaying dialog for the game request
				// Creating alert Dialog with two Buttons

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						PlayScreen.this);

				// Setting Dialog Title
				alertDialog.setTitle("NEW CHALLENGE ...");

				// Setting Dialog Message
				alertDialog
						.setMessage("DO YOU WANT TO ACCEPT THE CHALLENGE FROM "
								+ user_name + " ?");

				// Setting Icon to Dialog
				alertDialog.setIcon(R.drawable.tick);

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								// Intent i = new
								// Intent(getApplicationContext(),
								// ChatWindow.class);
								// startActivity(i);
								sms.send(user_id, regId + ";" + name + ";"
										+ email + ";" + GAME_ACCEPT);
								Toast.makeText(getApplicationContext(),
										"Challenge accepted",
										Toast.LENGTH_SHORT).show();
								// start playing
								
								pDialog = new ProgressDialog(PlayScreen.this);
								pDialog.setMessage("Waiting for "+user_name+" .....");
								pDialog.setIndeterminate(false);
								pDialog.setCancelable(false);
								pDialog.show();
								
								// starting coin flip application 
								Intent i = new Intent(getApplicationContext(),CoinFlip.class);
								i.putExtra("user_email", user_email);
								i.putExtra("user_name", user_name);
								i.putExtra("user_id", user_id);
								i.putExtra("my_email", email);
								i.putExtra("my_name", name);
								i.putExtra("my_id", regId);
								i.putExtra("player", "player1");

								startActivity(i);

							}
						});
				// Setting Negative "NO" Button
				alertDialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to execute after dialog
								sms.send(user_id, regId + ";" + name + ";"
										+ email + ";" + GAME_REJECT);
								Toast.makeText(getApplicationContext(),
										"Challenge Declined ...",
										Toast.LENGTH_SHORT).show();
								dialog.cancel();
							}
						});

				// Showing Alert Message
				alertDialog.show();

			} else if (text.equals(GAME_ACCEPT)) {
				// check if user is already playing or not , to be implemented
				// .....

				Toast.makeText(getApplicationContext(),"Challenge Request Accepted from " + user_name,Toast.LENGTH_SHORT).show();

				// send ok and open play screen
				sms.send(user_id, regId + ";" + name + ";" + email + ";"+ GAME_ACK);
				
			} else if (text.equals(GAME_REJECT)) {
				Toast.makeText(getApplicationContext(),
						"Challenge Request Declined from " + user_name,
						Toast.LENGTH_SHORT).show();
				
			}
			else if (text.equals(GAME_ACK)) {
				//Toast.makeText(getApplicationContext(),	"Challenge Request Declined from " + user_name,Toast.LENGTH_SHORT).show();
				pDialog.dismiss();
				Intent i = new Intent(getApplicationContext(),CoinFlip.class);
				i.putExtra("user_email", user_email);
				i.putExtra("user_name", user_name);
				i.putExtra("user_id", user_id);
				i.putExtra("my_email", email);
				i.putExtra("my_name", name);
				i.putExtra("my_id", regId);
				i.putExtra("player", "player1");

				startActivity(i);

				
			}

			Intent i = new Intent(getApplicationContext(), ChatWindow.class);
			i.putExtra("user_email", user_email);
			i.putExtra("user_name", user_name);
			i.putExtra("user_id", user_id);
			i.putExtra("my_email", email);
			i.putExtra("my_name", name);
			i.putExtra("my_id", regId);
			i.putExtra("user_msg", text);

			// Integer.parseInt(user_id);
			// Inserting Chat
			Log.d("Insert: ", "Inserting ..");
			db.addChat(new User(user_id, user_name, text, "R"));

			// Temporary disabling chat window in order to accept game requests
			// startActivity(i);

			// Toast.makeText(getApplicationContext(), "New Message: " +
			// user_id+"::"+newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {             
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	/**
	 * Background Async Task to Load all Albums by making http request
	 * */
	class LoadUsers extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PlayScreen.this);
			pDialog.setMessage("Listing Users ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Albums JSON
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			// getting JSON string from URL
			String json = jsonParser.makeHttpRequest(URL_USERS, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("Users JSON: ", "> " + json);

			try {
				users = new JSONArray(json);
				Log.i("info", "my name is ::" + name);
				if (users != null) {
					// looping through All users
					for (int i = 0; i < users.length(); i++) {
						JSONObject c = users.getJSONObject(i);

						// Storing each json item values in variable
						String id = c.getString(TAG_ID);
						String names = c.getString(TAG_NAME);
						String email = c.getString(TAG_EMAIL);

						// Temporary allowing self name to appear in order to
						// test the application
						//if (!names.equalsIgnoreCase(""))
						 if (!names.equalsIgnoreCase(name))
						{

							Log.i("info", "displaying users::" + names);

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							map.put(TAG_ID, id);
							map.put(TAG_NAME, names);
							map.put(TAG_EMAIL, email);

							// adding HashList to ArrayList
							usersList.add(map);
						} else {
							Log.i("info", "Not showing user ::" + names);

						}
					}
				} else {
					Log.d("Users: ", "null");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all albums
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(PlayScreen.this,
							usersList, R.layout.list_item_albums, new String[] {
									TAG_ID, TAG_NAME, TAG_EMAIL }, new int[] {
									R.id.user_id, R.id.user_name,
									R.id.user_email });

					// updating listview
					ListView myList = (ListView) findViewById(android.R.id.list);

					Log.i("info", "displaying users" + usersList);

					myList.setAdapter(adapter);

					// getListView().setListAdapter(adapter);
				}
			});

		}

	}

	// Adding Override for the Hardware Back key
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
