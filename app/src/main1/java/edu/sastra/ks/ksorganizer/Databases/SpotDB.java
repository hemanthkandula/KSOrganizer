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

public class SpotDB extends SQLiteOpenHelper {

    public SpotDB(Context applicationcontext) {
        super(applicationcontext, "Spot.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE spot ( userId INTEGER PRIMARY KEY, userName TEXT,udpateStatus TEXT,EmailId TEXT NOT NULL DEFAULT '0',Phone INT NOT NULL DEFAULT 0,Regino TEXT DEFAULT '0')";
        database.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS spot";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertUser(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userName", queryValues.get("userName"));
        values.put("EmailId",queryValues.get("EmailId"));
        values.put("Phone",queryValues.get("Phone"));
        values.put("Regino",queryValues.get("Regino"));


        values.put("udpateStatus", "no");

        database.insert("spot", null, values);
        database.close();
    }




    public String composeJSONfromSQLite(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM spot where udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));

                map.put("Regino", cursor.getString(5));
                map.put("userName", cursor.getString(1));
                map.put("EmailId", cursor.getString(3));
                map.put("Phone", cursor.getString(4));

                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        Gson gson = new GsonBuilder().create();

        return gson.toJson(wordList);
    }
    public ArrayList<HashMap<String, String>> getAllGroups() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM spot";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));
                map.put("Regino", cursor.getString(4));
                map.put("userName", cursor.getString(1));
                map.put("EmailId", cursor.getString(3));
                map.put("Phone", cursor.getString(4));

                wordList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return wordList;
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
        String selectQuery = "SELECT  * FROM spot where udpateStatus = '"+"no"+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        count = cursor.getCount();
        database.close();
        return count;
    }


    public void updateSyncStatus(String id, String status){
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "Update spot set udpateStatus = '"+ status +"' where userId="+"'"+ id +"'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }





}
