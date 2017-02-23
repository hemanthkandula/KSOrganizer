package edu.sastra.ks.ksorganizer.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class ResulltsDB extends SQLiteOpenHelper {

	public ResulltsDB(Context applicationcontext) {


        super(applicationcontext, "Results.db", null, 1);


    }

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE studs ( userId INTEGER PRIMARY KEY, userName TEXT,udpateStatus TEXT,EventNo TEXT NOT NULL DEFAULT '0',GroupName TEXT NOT NULL DEFAULT 'G',Position TEXT NOT NULL DEFAULT 'P',Rank INT DEFAULT 0,RegNo INT)";
        database.execSQL(query);
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS studs";
		database.execSQL(query);
        onCreate(database);
	}

	public void insertUser(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("userName", queryValues.get("userName"));
		values.put("udpateStatus", "no");
		values.put("EventNo",queryValues.get("EventNo"));
		values.put("GroupName",queryValues.get("GroupName"));
		values.put("Position",queryValues.get("Position"));
		values.put("Rank",queryValues.get("Rank"));
        values.put("RegNo",queryValues.get("RegiNo"));
		//values.put("EventNo",queryValues.get("EventNo"));





		database.insert("studs", null, values);

		database.close();
	}
	

	public ArrayList<HashMap<String, String>> getAllstuds(String EventNo) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM studs WHERE EventNo="+"'"+ EventNo +"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	if (cursor.getString(6)== "0")
				{map.put("Rank", "");}
				else {map.put("Rank", cursor.getString(6));}
	        	map.put("userName", cursor.getString(1));
				map.put("RegiNo", cursor.getString(7));



				//map.put(("EventNo"),cursor.getString(3));
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	    database.close();
	    return wordList;
	}

    public ArrayList<HashMap<String, String>> getAllGroupstuds(String EventNo) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT DISTINCT GroupName,Rank FROM studs WHERE EventNo="+"'"+ EventNo +"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Rank", cursor.getString(1));
				map.put("GroupName", cursor.getString(0));
				//map.put("RegiNo", cursor.getString(7));



				//map.put(("EventNo"),cursor.getString(3));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		return wordList;
	}

	public ArrayList<HashMap<String, String>> getallGroupUsers(String GroupName) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT DISTINCT * FROM studs WHERE GroupName="+"'"+ GroupName +"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Position", cursor.getString(5));
				map.put("RegiNo", cursor.getString(1));
				//map.put("RegiNo", cursor.getString(7));



				//map.put(("EventNo"),cursor.getString(3));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		return wordList;
	}

	public String getEventNO(String EventId){
		String selectQuery = "SELECT  EventNo FROM studs where EventId = '"+EventId+"'";
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
        String selectQuery = "SELECT  * FROM studs where udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("userId", cursor.getString(0));
                //map.put("userName", cursor.getString(1));
                map.put("EventNo", cursor.getString(3));
                map.put("GroupName", cursor.getString(4));
                map.put("Position",cursor.getString(5));
                map.put("Rank", cursor.getString(6));
                map.put("RegNo",cursor.getString(7));
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
	    	msg = " in Sync!";
	    }else{
	    	msg = "Sync needed\n";
	    }
	    return msg;
	}


	public int dbSyncCount(){
		int count = 0;
		String selectQuery = "SELECT  * FROM studs where udpateStatus = '"+"no"+"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    count = cursor.getCount();
	    database.close();
		return count;
	}


	public void updateSyncStatus(String id, String status){
		SQLiteDatabase database = this.getWritableDatabase();
		String updateQuery = "Update studs set udpateStatus = '"+ status +"' where userId="+"'"+ id +"'";
		Log.d("query",updateQuery);
		database.execSQL(updateQuery);
		database.close();
	}
	public void updateRank(HashMap<String, String> queryValues){
		SQLiteDatabase database = this.getWritableDatabase();

		String updateQuery = "Update studs set Rank = '"+ queryValues.get("Rank") +"' where RegNo="+"'"+ queryValues.get("RegiNo") +"' and EventNo = "+" '"+ queryValues.get("EventNo")+"'    ";
		Log.d("query",updateQuery);
		database.execSQL(updateQuery);
		database.close();
	}
	public void updateposition(HashMap<String, String> queryValues){
		SQLiteDatabase database = this.getWritableDatabase();

		String updateQuery = "Update studs set Position = '"+ queryValues.get("Position") +"' where userName="+"'"+ queryValues.get("userName") +"' and GroupName = "+" '"+ queryValues.get("GroupName")+"'    ";
		Log.d("query",updateQuery);
		database.execSQL(updateQuery);
		database.close();
	}
	public void updateGroupRank(HashMap<String, String> queryValues){
		SQLiteDatabase database = this.getWritableDatabase();

		String updateQuery = "Update studs set Rank = '"+ queryValues.get("Rank") +"' where GroupName="+"'"+ queryValues.get("GroupName") +"' and EventNo = "+" '"+ queryValues.get("EventNo")+"'    ";
		Log.d("query",updateQuery);
		database.execSQL(updateQuery);
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

		database.delete("studs",null,null);
		database.close();
    }
    public void getEventId(){
        String selectQuery = "SELECT  EventNo FROM studs";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);



    }

}
