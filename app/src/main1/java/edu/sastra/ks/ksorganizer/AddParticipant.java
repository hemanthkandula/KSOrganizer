package edu.sastra.ks.ksorganizer;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.sastra.ks.ksorganizer.ClickListener;
import edu.sastra.ks.ksorganizer.Databases.SqliteDB;
import edu.sastra.ks.ksorganizer.DividerItemDecoration;
import edu.sastra.ks.ksorganizer.Movie;
import edu.sastra.ks.ksorganizer.MyRecyclerViewAdapter;
import edu.sastra.ks.ksorganizer.R;
import edu.sastra.ks.ksorganizer.RecyclerTouchListener;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AddParticipant extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter mAdapter;
    String a = "";
    ProgressDialog prgDialog;
    private RadioButton First, Second, Third, Noplace;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String EventNo, EventName;
    HashMap<String, String> queryValues;
    String get, ksid, coll, sex, acc, mno, email;
    SharedPreferences prefs;
    static final Integer CALL = 0x1;


    public static final String UserPref = "Userpref";
    public static final String Regikey = "RegiKey";
    public static final String Passkey = "PassKey";
    public static final String Clusterkey = "Clusterkey";


    private RadioGroup radioGroup;
    EditText title;
    private RadioButton radioButton;
    private Button btnDisplay, btnCancel;
    MenuItem item1;
    ArrayList<HashMap<String, String>> ParticipantsList = new ArrayList<>();


    SqliteDB sql = new SqliteDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_participant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventNo = getIntent().getStringExtra("EventNo");
        EventName = getIntent().getStringExtra("EventName");
        getSupportActionBar().setTitle(EventName);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(" Please wait...");
        prgDialog.setCancelable(true);
        ParticipantsList = sql.getAllUsers(EventNo);
        System.out.println(EventNo);

        System.out.println(ParticipantsList);
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
                final Movie movie = mAdapter.getpos(position);

                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                TextView t1,name,id,gen,mob,mail1,colg;
                ImageButton call, msg, mail;

                Button place, cancel;
                final Dialog dialog = new Dialog(AddParticipant.this);
                dialog.setContentView(R.layout.profile_dialog);
                dialog.setCancelable(true);

                name = (TextView) dialog.findViewById(R.id.name);
                id = (TextView) dialog.findViewById(R.id.ksid);
                gen = (TextView) dialog.findViewById(R.id.gen);
                mob = (TextView) dialog.findViewById(R.id.mob);
                mail1 = (TextView) dialog.findViewById(R.id.email);
                colg = (TextView) dialog.findViewById(R.id.coll);


                call = (ImageButton) dialog.findViewById(R.id.call);
                msg = (ImageButton) dialog.findViewById(R.id.msg);
                mail = (ImageButton) dialog.findViewById(R.id.mail);
                place = (Button) dialog.findViewById(R.id.place);
                cancel = (Button) dialog.findViewById(R.id.cancel);

                ksid = movie.getGenre();

                coll = sql.getCollege(EventNo, ksid);
                sex = sql.getGender(EventNo, ksid);
                acc = sql.getAccom(EventNo, ksid);
                mno = sql.getMobile(EventNo, ksid);
                email = sql.getEmail(EventNo, ksid);
                System.out.println(email);
                if(sex.equals("m") || sex.equals("M"))
                    sex = "Male";
                else
                    sex = "Female";
                name.setText(movie.getTitle());
                gen.setText(sex);
                colg.setText(coll);
                mob.setText(mno);
                mail1.setText(email);
                id.setText(movie.getGenre());
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mno));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            askForPermission(Manifest.permission.CALL_PHONE,CALL);
                            return;
                        }
                        startActivity(intent);
                    }
                });

                mail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] TO = {email};
                        String[] CC = {""};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);

                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "TEAM KURUKSASTRA");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                        }
                    }
                });

                msg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", mno);
                        smsIntent.putExtra("sms_body","");
                        startActivity(smsIntent);
                    }
                });

                place.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog1 = new Dialog(AddParticipant.this);
                        dialog1.setContentView(R.layout.group_result_dialog);
                        dialog1.setTitle("ASSIGN PLACE");
                        dialog1.setCancelable(true);
                        dialog.cancel();
                        // there are a lot of settings, for dialog, check them all out!
                        // set up radiobutton
                         title = (EditText) dialog1.findViewById(R.id.et);
                        radioGroup=(RadioGroup)dialog1.findViewById(R.id.radioGroup);
                        btnDisplay=(Button)dialog1.findViewById(R.id.button);
                        btnCancel=(Button)dialog1.findViewById(R.id.button1);
                        First=(RadioButton)dialog1.findViewById(R.id.radioButton);
                        Second=(RadioButton)dialog1.findViewById(R.id.radioButton2);
                        Third=(RadioButton)dialog1.findViewById(R.id.radioButton3);
                        Noplace=(RadioButton)dialog1.findViewById(R.id.radioButton4);
                        String s = sql.getRank(EventNo,ksid);

                        if(!sql.getTitle1(EventNo,ksid).equals(""))
                        {
                            title.setText(sql.getTitle1(EventNo,ksid));
                        }

                        if(s.equals("1")) {
                            First.toggle();
                            }
                        else if(s.equals("2")) {
                            Second.toggle();
                        }
                        else if(s.equals("3")) {
                            Third.toggle();
                        }
                        else {
                            Noplace.toggle();
                        }
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.cancel();
                            }
                        });

                        btnDisplay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final int selectedId=radioGroup.getCheckedRadioButtonId();
                                radioButton=(RadioButton)findViewById(selectedId);
                                dialog1.cancel();
                                final EditText edittext = new EditText(AddParticipant.this);
                                edittext.setHint("Enter here");
                                AlertDialog.Builder alert = new AlertDialog.Builder(AddParticipant.this,R.style.AppCompatAlertDialogStyle);
                                alert.setTitle("Enter Pasword");

                                if(selectedId == First.getId()) {
                                    a = "1";


                                } else if(selectedId == Second.getId()) {
                                    a = "2";
                                }
                                else if(selectedId == Third.getId()) {
                                    a = "3";
                                }
                                else {
                                    a = "";
                                }
                                alert.setView(edittext);

                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {


                                        //What ever you want to do with the value
//                                        //OR
//                                        String YouEditTextValue = edittext.getText().toString();
                                        prefs = getApplicationContext().getSharedPreferences(UserPref, 0);
                                        if(prefs.getString(Passkey,null).equals(edittext.getText().toString())){
                                            //Log.d("radio",(radioButton.toString()));
                                            //Log.d("selcttedid",selectedId+"");
                                        System.out.println(selectedId);
                                            System.out.println(radioButton);

                                        sql.updateRank(EventNo,ksid,a);
                                            if(!title.getText().toString().equals("")){
                                                sql.updatetitle(EventNo,ksid,title.getText().toString());
                                                Toast.makeText(getApplicationContext(),movie.getTitle()+" is assigned as "+title.getText().toString(),Toast.LENGTH_SHORT).show();

                                            }
                                            Toast.makeText(getApplicationContext(),movie.getTitle()+" is assigned as position "+a,Toast.LENGTH_SHORT).show();
                                                item1.setIcon(R.drawable.ic_action_update);

                                            Intent Cl = new Intent(getApplicationContext(), AddParticipant.class);
                                            Cl.putExtra("EventNo",EventNo);
                                            Cl.putExtra("EventName",EventName);
                                            System.out.println("NNNAAMMMEEE:"+movie.getGenre());
                                            startActivity(Cl);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"PASSWORD WRONG..TRY AGAIN",Toast.LENGTH_LONG).show();

                                        }
                                        mAdapter.notifyDataSetChanged();
                                        recyclerView.invalidate();
//                                        final SharedPreferences.Editor editor = prefs.edit();
//                                        //  try {

                                    }
                                });

                                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // what ever you want to do with No option.
                                    }
                                });

                                alert.show();

                            }
                        });
                        // now that the dialog is set up, it's time to show it
                        dialog1.show();
                    }
                });

/*                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });*/
                dialog.show();


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
                getothers();
                getallusers();
                mAdapter.notifyDataSetChanged();
                if(sql.dbSyncCount()==0)
                {
                    item1.setIcon(R.drawable.ic_action_cloud_done);
                }
                Intent Cl = new Intent(getApplicationContext(), AddParticipant.class);
                Cl.putExtra("EventNo",EventNo);
                Cl.putExtra("EventName",EventName);
                startActivity(Cl);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //Location
                //Call
                case 1:
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + mno));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    }
                    break;
                //Write external Storage

            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }



    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(AddParticipant.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddParticipant.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(AddParticipant.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(AddParticipant.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }



    private void prepareMovieData() {
        Movie movie;
        int i;
        for (i = 0; i< ParticipantsList.size();i++) {
            movie = new Movie(ParticipantsList.get(i).get("NAME"), ParticipantsList.get(i).get("KSID"),ParticipantsList.get(i).get("PLACE"),ParticipantsList.get(i).get("TITLE"));
            movieList.add(movie);
        }
        mAdapter.notifyItemChanged(i);      //  mAdapter.notifyDataSetChanged();



    }
    public  void  onFab(View view){
        Intent I = new Intent(this,SimpleScannerActivity.class);
        I.putExtra("EventNo",EventNo);
        I.putExtra("EventName",EventName);
        startActivity(I);
        //finish();

    }

//    public void onFab(View view){
//        Log.e("SIVA", "1");
//        // Prints scan results
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//
//
//        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
//
//        Log.e("SIVA", "2"); // Prints scan results
//        // Set the scanner view as the content view
//        setContentView(mScannerView);
//        Log.e("SIVA", "3"); // Prints scan results
//        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
//        Log.e("SIVA", "4"); // Prints scan results
//
//        mScannerView.startCamera();
//        Log.e("SIVA", "5"); // Prints scan results
//
//    }
//
//    @Override
//    public void handleResult(final Result rawResult) {
//        // Do something with the result here
//        mScannerView.stopCamera();
//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
//        builder.setTitle("Result");
//        builder.setMessage(rawResult.getText());
//
//        Log.d("Raw",rawResult.getText());
//        builder.setCancelable(false);
//        // HashMap<String, String> queryValues = new HashMap<String, String>();
//        // queryValues.put("userName", rawResult.getText());
//        if (rawResult.getText() != null
//                && rawResult.getText().trim().length() != 0) {
//            //controller.insertUser(queryValues);
//        } else {
//            Toast.makeText(getApplicationContext(), "Please enter User name",
//                    Toast.LENGTH_LONG).show();
//        }
//
//
//
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                mScannerView.startCamera();
//            }
//        });
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
//                Movie movie = new Movie(rawResult.getText(), rawResult.getText());
//                movieList.add(movie);
//                Toast.makeText(getApplicationContext(), rawResult.getText()+" added",
//                        Toast.LENGTH_SHORT).show();
//                mAdapter.notifyDataSetChanged();
//                Log.e("SIVA",movieList.isEmpty()+""); // Prints scan results
//
//                mScannerView.stopCamera();
//                //mScannerView.startCamera();
//            }
//        });
//        builder.show();
//        Toast.makeText(this,rawResult.getText(),Toast.LENGTH_LONG);
//        Toast.makeText(this,rawResult.getBarcodeFormat().toString(),Toast.LENGTH_LONG);
//
//
//        Log.e("SIVA", rawResult.getText()); // Prints scan results
//        Log.e("SIVA", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
//
//        // If you would like to resume scanning, call this method below:
//        mScannerView.resumeCameraPreview(this);
//    }



    public void syncSQLiteMySQLDB() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        ArrayList<HashMap<String, String>> userList =  sql.getAllUsers(EventNo);
        if(userList.size()!=0){
            if(sql.dbSyncCount() != 0){
                prgDialog.show();
                params.put("usersJSON", sql.composeJSONfromSQLite(EventNo));
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
                            Toast.makeText(getApplicationContext(), "Device might not be connected to Internet...Connect and Retry", Toast.LENGTH_LONG).show();
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
                    queryValues.put("EVENTNO", EventNo);
                    queryValues.put("GROUPNAME", null);

                    queryValues.put("ORGANIZER",obj.get("ORGANISER").toString() );
                    queryValues.put("ACCOM", obj.get("ACCOM").toString());
                    queryValues.put("ACCOM", obj.get("ACCOM").toString());
                    queryValues.put("ACCOM", obj.get("ACCOM").toString());

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

    private void getothers(){
       // ArrayList<String> ksids =  sql.getksid();

//        System.out.println(ksids);

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
        System.out.println("SSSSUUUPPPPEEERRRR   0000");
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        System.out.println("SSSSUUUPPPPEEERRRR   1111");
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
                getothers();
                getallusers();
                mAdapter.notifyDataSetChanged();
                if(sql.dbSyncCount()==0)
                {
                    item1.setIcon(R.drawable.ic_action_cloud_done);
                }
                Intent Cl = new Intent(getApplicationContext(), AddParticipant.class);
                Cl.putExtra("EventNo",EventNo);
                Cl.putExtra("EventName",EventName);
                startActivity(Cl);

                // refresh
                return true;
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Movie> filteredModelList = filter(movieList, newText);
        System.out.println("SSSSUUUPPPPEEERRRR   2222");
        mAdapter.setFilter(filteredModelList);
        System.out.println("SSSSUUUPPPPEEERRRR   3333");
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<Movie> filter(List<Movie> models, String query) {
        //query = query.toLowerCase();
        System.out.println("SSSSUUUPPPPEEERRRR   ssss1");
        final List<Movie> filteredModelList = new ArrayList<>();
        for (Movie model : models) {
            System.out.println("SSSSUUUPPPPEEERRRR   dddddd1");
            final String text2 = model.getGenre().toLowerCase();
           if(model.getTitle()!=null){ final String text1 = model.getTitle().toLowerCase();
            if (text1.contains(query) || text2.contains(query)) {
                filteredModelList.add(model);
            }}else {if (text2.contains(query) ) {
               filteredModelList.add(model);
           }}
        }
        return filteredModelList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent objIntent = new Intent(getApplicationContext(), ClusterEvents.class);
        startActivity(objIntent);
       // finish();
    }


}
