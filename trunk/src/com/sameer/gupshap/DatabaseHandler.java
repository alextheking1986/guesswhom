/**
 * 
 */
package com.sameer.gupshap;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Syed Sameer
 * 
 */
public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "Gupshap";

	// Users table name
	private static final String TABLE_CHAT = "chat";

	// Users Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_CHAT = "chat";
	private static final String KEY_MSG_TYPE = "msg_type";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i("info", "inside constructor of DatabaseHandler");

	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_UserS_TABLE = "CREATE TABLE " + TABLE_CHAT + "(" + KEY_ID
				+ " TEXT ," + KEY_NAME + " TEXT," + KEY_CHAT + " TEXT,"
				+ KEY_MSG_TYPE + " TEXT)";
		Log.i("info", "creating table");
		db.execSQL(CREATE_UserS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new User
	void addChat(User User) {
		SQLiteDatabase db = this.getWritableDatabase();

		Log.i("info",
				User.getId() + ":" + User.getName() + ":" + User.getChat());
		ContentValues values = new ContentValues();
		values.put(KEY_ID, User.getId());
		values.put(KEY_NAME, User.getName()); // User Name
		values.put(KEY_CHAT, User.getChat()); // User Chat
		values.put(KEY_MSG_TYPE, User.getMsg_type()); // User Msg type

		// Inserting Row
		db.insert(TABLE_CHAT, null, values);

		db.close(); // Closing database connection
	}

	// Getting single User
	User getUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CHAT, new String[] { KEY_ID, KEY_NAME,
				KEY_CHAT }, KEY_ID + "=?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		User User = new User((cursor.getString(0)), cursor.getString(1),
				cursor.getString(2), null);
		// return User
		return User;
	}

	// Getting All Users
	public List<User> getAllUsers() {
		List<User> UserList = new ArrayList<User>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CHAT;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				User User = new User();
				User.setId((cursor.getString(0)));
				User.setName(cursor.getString(1));
				User.setChat(cursor.getString(2));
				// Adding User to list
				UserList.add(User);
			} while (cursor.moveToNext());
		}

		// return User list
		return UserList;
	}

	// Updating single User
	public int updateUser(User User) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, User.getName());
		values.put(KEY_CHAT, User.getChat());

		// updating row
		return db.update(TABLE_CHAT, values, KEY_ID + " = ?",
				new String[] { String.valueOf(User.getId()) });
	}

	// Deleting single User
	public void deleteChat(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CHAT, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Getting Users Count
	public int getUsersCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CHAT;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public List<String> getChat(String id) {
		List<String> chat = new ArrayList<String>();
		try
		{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CHAT, new String[] { KEY_NAME,KEY_CHAT }, KEY_ID
				+ "=?", new String[] { String.valueOf(id) }, null, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();
			

			while (!cursor.isLast()) {
				Log.d("getting result",cursor.toString());
				chat.add(cursor.getString(0)+": "+cursor.getString(1));
				cursor.moveToNext();
			}
		}
		}catch(Exception e)
		{
			Log.i("error",e.toString());
		}
		// return User

		return chat;
	}

}
