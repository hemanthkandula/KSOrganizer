package edu.sastra.ks.ksorganizer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
//
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;

import android.content.SearchRecentSuggestionsProvider;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.sastra.ks.ksorganizer.Databases.SqliteDB;

public class ClusterEvents extends AppCompatActivity {
    private List<Movie> movieList = new ArrayList<>();
    SqliteDB sql = new SqliteDB(this);

    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    MenuItem item1;
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;
    String Clusternumber = null;
    ArrayList<HashMap<String, String>> EventsList = new ArrayList<>() ;
    ArrayList<HashMap<String, String>> CollegeList = new ArrayList<>() ;

    SharedPreferences prefs;
    public static final String UserPref = "Userpref";
    public static final String Regikey = "RegiKey";
    public static final String Passkey = "PassKey";
    public static final String Clusterkey = "Clusterkey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster_events);
        Clusternumber = getIntent().getStringExtra("Cluster");
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        //Clusternumber = "1";
        EventsList =  sql.getAllEvents();
        CollegeList = sql.getallclgs();

        if (EventsList.size() == 0) {
            prgDialog = new ProgressDialog(this);

//            VideoView videoView = (VideoView)findViewById(R.id.videoView);
//            Uri uri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.giphy);
//            videoView.setVideoURI(uri);
//            videoView.start();


//            WebView webView = (WebView) prgDialog.findViewById(R.id.imageWebView);
//            // 'filePath' is the path of your .GIF file on SD card.
//            webView.loadUrl("file:///android_asset/giphy.gif");

           //ad prgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            prgDialog.setCancelable(false);
            syncSQLiteMySQLDB(Clusternumber);
        }
        if (CollegeList.size() == 0) {
            prgDialog = new ProgressDialog(this);

            prgDialog.setCancelable(false);
            GETCOLLS();
        }

//        MobileAds.initialize(getApplicationContext(), getString(R.string.google_ad_id));
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        System.out.println(MonoalphabeticCipher.doEncryption("KS001245$%SIVA SUBRAMANIAN$%SASTRA UNIVERSITY$%M$%8438181024$%YES$%SIVASUBRAMANIAN96@GMAIL.COM"));
        System.out.println(MonoalphabeticCipher.doEncryption("KS011111$%hem$%M$%7639810640$%YES$%122@GMAIL.COM"));


        EventsList =  sql.getAllEvents();
        System.out.println(EventsList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(movieList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();



                if (movie.getGenre()=="Individual Event") {
                    String Eventno = sql.getEventNO(movie.getTitle());
                    Intent Cl = new Intent(ClusterEvents.this, AddParticipant.class);
                    Cl.putExtra("EventNo",Eventno);
                    Cl.putExtra("EventName",movie.getTitle());
                    System.out.println("NNNAAMMMEEE:"+movie.getGenre());
                    Log.d("Eventno", Eventno);
                    startActivity(Cl);
                }
                else  {
                    String Eventno = sql.getEventNO(movie.getTitle());
                    Intent Cl = new Intent(ClusterEvents.this, GroupActivity.class);
                    Cl.putExtra("EventNo",Eventno);
                    Cl.putExtra("EventName",movie.getTitle());
                    System.out.println("NNNAAMMMEEE:"+movie.getGenre());
                    Log.d("Eventno", Eventno);
                    startActivity(Cl);
                }

                }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        item1 = menu.findItem(R.id.action_refresh);
        if(sql.dbSyncCount()!=0)
        {
            item1.setIcon(R.drawable.ic_action_update);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if(sql.dbSyncCount()!=0)
                {
                    item1.setIcon(R.drawable.ic_action_update);
                }
                //getothers();*/
                // refresh
                return true;

            case R.id.feedback:
                Intent Cl1 = new Intent(Intent.ACTION_VIEW,Uri.parse(""));
                startActivity(Cl1);
                return true;

            case R.id.action_check_updates:
                // check for updates action
                return true;

            case R.id.action_log_out:
                prefs = getApplicationContext().getSharedPreferences(UserPref, 0);
                final SharedPreferences.Editor editor = prefs.edit();
                editor.remove(Clusterkey);
                editor.commit();
                Intent Cl = new Intent(ClusterEvents.this, MainActivity.class);
                startActivity(Cl);
                finish();
                return true;

            case R.id.action_exit:
                onBackPressed();
                // check for updates action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Launching new activity
     * */





    private void recyview(){

        EventsList =  sql.getAllEvents();
       // System.out.println(EventsList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MoviesAdapter(movieList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();



                if (movie.getGenre()=="Individual Event") {
                    String Eventno = sql.getEventNO(movie.getTitle());
                    Intent Cl = new Intent(ClusterEvents.this, AddParticipant.class);
                    Cl.putExtra("EventName",movie.getTitle());
                    Cl.putExtra("EventNo",Eventno);
                    System.out.println(movie.getGenre());
                    Log.d("Eventno", Eventno);
                    startActivity(Cl);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();
    }

    private void prepareMovieData() {
        Movie movie;
    for (int i = 0; i< EventsList.size();i++) {
        System.out.println(EventsList);
        System.out.println(EventsList.get(i).get("Gflag"));
        String Group;
        // result = Integer.valueOf((EventsList.get(i).get("Gflag ")));
        Integer result =Integer.parseInt(EventsList.get(i).get("Gflag"));
        Log.d("reult:",String.valueOf(result));

               if (result== 1 ){ Group = "Group Event";}else { Group= "Individual Event";}
         movie = new Movie(EventsList.get(i).get("EVENTNAME"), Group,"","");
        System.out.println(movie);
        movieList.add(movie);
    }
        mAdapter.notifyDataSetChanged();
    }



    public void syncSQLiteMySQLDB(String Clusternumber) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        prgDialog.show();
        params.put("Cluster",Clusternumber);
        // Log.d("event",Clustername);
        client.post("http://300dpi.xyz/app/Events.php" , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                prgDialog.hide();
                //System.out.println(response);

                updateSQLite(response);
               // System.out.println(response);
                recyview();
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void GETCOLLS() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        prgDialog.show();

        client.post("http://300dpi.xyz/app/allcolleges.php" , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                prgDialog.hide();
                //System.out.println(response);

                addcolls(response);

            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


 //   @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(ClusterEvents.this,R.style.AppCompatAlertDialogStyle);
//        alert.setTitle("Want to Close the App??");
//
//
//        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //What ever you want to do with the value
//
//                finish();
//                System.exit(0);
//                //OR
//            }
//        });
//
//        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // what ever you want to do with No option.
//            }
//        });
//
//        alert.show();
//
//    }
 @Override
 public void onBackPressed() {
     new AlertDialog.Builder(this)
             .setIcon(android.R.drawable.ic_dialog_alert)
             .setTitle("Closing..")
             .setMessage("Are you sure you want to Exit ?")
             .setPositiveButton("Yes", new DialogInterface.OnClickListener()
             {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {

                     finish();
                     moveTaskToBack(true);
                     android.os.Process.killProcess(android.os.Process.myPid());
                     System.exit(1);
                 }

             })
             .setNegativeButton("No", null)
             .show();
 }

    public void updateSQLite(String response){

        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if(arr.length() != 0){
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    //System.out.println(obj.get("EVENTNO"));
                   // System.out.println(obj.get("EVENTNAME"));
                    queryValues = new HashMap<String, String>();
                    queryValues.put("EVENTNO", obj.get("EVENTNO").toString());
                    queryValues.put("EVENTNAME", obj.get("EVENTNAME").toString());
                    //queryValues.put("EventNo", obj.get("EventId").toString());
                    queryValues.put("Gflag", obj.get("Gflag").toString());


                    sql.InsertEvents(queryValues);

                }
//                updateMySQLSyncSts(gson.toJson(Eventsynclist));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




    public void addcolls(String response){

        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if(arr.length() != 0){
                for (int i = 0; i < arr.length(); i++) {

                    sql.Insertclgs( arr.get(i).toString());

                }

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}


