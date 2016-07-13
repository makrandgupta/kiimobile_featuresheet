package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class DisplayActivity extends AppCompatActivity {
    public final static String TAG = "DISPLAY";
    Helpers helpers = new Helpers(getApplicationContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_display);

        WebView webView = new WebView(this);
        setContentView(webView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String payload = intent.getStringExtra(MainActivity.EXTRA_PAYLOAD);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.loadData(payload, "text/html", null);
//        String fileURL = "file:///" + MainActivity.GENERATED_FILE;
        String html = helpers.readFileFromInternal(MainActivity.GENERATED_FILE);
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
