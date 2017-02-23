package edu.sastra.ks.ksorganizer;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class GroupScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    public String EventNo,EventName;
    String GroupName,Ksid,Name,Gen,Mob,Acc,Email,get,temp;
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

        EventNo = getIntent().getStringExtra("EventNo");
        EventName = getIntent().getStringExtra("EventName");
        GroupName = getIntent().getStringExtra("GroupName");
        getSupportActionBar().setTitle(GroupName);
        getSupportActionBar().setSubtitle(EventName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        try{

            // Do something with the result here
            mScannerView.stopCamera();
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            builder.setTitle("Result");


            if(rawResult.getBarcodeFormat().toString().equals("CODE_128")){

                builder.setMessage(rawResult.getText().toString());
                Ksid = rawResult.getText().toString();
            }else {
                get = MonoalphabeticCipher.doDecryption(rawResult.getText());
                Ksid = get.substring(0, get.indexOf("$%", 0));
                get = get.substring(get.indexOf("$%", 0) + 2, get.length());
                Name = get.substring(0, get.indexOf("$%", 0));
                get = get.substring(get.indexOf("$%", 0) + 2, get.length());
                Gen = get.substring(0, get.indexOf("$%", 0));
                get = get.substring(get.indexOf("$%", 0) + 2, get.length());
                Mob = get.substring(0, get.indexOf("$%", 0));
                get = get.substring(get.indexOf("$%", 0) + 2, get.length());
                Acc = get.substring(0, get.indexOf("$%", 0));
                get = get.substring(get.indexOf("$%", 0) + 2, get.length());
                Email = get.substring(0, get.length());
                builder.setMessage(Ksid + "\n" + Name  + "\n" + Gen + "\n" + Mob + "\n" + Acc + "\n" + Email);
            }
            Log.d("Raw", rawResult.getText());
            builder.setCancelable(false);
            // HashMap<String, String> queryValues = new HashMap<String, String>();
            // queryValues.put("userName", rawResult.getText());
            if (rawResult.getText() != null
                    && rawResult.getText().trim().length() != 0) {
                //controller.insertUser(queryValues);
            } else {
                Toast.makeText(getApplicationContext(), "Please enter User name",
                        Toast.LENGTH_LONG).show();
            }


            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mScannerView.startCamera();
                }
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getApplicationContext(), rawResult.getText(),
                            Toast.LENGTH_SHORT).show();
                    temp = rawResult.getText();
                    if(!sql.getksid(EventNo).contains(queryValues.get("KSID"))){
                    addtodb();}
                    else{
                        Toast.makeText(getApplicationContext(), "Already There!!", Toast.LENGTH_LONG).show();

                    }
                    Log.d("eventv jo", EventNo);
                    mScannerView.startCamera();
                }
            });
            builder.show();
            Toast.makeText(this, rawResult.getText(), Toast.LENGTH_LONG);
            Toast.makeText(this, rawResult.getBarcodeFormat().toString(), Toast.LENGTH_LONG);


            Log.wtf(TAG, rawResult.getText()); // Prints scan results
            Log.wtf(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

            // If you would like to resume scanning, call this method below:
            mScannerView.resumeCameraPreview(this);



        }catch (Exception e){
            mScannerView.stopCamera();
            Toast.makeText(getApplicationContext(), "Scan a Valid KS-ID or Registration No.", Toast.LENGTH_SHORT).show();
            mScannerView.resumeCameraPreview(this);


        }
    }


    void addtodb() {

        queryValues.put("KSID",Ksid );
        queryValues.put("NAME", Name);
        queryValues.put("GEN", Gen);
        queryValues.put("MOB", Mob);
        queryValues.put("EMAIL", Email);
        queryValues.put("ACCOM", Acc);
        queryValues.put("EVENTNO", EventNo);
        queryValues.put("GROUPNAME", GroupName);
        queryValues.put("ORGANIZER",   prefs.getString(Regikey,null));

        System.out.println(queryValues);


        sql.insertUser(queryValues);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent objIntent = new Intent(getApplicationContext(),
                GroupMembers.class);


        objIntent.putExtra("EventNo", EventNo);
        objIntent.putExtra("GroupName",GroupName);
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
                Intent objIntent = new Intent(getApplicationContext(),
                        GroupMembers.class);
                objIntent.putExtra("EventNo", EventNo);
                objIntent.putExtra("GroupName",GroupName);
                objIntent.putExtra("EventName", EventName);
                startActivity(objIntent);
                finish();

                // refresh
                return true;
            case android.R.id.home:
                onBackPressed();

                return true;
            case R.id.addNum:
                // check for updates action
                addKSID();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addKSID()
    {
        final EditText edittext = new EditText(GroupScanner.this);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittext.setText("KS");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                mScannerView.getWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);

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

        final AlertDialog.Builder alert = new AlertDialog.Builder(GroupScanner.this,R.style.AppCompatAlertDialogStyle);
        final AlertDialog OptionDialog = alert.create();
        alert.setTitle("Enter KS-ID");
        alert.setView(edittext);
        edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String YouEditTextValue = edittext.getText().toString();
                    if(!YouEditTextValue.equals("KS")||(YouEditTextValue.length()<4)) {
                        queryValues.put("KSID", YouEditTextValue);
                        queryValues.put("EVENTNO", EventNo);
                        queryValues.put("GROUPNAME",GroupName);
                        queryValues.put("ORGANIZER",  prefs.getString(Regikey,null));
                        sql.insertUser(queryValues);
                        Toast.makeText(getApplicationContext(), YouEditTextValue+"", Toast.LENGTH_SHORT).show();


                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Enter a Valid KS-ID", Toast.LENGTH_SHORT).show();
                        addKSID();

                    }
                }
                return false;
            }
        });

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //OR

                String YouEditTextValue = edittext.getText().toString();
                if(!YouEditTextValue.equals("KS")||(YouEditTextValue.length()<4)) {
                queryValues.put("KSID", YouEditTextValue);
                queryValues.put("EVENTNO", EventNo);
                queryValues.put("GROUPNAME",GroupName);
                queryValues.put("ORGANIZER",  prefs.getString(Regikey,null));
                sql.insertUser(queryValues);
                Toast.makeText(getApplicationContext(), YouEditTextValue+"", Toast.LENGTH_SHORT).show();
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter a Valid KS-ID", Toast.LENGTH_SHORT).show();
                    addKSID();

                }

            }
        });



        alert.show();


    }


}
