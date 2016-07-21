package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.kiimobiletech.makrand.featuresheetgenerator.dataInput.DataContainer;

public class DisplayActivity extends AppCompatActivity {
    public final static String TAG = "DISPLAY";
    Helpers helpers;
    DataContainer dataContainer = DataContainer.getInstance();
    Generator generator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helpers = new Helpers(getApplicationContext());
        try {
            generator = Generator.getInstance(getApplicationContext());
        } catch (Generator.TemplateException e) {
            e.printStackTrace();
        }

        generator.saveDocument();


        WebView webView = new WebView(this);
        setContentView(webView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String payload = intent.getStringExtra(Constants.EXTRA_PAYLOAD);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadData(payload, "text/html", null);
        String html = helpers.readFileFromInternal(Constants.GENERATED_FILE);
        webView.loadDataWithBaseURL("file://", html, "text/html", "UTF-8", "");
//        webView.loadData(html, "text/html", "UTF-8");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
