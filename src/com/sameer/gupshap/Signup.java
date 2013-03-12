/**
 * 
 */
package com.sameer.gupshap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author sameer
 *
 */
public class Signup extends Activity {

	/**
	 * @param args
	 */
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }
	
	public void onSignUp(View view)
	{
		EditText et1,et2;
		String user,pass;
		int status;
		
		et1= (EditText)findViewById(R.id.editText1);
		et2= (EditText)findViewById(R.id.editText2);
		
		//getting data from form 
		user=et1.getText().toString();
		pass=et2.getText().toString();
		
		status=createUser(user, pass);
		if (status == 0)
		{
			Log.i("error", "Unable to create user");
			Toast.makeText(getApplicationContext(),"Unable to create user", Toast.LENGTH_LONG).show();
		}
		else if(status == 1)
		{
			Log.i("info", "user created successfully");
			Toast.makeText(getApplicationContext(),"user created successfully", Toast.LENGTH_LONG).show();
		}
		else 
		{
			Log.i("info", "user already exists");
			Toast.makeText(getApplicationContext(),"user already exists", Toast.LENGTH_LONG).show();
		}
		
		
		
		
	}
	
	public int createUser(String name,String pass)
	{
		
		return 0;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
