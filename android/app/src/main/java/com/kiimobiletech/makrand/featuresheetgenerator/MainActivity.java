package com.kiimobiletech.makrand.featuresheetgenerator;

import android.os.Bundle;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button button;
    DataContainer data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("INFO", "Loaded!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nextButtonListener();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

    public void nextButtonListener(){
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //package the data into one object
                String agentName = null;
                if (((EditText) findViewById(R.id.agentName)) != null) {
                    agentName = (String) ((EditText) findViewById(R.id.agentName)).getText().toString();
                }
                String agentEmail = null;
                if (((EditText) findViewById(R.id.agentEmail)) != null) {
                    agentEmail = (String) ((EditText) findViewById(R.id.agentEmail)).getText().toString();
                }
//                Integer agentPhone = Integer.parseInt(String.valueOf((EditText) findViewById(R.id.agentPhone)));
//                Integer listingPrice = Integer.parseInt(String.valueOf((EditText) findViewById(R.id.listingPrice)));
                String listingAddress = null;
                if (((EditText) findViewById(R.id.listingAddress)) != null) {
                    listingAddress = (String) ((EditText) findViewById(R.id.listingAddress)).getText().toString();
                }

                data = new DataContainer("Agent Smith", "smith@agent.com", 123456, 456789, "123 Smithson Ave");
                Log.d("DATA", data.toString());
                //get html template
                String template = LoadData("template.html");
                Log.d("TEMPLATE", "INCOMING");
                Log.d("Template", template);
                Log.d("TEMPLATE", "Complete");
                //insert data into template
                TemplateParser parser = new TemplateParser(template, data);
                try {
                    parser.inflate();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //generate pdf from filled template
            }
        });
    }

    public String LoadData(String inFile) {
        String tContents = "";

        try {
            InputStream stream = getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }
}
