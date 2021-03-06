package edu.sastra.ks.ksorganizer;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.HashMap;

import edu.sastra.ks.ksorganizer.Databases.SqliteDB;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static edu.sastra.ks.ksorganizer.R.id.linearLayout;

public class SimpleScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    public String EventNo,EventName;
    int n;
    String GroupName,Ksid,Name,Coll,Gen,Mob,Acc,Email,get;
    SqliteDB sql;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

    HashMap<String, String> queryValues = new HashMap<String, String>();
    SharedPreferences prefs;

    public static final String UserPref = "Userpref";
    public static final String Regikey = "RegiKey";
    public static final String Passkey = "PassKey";
    public static final String Clusterkey = "Clusterkey";

    SqliteDB controller = new SqliteDB(this);
    String TAG = "com.myapplication.siva.Carpedium";

    @Override
    public void onCreate(Bundle state) {
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        }
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        // Set the scanner view as the content view
        setContentView(mScannerView);
        sql = new SqliteDB(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EventNo = getIntent().getStringExtra("EventNo");
        EventName = getIntent().getStringExtra("EventName");
        getSupportActionBar().setTitle(EventName);
        prefs = getApplicationContext().getSharedPreferences(UserPref, Context.MODE_PRIVATE);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(final Result rawResult) {

        // Do something with the result here
        mScannerView.stopCamera();
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Result");
        //builder.setMessage(rawResult.getText());
        System.out.println(rawResult.getBarcodeFormat());
        if(rawResult.getBarcodeFormat().toString().equals("CODE_128")){

            builder.setMessage(rawResult.getText().toString());
            Ksid = rawResult.getText().toString();
            Coll = "Sastra University";
        }else {

            get = MonoalphabeticCipher.doDecryption(rawResult.getText());
            Ksid = get.substring(0, get.indexOf("$%", 0));
            get = get.substring(get.indexOf("$%", 0) + 2, get.length());
            Name = get.substring(0, get.indexOf("$%", 0));
            get = get.substring(get.indexOf("$%", 0) + 2, get.length());
            Coll = get.substring(0, get.indexOf("$%", 0));
            get = get.substring(get.indexOf("$%", 0) + 2, get.length());
            Gen = get.substring(0, get.indexOf("$%", 0));
            get = get.substring(get.indexOf("$%", 0) + 2, get.length());
            Mob = get.substring(0, get.indexOf("$%", 0));
            get = get.substring(get.indexOf("$%", 0) + 2, get.length());
            Acc = get.substring(0, get.indexOf("$%", 0));
            get = get.substring(get.indexOf("$%", 0) + 2, get.length());
            Email = get.substring(0, get.length());
            builder.setMessage(Ksid + "\n" + Name + "\n" + Coll + "\n" + Gen + "\n" + Mob + "\n" + Acc + "\n" + Email);


        }

        Log.d("Raw", rawResult.getText());
        builder.setCancelable(false);
                // HashMap<String, String> queryValues = new HashMap<String, String>();
                // queryValues.put("userName", rawResult.getText());
        if (rawResult.getText() != null
                && rawResult.getText().trim().length() != 0) {
            //controller.insertUser(queryValues);
        } else {
//            Toast.makeText(getApplicationContext(), "Please enter User name",
//                    Toast.LENGTH_LONG).show();
        }


        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mScannerView.startCamera();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Added",
                        Toast.LENGTH_SHORT).show();
               // GroupName = rawResult.getText();
                addtodb();
                Log.d("eventv jo", EventNo);
                mScannerView.startCamera();
            }
        });
        builder.show();

        //System.out.println(MonoalphabeticCipher.doDecryption("KS001245$%SIVA SUBRAMANIAN$%SASTRA UNIVERSITY$%M$%8438181024$%YES$%SIVASUBRAMANIAN96@GMAIL.COM"));
        Toast.makeText(this, rawResult.getText(), Toast.LENGTH_SHORT);


        Log.wtf(TAG, rawResult.getText()); // Prints scan results

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }


    void addtodb() {

        //queryValues.put("get", Ksid);
        queryValues.put("KSID",Ksid);
        queryValues.put("NAME", Name);
        queryValues.put("GEN", Gen);
        queryValues.put("CLG", Coll);
        queryValues.put("MOB", Mob);
        queryValues.put("EMAIL", Email);
        queryValues.put("ACCOM", Acc);
        queryValues.put("EVENTNO", EventNo);
       // queryValues.put("GROUPNAME", GroupName);
        prefs = getApplicationContext().getSharedPreferences(UserPref, Context.MODE_PRIVATE);

        System.out.println(prefs.getString(Regikey,null));
        queryValues.put("ORGANIZER",  prefs.getString(Regikey,null));
        System.out.println(queryValues);
        sql.insertUser(queryValues);
        queryValues =null;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent objIntent = new Intent(getApplicationContext(), AddParticipant.class);
        objIntent.putExtra("EventNo", EventNo);
        objIntent.putExtra("EventName", EventName);
        startActivity(objIntent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_scanner_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_done:
                // refresh
                Intent objIntent = new Intent(getApplicationContext(), AddParticipant.class);
                objIntent.putExtra("EventNo", EventNo);
                objIntent.putExtra("EventName", EventName);

                startActivity(objIntent);
                finish();
                return true;
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            case R.id.addNum:
                // check for updates action
                addget();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void addget()
    {
        final EditText edittext = new EditText(SimpleScannerActivity.this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);

        final InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                mScannerView.getWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);


        edittext.setText("KS");

        int textLength = edittext.getText().length();
        edittext.setSelection(textLength, textLength);
        edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().contains("KS")){
                    edittext.setText("KS");
                    Selection.setSelection(edittext.getText(), edittext.getText().length());

                }

            }
        });
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);

        final AlertDialog.Builder alert = new AlertDialog.Builder(SimpleScannerActivity.this,R.style.AppCompatAlertDialogStyle);
        final AlertDialog OptionDialog = alert.create();
        alert.setTitle("Enter KS-ID");

        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String YouEditTextValue = edittext.getText().toString();
                    if(!(YouEditTextValue.equals("KS")||(YouEditTextValue.length()<4))) {
                        // queryValues.put("get", YouEditTextValue);
                        queryValues.put("KSID", YouEditTextValue);
                        queryValues.put("EVENTNO", EventNo);
                        queryValues.put("ORGANIZER", prefs.getString(Regikey, null));
                        sql.insertUser(queryValues);
                        Toast.makeText(getApplicationContext(), YouEditTextValue + " Added", Toast.LENGTH_SHORT).show();
                        OptionDialog.dismiss();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Enter a Valid KS-ID", Toast.LENGTH_SHORT).show();
                        addget();
                    }

                }
                return false;
            }
        });
        alert.setView(edittext);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //OR
                String YouEditTextValue = edittext.getText().toString();
                if(!(YouEditTextValue.equals("KS")||(YouEditTextValue.length()<4))) {
                    // queryValues.put("get", YouEditTextValue);
                    queryValues.put("KSID", YouEditTextValue);
                    queryValues.put("EVENTNO", EventNo);
                    queryValues.put("ORGANIZER", prefs.getString(Regikey, null));
                    sql.insertUser(queryValues);
                    Toast.makeText(getApplicationContext(), YouEditTextValue + " Added", Toast.LENGTH_SHORT).show();
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter a Valid KS-ID", Toast.LENGTH_SHORT).show();
                    addget();
                }

            }
        });



        alert.show();


    }

    }



