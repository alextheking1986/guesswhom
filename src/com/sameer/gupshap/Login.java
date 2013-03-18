package com.sameer.gupshap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	/**
	 * @param args
	 */
	// All the properties for the class
	//public static final String server="http://192.168.2.3";
	public static final String PREFS_NAME = "GupFile";
	public static final String server="http://paradigmcraft.com/";
	VerifyUser verify;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}

	public void onSubmit(View view)
	{
		verify = new VerifyUser();

		ProgressDialog mDialog = new ProgressDialog(Login.this);
		mDialog.setMessage("Logging in...");
		mDialog.setCancelable(true);
		mDialog.show();
		EditText et1,et2;
		String user,pass;
		boolean status;

		et1= (EditText)findViewById(R.id.editText1);
		et2= (EditText)findViewById(R.id.editText2);

		//getting data from form 
		user=et1.getText().toString();
		pass=et2.getText().toString();

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

			// Commit the edits!
			editor.commit();

			//Toast.makeText(getApplicationContext(),"User ::"+name+" pass::"+pass, Toast.LENGTH_LONG).show();
			Intent nextScreen = new Intent(getApplicationContext(), PlayScreen.class);
			nextScreen.putExtra("name", user);
			nextScreen.putExtra("pass", pass);
			startActivity(nextScreen);

		}
		else 
		{
			Log.i("info", "Incorrect username / password");
			Toast.makeText(getApplicationContext(),"Incorrect username / password", Toast.LENGTH_LONG).show();
		}





	}


}
