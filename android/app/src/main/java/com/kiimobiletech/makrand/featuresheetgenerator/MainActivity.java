package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.Manifest;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kiimobiletech.makrand.featuresheetgenerator.dataInput.DataContainer;
import com.kiimobiletech.makrand.featuresheetgenerator.dataInput.DataInputActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    DataContainer dataContainer = DataContainer.getInstance();
    String[] templateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if(!Helpers.hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, Constants.PERMISSION_ALL);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        * load templates from assets
        * */
        try {
            templateList  = getAssets().list("templates");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (templateList != null) {
            Log.d("TEMPLATES", "found");
            for (String template: templateList) {
                //Do your stuff here
                Log.d("TEMPLATES", template);
            }
        }

        // Get the reference of ListViewAnimals
        ListView templateListView=(ListView)findViewById(R.id.template_picker_list_view);

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, templateList);
        // Set The Adapter
        templateListView.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        templateListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3)
            {
                String selectedTemplate = templateList[position];
                dataContainer.setTemplate(selectedTemplate);
                Intent intent = new Intent(MainActivity.this, DataInputActivity.class);
//                Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                Snackbar.make(v, selectedTemplate + "selected", Snackbar.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    protected void onResume() {
        super.onResume();
        dataContainer.empty();
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
}
