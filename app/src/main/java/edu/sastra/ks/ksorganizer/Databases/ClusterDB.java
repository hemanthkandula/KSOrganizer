package edu.sastra.ks.ksorganizer.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class ClusterDB extends SQLiteOpenHelper {

	public ClusterDB(Context applicationcontext) {


        super(applicationcontext, "Clustersqlite.db", null, 1);


    }

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE ClusterEvents ( EventId INTEGER PRIMARY KEY, EventName TEXT, udpateStatus TEXT,EventNo TEXT,Gflag Text)";
        database.execSQL(query);
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS ClusterEvents";
		database.execSQL(query);
        onCreate(database);
	}

	public void insertUser(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("EventName", queryValues.get("EventName"));
		values.put("udpateStatus", "no");
		values.put("EventNo",queryValues.get("EventNo"));
		values.put("Gflag",queryValues.get("Gflag"));

		database.insert("ClusterEvents", null, values);
		database.close();
	}

	public void dropDB()
	{
		// db.delete(String tableName, String whereClause, String[] whereArgs);
		// If whereClause is null, it will delete all rows.
		//DELETE FROM COMPANY WHERE ID = 7;
		SQLiteDatabase database = this.getWritableDatabase();
		System.out.println("i droped it");
//        String query;
//        query = "DELETE  FROM studs";
//        database.execSQL(query);
//        onCreate(database);

		database.delete("ClusterEvents",null,null);
		database.close();
	}

	public ArrayList<HashMap<String, String>> getAllEvents() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM ClusterEvents";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("EventId", cursor.getString(0));
	        	map.put("EventName", cursor.getString(1));
				//map.put(("EventNo"),cursor.getString(3));
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	    database.close();
	    return wordList;
	}
	public String getEventNO(String EventId){
		String selectQuery = "SELECT  EVENTNO FROM ClusterEvents where EVENTNAME = '"+EventId+"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		String EventNo =new String();
		if (cursor.moveToFirst()) {
		 EventNo = cursor.getString(0);
		}
return EventNo;
	}

    public String AmGroup(String EventId){
        String selectQuery = "SELECT  Gflag FROM ClusterEvents where EventId = '"+EventId+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String EventNo =new String();
        if (cursor.moveToFirst()) {
            EventNo = cursor.getString(0);
        }
        return EventNo;
    }

	public String composeJSONfromSQLite(){
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM users where udpateStatus = '"+"no"+"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("userId", cursor.getString(0));
	        	map.put("userName", cursor.getString(1));
	        	wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	    database.close();
		Gson gson = new GsonBuilder().create();

		return gson.toJson(wordList);
	}

	public String getSyncStatus(){
	    String msg = null;
	    if(this.dbSyncCount() == 0){
	    	msg = "SQLite and Remote MySQL DBs are in Sync!";
	    }else{
	    	msg = "DB Sync needed\n";
	    }
	    return msg;
	}


	public int dbSyncCount(){
		int count = 0;
		String selectQuery = "SELECT  * FROM users where udpateStatus = '"+"no"+"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    count = cursor.getCount();
	    database.close();
		return count;
	}

//
//	public void updateSyncStatus(String id, String status){
//		SQLiteDatabase database = this.getWritableDatabase();
//		String updateQuery = "Update users set udpateStatus = '"+ status +"' where userId="+"'"+ id +"'";
//		Log.d("query",updateQuery);
//		database.execSQL(updateQuery);
//		database.close();
//	}


    public void dropDB(SQLiteDatabase database)
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        //DELETE FROM COMPANY WHERE ID = 7;


        String query;
        query = "DROP TABLE IF EXISTS ClusterEvents";
        database.execSQL(query);
        onCreate(database);
    }
    public void getEventId(){
        String selectQuery = "SELECT  EventNo FROM ClusterEvents";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);



    }




}
