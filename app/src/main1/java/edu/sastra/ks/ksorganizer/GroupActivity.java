package edu.sastra.ks.ksorganizer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.sastra.ks.ksorganizer.Databases.SqliteDB;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

import static edu.sastra.ks.ksorganizer.AddParticipant.Regikey;
import static edu.sastra.ks.ksorganizer.AddParticipant.UserPref;

public class GroupActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter mAdapter;
    ProgressDialog prgDialog;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String EventNo,EventName;
    MenuItem item1;
    private RadioGroup radioGroup;
    SharedPreferences prefs;
    private RadioButton radioButton;

    HashMap<String, String> queryValues;

    private Button btnDisplay,btnCancel;
    SqliteDB sql = new SqliteDB(this);
    ArrayList<HashMap<String, String>> GroupsList = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getApplicationContext().getSharedPreferences(UserPref,0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(" Please wait...");
        prgDialog.setCancelable(false);

        EventNo= getIntent().getStringExtra("EventNo");
        EventName = getIntent().getStringExtra("EventName");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(EventName);
        // ParticipantsList  =  sql.getAllUsers(EventNo);
        GroupsList= sql.getAllGroups(EventNo);
        System.out.println(GroupsList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mAdapter = new MyRecyclerViewAdapter(movieList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));

        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);

        recyclerView.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
        // recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Movie movie = movieList.get(position);
                Movie movie = mAdapter.getpos(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();


                    Intent Cl = new Intent(GroupActivity.this, GroupMembers.class);
                    Cl.putExtra("EventNo",EventNo);
                    Cl.putExtra("GroupName", movie.getTitle());
                    Cl.putExtra("EventName",EventName);
                    System.out.println("NNNAAMMMEEE:"+movie.getGenre());
                    Log.d("Eventno", EventNo);
                    startActivity(Cl);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareMovieData();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                syncSQLiteMySQLDB();
                getallusers();
                mAdapter.notifyDataSetChanged();

                if(sql.dbSyncCount()!=0)
                {
                    item1.setIcon(R.drawable.ic_action_update);
                }
                Intent Cl = new Intent(getApplicationContext(), GroupActivity.class);
                Cl.putExtra("EventNo",EventNo);
                Cl.putExtra("EventName",EventName);
                startActivity(Cl);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void prepareMovieData() {


        Movie movie;
        int i;
        for (i = 0; i< GroupsList.size();i++) {
            movie = new Movie(GroupsList.get(i).get("GROUPNAME"),sql.getGroupCollege(EventNo,GroupsList.get(i).get("GROUPNAME")),GroupsList.get(i).get("GROUPRANK"),GroupsList.get(i).get("GROUPTITLE"));
            movieList.add(movie);
        }
        mAdapter.notifyItemChanged(i);      //  mAdapter.notifyDataSetChanged();





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent objIntent = new Intent(getApplicationContext(),
                ClusterEvents.class);
        startActivity(objIntent);
        finish();
    }
    public void onFab(View view)
    {
            // FAB Action goes here
            AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this,R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Enter Group Name");
            LinearLayout layout = new LinearLayout(GroupActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            // Set up the input
            final EditText input = new EditText(GroupActivity.this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setHint("Enter team name");
        final InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                view.getWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String   m_Text = input.getText().toString();
                    //String   m_Text2 = input2.getText().toString();

                    if(m_Text != null ){




                        Intent objIntent = new Intent(GroupActivity.this,GroupScanner.class);
//
                        objIntent.putExtra("EventNo", EventNo);
                        objIntent.putExtra("GroupName", m_Text);


                        startActivity(objIntent);
                        finish();}

                    else
                        Toast.makeText(getApplicationContext(), "Enter Valid Team Name", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
            layout.addView(input);


            //final EditText input2 = new EditText(getApplicationContext());
            //input2.setHint("Enter Register Number of Team Head");
            //layout.addView(input2);



            builder.setView(layout);
            builder.setCancelable(true);



            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                 String   m_Text = input.getText().toString();
                 //String   m_Text2 = input2.getText().toString();

                    if(m_Text != null ){




                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                        Intent objIntent = new Intent(GroupActivity.this,GroupScanner.class);
//
                        objIntent.putExtra("EventNo", EventNo);
                        objIntent.putExtra("GroupName", m_Text);


                        startActivity(objIntent);
                        finish();}

                    else
                        Toast.makeText(getApplicationContext(), "Enter Valid Team Name", Toast.LENGTH_SHORT).show();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }




    public void syncSQLiteMySQLDB() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        ArrayList<HashMap<String, String>> userList =  sql.getAllUsers(EventNo);
        if(userList.size()!=0){
            if(sql.dbSyncCount() != 0){
                prgDialog.show();
                params.put("usersJSON", sql.composeJSONfromSQLite( EventNo));
                System.out.println(sql.composeJSONfromSQLite(EventNo));
                client.post("http://kuruksastra.ml/app/particpantsadd.php",params ,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        prgDialog.hide();
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length());
                            for(int i=0; i<arr.length();i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                System.out.println(obj.get("KSID"));
                                System.out.println(obj.get("status"));
                                sql.updateSyncStatus(obj.get("KSID").toString(),obj.get("status").toString(),EventNo);
                            }
                            Toast.makeText(getApplicationContext(), "Sync completed!", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // TODO Auto-generated method stub
                        prgDialog.hide();
                        if(statusCode == 404){
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }else if(statusCode == 500){
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Device might not be connected to Internet...Connect and retry", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), " in Sync!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "No data  please do enter User name  action", Toast.LENGTH_LONG).show();
        }
    }


    public void getallusers() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        prgDialog.show();
        params.put("EventNo",EventNo);
        // Log.d("event",Clustername);
        client.post("http://kuruksastra.ml/app/allusers.php" , params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                prgDialog.hide();
                System.out.println(response);

                updateSQLite(response);
                //System.out.println(response);
                //recyview();
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
    public void updateSQLite(String response){

        try {
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            if(arr.length() != 0){
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = (JSONObject) arr.get(i);
                    //System.out.println(obj.get("EVENTNO"));
                    // System.out.println(obj.get("EVENTNAME"));

                    List<String> ksids = sql.getksid(EventNo);


                    List<String> ksidnulls = sql.getksidnull(EventNo);

//System.out.println(ksids);
                    System.out.print("ksidsnulls");
                    System.out.println(ksidnulls);



                    queryValues = new HashMap<String, String>();
                    queryValues.put("KSID",obj.get("KSID").toString() );
                    queryValues.put("NAME", obj.get("NAME").toString());
                    queryValues.put("GEN", obj.get("GEN").toString());
                    queryValues.put("CLG", obj.get("CLG").toString());
                    queryValues.put("MOB", obj.get("MOB").toString());
                    queryValues.put("EMAIL", obj.get("EMAIL").toString());
                    queryValues.put("ACCOM", obj.get("ACCOM").toString());
                    queryValues.put("EVENTNO",EventNo);
                    queryValues.put("GROUPNAME", obj.get("GROUPNAME").toString());

                    queryValues.put("ORGANIZER",  obj.get("ORGANISER").toString());
//
//                   // queryValues.put("EVENTNO", );
//                    queryValues.put("EVENTNAME", obj.get("EVENTNAME").toString());
//                    queryValues.put("EventNo", obj.get("EventId").toString());
//                    queryValues.put("Gflag", obj.get("Gflag").toString());
                    if (!ksids.contains(obj.get("KSID").toString())){

                        sql.insertUser(queryValues);}

                    if (ksidnulls.contains(obj.get("KSID").toString())){

                        sql.updateUser(queryValues);
                    }






                }
//                updateMySQLSyncSts(gson.toJson(Eventsynclist));

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_participant_actions, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        item1 = menu.findItem(R.id.action_refresh);
        if(sql.dbSyncCount()!=0)
        {
            item1.setIcon(R.drawable.ic_action_update);
        }
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        mAdapter.setFilter(movieList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_refresh:
                syncSQLiteMySQLDB();
                getallusers();
                mAdapter.notifyDataSetChanged();

                if(sql.dbSyncCount()!=0)
                {
                    item1.setIcon(R.drawable.ic_action_update);
                }
                Intent Cl = new Intent(getApplicationContext(), GroupActivity.class);
                Cl.putExtra("EventNo",EventNo);
                Cl.putExtra("EventName",EventName);
                startActivity(Cl);
                //getothers();*/
                // refresh
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Movie> filteredModelList = filter(movieList, newText);

        mAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Movie> filter(List<Movie> models, String query) {
        query = query.toLowerCase();
        final List<Movie> filteredModelList = new ArrayList<>();
        for (Movie model : models) {
            final String text2 = model.getGenre().toLowerCase();
           if(model.getTitle()!=null){ final String text1 = model.getTitle().toLowerCase();
            if (text1.contains(query) || text2.contains(query)) {
                filteredModelList.add(model);
            }
        }else {if (text2.contains(query) ) {

               filteredModelList.add(model);
           }}
        }
        return filteredModelList;
    }
}
