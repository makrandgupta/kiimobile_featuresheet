package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_PAYLOAD = "com.kiimobiletech.makrand.featuresheetgenerator.FILLED";
    public final static String GENERATED_FILE = "generated.html";
    Button startBtn;
    DataContainer data;

    Helpers helpers = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("INFO", "Loaded!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helpers = new Helpers(this.getApplicationContext());

//        nextButtonListener();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //package the data into one object
                viewGeneratedDocument(view);
            }
        });

        startBtn = (Button) findViewById(R.id.button);
        startBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                letsGetStarted(view);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.kiimobiletech.makrand.featuresheetgenerator/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.kiimobiletech.makrand.featuresheetgenerator/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void viewGeneratedDocument(View view){
        String agentName = null;
        EditText agentNameEditText = ((EditText) findViewById(R.id.agentName));
        if ( agentNameEditText != null) {
            agentName = agentNameEditText.getText().toString();
        }
        String agentEmail = null;
        EditText agentEmailEditText = ((EditText) findViewById(R.id.agentEmail));
        if ( agentEmailEditText!= null) {
            agentEmail =  agentEmailEditText.getText().toString();
        }
        Integer agentPhone = null;
        EditText agentPhoneEditText = ((EditText) findViewById(R.id.agentPhone));
        if (agentPhoneEditText!= null) {
            agentPhone = Integer.parseInt(agentPhoneEditText.getText().toString());
        }

        Integer listingPrice = null;
        EditText listingPriceEditText = ((EditText) findViewById(R.id.listingPrice));
        if (listingPriceEditText != null) {
            listingPrice = Integer.parseInt(listingPriceEditText.getText().toString());
        }
        String listingAddress = null;
        EditText listingAddressEditText = ((EditText) findViewById(R.id.listingAddress));
        if (listingAddressEditText != null) {
            listingAddress = listingAddressEditText.getText().toString();
        }

        //encapsulate the data
//                data = new DataContainer("Agent Smith", "smith@agent.com", 123456, 456789, "123 Smithson Ave");
        data = new DataContainer(agentName, agentEmail, agentPhone, listingPrice, listingAddress);
        //get html template
        String template = helpers.loadFileFromAssets("template.html");

        //insert data into template

        //initialize the Feature Sheet Generator
        Generator generator = new Generator(template, data);

        //Generate the feature sheet
        try {
            generator.generateFeatureSheet();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //open file stream to output the file
        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput(GENERATED_FILE, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //write file to the filestream
        try {
            assert fOut != null;
            fOut.write(generator.doc.toString().getBytes());
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //display html
        Intent intent = new Intent(view.getContext(), DisplayActivity.class);
        intent.putExtra(EXTRA_PAYLOAD, generator.doc.toString());
        startActivity(intent);
        //generate pdf from filled template

    }

    public void letsGetStarted(View view){
        Intent intent = new Intent(view.getContext(), DataInputActivity.class);
        startActivity(intent);
    }
}
