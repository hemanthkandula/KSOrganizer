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
import java.util.List;

public class WinnnerDB extends SQLiteOpenHelper {

	public WinnnerDB(Context applicationcontext) {


        super(applicationcontext, "winnners.db", null, 1);


    }

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE winners ( userId INTEGER PRIMARY KEY, userName TEXT,udpateStatus TEXT,EventNo TEXT NOT NULL DEFAULT '0',GroupName TEXT NOT NULL DEFAULT 'G',Position TEXT NOT NULL DEFAULT 'P',Rank INT DEFAULT 0,RegNo INT,Gflag Text,EventName TEXT,ClusterName TEXT)";
        database.execSQL(query);
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS winners";
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

		values.put("EventName", queryValues.get("EventName"));
		values.put("ClusterName", queryValues.get("ClusterName"));
		//values.put("udpateStatus", "no");
		//values.put("EventNo",queryValues.get("EventNo"));
		values.put("Gflag",queryValues.get("Gflag"));




		database.insert("winners", null, values);

		database.close();
	}
	

	public ArrayList<HashMap<String, String>> getAllwinners() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM winners WHERE 1";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("Rank", cursor.getString(6));
	        	map.put("userName", cursor.getString(1));
				map.put("RegiNo", cursor.getString(7));



				//map.put(("EventNo"),cursor.getString(3));
                wordList.add(map);
	        } while (cursor.moveToNext());
	    }
	    database.close();
	    return wordList;
	}
	public ArrayList<HashMap<String, String>> getAllGroupwinners(String EventName) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();


        String selectQuery = "SELECT  userName,Position,GroupName FROM winners WHERE Position != 'P' AND EventName="+"'"+ EventName +"'  ";
        System.out.println("pos" + selectQuery);
        SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
        System.out.println(cursor);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
                if (cursor.getString(0)!= "" &&cursor.getString(0)!= null )
                {map.put("Position", cursor.getString(1));
				map.put("userName", cursor.getString(0));
                map.put("GroupName",cursor.getString(2));
				//map.put("RegiNo", cursor.getString(7));



				//map.put(("EventNo"),cursor.getString(3));}
				wordList.add(map);}
			} while (cursor.moveToNext());
		}
		database.close();




		return wordList;
	}
	public List<String> getClusters() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT DISTINCT ClusterName FROM winners WHERE 1  ";
		System.out.println(selectQuery);

		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				//map.put("Rank", cursor.getString(6));
				map.put("ClusterName", cursor.getString(0));
				//map.put("RegiNo", cursor.getString(7));



				//map.put(("EventNo"),cursor.getString(3));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		List<String> clusters =new ArrayList<String>();
		for(int i=0;i<wordList.size();i++){
			clusters.add(wordList.get(i).get("ClusterName"));
		}
		System.out.println(clusters);

		return clusters;
	}

	public List<String> getEvents(String ClusterName) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT DISTINCT EventName FROM winners where ClusterName = '"+ClusterName+"'";
System.out.println(selectQuery);

		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				//map.put("Rank", cursor.getString(6));
				map.put("EventName", cursor.getString(0));
				//map.put("RegiNo", cursor.getString(7));



				//map.put(("EventNo"),cursor.getString(3));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		List<String> events =new ArrayList<String>();
		if(wordList.size()!=0){
		for(int i=0;i<wordList.size();i++){
			events.add(wordList.get(i).get("EventName"));
		}
		System.out.println(events);}
		return events;
	}


	public ArrayList<HashMap<String, String>> getsinglewinners(String EventName) {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
if(gflag(EventName).equals("0")) {

	String selectQuery = "SELECT  userName,Rank,GroupName,RegNo FROM winners WHERE Rank != 0 AND EventName=" + "'" + EventName + "'  ";
	System.out.println("SIn" + selectQuery);

	SQLiteDatabase database = this.getWritableDatabase();
	Cursor cursor = database.rawQuery(selectQuery, null);
	System.out.println(cursor + "cus");
	if (cursor.moveToFirst()) {
		do {
			HashMap<String, String> map = new HashMap<String, String>();
			//map.put("Rank", cursor.getString(6));

			if (cursor.getString(0) != "" && cursor.getString(0) != null) {
				map.put("userName", cursor.getString(0));
				map.put("Rank", cursor.getString(1));
//map.put("RegiNo",cursor.getString(3));
				// map.put("GroupName",cursor.getString(4));


				//map.put(("EventNo"),cursor.getString(3));
				wordList.add(map);
			}
		} while (cursor.moveToNext());
	}
	database.close();
	System.out.println("wordlist" + wordList);
	Log.d("wod ", "");
}



else if(gflag(EventName).equals("1")) {
	String selectQuery = "SELECT  DISTINCT Rank,GroupName FROM winners WHERE Rank !='0' AND EventName=" + "'" + EventName + "'  ";
	System.out.println("SIn" + selectQuery);

	SQLiteDatabase database = this.getWritableDatabase();
	Cursor cursor = database.rawQuery(selectQuery, null);
	System.out.println(cursor + "cus");
	if (cursor.moveToFirst()) {
		do {
			HashMap<String, String> map = new HashMap<String, String>();
			//map.put("Rank", cursor.getString(6));

			if (cursor.getString(1) != "" && cursor.getString(0) != null) {
				map.put("userName", cursor.getString(1));
				System.out.println("Icame in");
				map.put("Rank", cursor.getString(0));
				//map.put("RegiNo",cursor.getString(3));
				// map.put("GroupName",cursor.getString(4));


				//map.put(("EventNo"),cursor.getString(3));
				wordList.add(map);
			}
		} while (cursor.moveToNext());
	}
	database.close();
	System.out.println("wordlist" + wordList);
	Log.d("wod ", "");

}

		return wordList; }

	public String getEventNO(String EventId){
		String selectQuery = "SELECT  EventNo FROM winners where EventId = '"+EventId+"'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		String EventNo =new String();
		if (cursor.moveToFirst()) {
		 EventNo = cursor.getString(0);
		}
return EventNo;
	}
	public String gflag(String EventName){
		String selectQuery = "SELECT  Gflag FROM winners where EventName = '"+EventName+"'";
        System.out.println("GF" + selectQuery);

        SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		String Gflag =new String();
		if (cursor.moveToFirst()) {
			Gflag = cursor.getString(0);
		}
        Log.d("Gflag",Gflag);
		return Gflag;
	}

    public String composeJSONfromSQLite(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM winners where udpateStatus = '"+"no"+"'";
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
		String selectQuery = "SELECT  * FROM winners where udpateStatus = '"+"no"+"'";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    count = cursor.getCount();
	    database.close();
		return count;
	}


	public void updateSyncStatus(String id, String status){
		SQLiteDatabase database = this.getWritableDatabase();
		String updateQuery = "Update winners set udpateStatus = '"+ status +"' where userId="+"'"+ id +"'";
		Log.d("query",updateQuery);
		database.execSQL(updateQuery);
		database.close();
	}
	public void updateRank(HashMap<String, String> queryValues){
		SQLiteDatabase database = this.getWritableDatabase();

		String updateQuery = "Update winners set Rank = '"+ queryValues.get("Rank") +"' where RegNo="+"'"+ queryValues.get("RegiNo") +"' and EventNo = "+" '"+ queryValues.get("EventNo")+"'    ";
		Log.d("query",updateQuery);
		database.execSQL(updateQuery);
		database.close();
	}
	public void updateGroupRank(HashMap<String, String> queryValues){
		SQLiteDatabase database = this.getWritableDatabase();

		String updateQuery = "Update winners set Rank = '"+ queryValues.get("Rank") +"' where GroupName="+"'"+ queryValues.get("GroupName") +"' and EventNo = "+" '"+ queryValues.get("EventNo")+"'    ";
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

		database.delete("winners",null,null);
		database.close();
    }
    public void getEventId(){
        String selectQuery = "SELECT  EventNo FROM winners";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);



    }




}
