package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

public class DisplayActivity extends AppCompatActivity {
    public final static String TAG = "DISPLAY";
    Helpers helpers;
    Generator generator = Generator.getInstance(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helpers = new Helpers(getApplicationContext());
//        setContentView(R.layout.activity_display);

        try {
            generator.saveDocument();
        } catch (Generator.TemplateException e) {
            e.printStackTrace();
        }


        WebView webView = new WebView(this);
        setContentView(webView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String payload = intent.getStringExtra(Constants.EXTRA_PAYLOAD);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadData(payload, "text/html", null);
//        String fileURL = "file:///" + MainActivity.GENERATED_FILE;
        String html = helpers.readFileFromInternal(Constants.GENERATED_FILE);
        webView.loadData(html, "text/html", "UTF-8");

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
