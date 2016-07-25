package com.kiimobiletech.makrand.featuresheetgenerator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kiimobiletech.makrand.featuresheetgenerator.dataInput.DataContainer;
import com.kiimobiletech.makrand.featuresheetgenerator.dataInput.DataInputActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayActivity extends AppCompatActivity {
    public final static String TAG = "DISPLAY";
    Helpers helpers;
    DataContainer dataContainer = DataContainer.getInstance();
    Generator generator;

    WebView webView;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR=1;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        helpers = new Helpers(getApplicationContext());
//        try {
//            generator = Generator.getInstance(getApplicationContext());
//        } catch (Generator.TemplateException e) {
//            e.printStackTrace();
//        }
//
//        generator.saveDocument();


//        WebView webView = new WebView(this);
//        setContentView(webView);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        Intent intent = getIntent();
//        String payload = intent.getStringExtra(Constants.EXTRA_PAYLOAD);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setDomStorageEnabled(true);
//        webView.loadData(payload, "text/html", null);
//        String html = helpers.readFromInternal(Constants.GENERATED_FILE);
//        webView.loadDataWithBaseURL("file://", html, "text/html", "UTF-8", "");
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

//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;
            //Check if response is positive
            if(resultCode== Activity.RESULT_OK){
                if(requestCode == FCR){
                    if(null == mUMA){
                        return;
                    }
                    if(intent == null){
                        //Capture Photo if no image available
                        if(mCM != null){
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    }else{
                        String dataString = intent.getDataString();
                        if(dataString != null){
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        }else{
            if(requestCode == FCR){
                if(null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        helpers = new Helpers(getApplicationContext());
        try {
            generator = Generator.getInstance(getApplicationContext());
        } catch (Generator.TemplateException e) {
            e.printStackTrace();
        }

        generator.saveDocument();

        Log.d(TAG, "Triggered!");
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        String html = null;
        try {
            html = helpers.readFromInternal(Constants.GENERATED_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String html = helpers.loadFileFromAssets(Constants.TEMP_TEMPLATE);
        webView.loadDataWithBaseURL("file://", html, "text/html", "UTF-8", "");


        if(Build.VERSION.SDK_INT >= 21){
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if(Build.VERSION.SDK_INT >= 19){
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if(Build.VERSION.SDK_INT >=11 && Build.VERSION.SDK_INT < 19){
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
                                       //For Android 4.1+
                                       public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                                           mUM = uploadMsg;
                                           Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                           i.addCategory(Intent.CATEGORY_OPENABLE);
                                           i.setType("image/*");
                                           startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
                                       }

                                       //For Android 5.0+
                                       public boolean onShowFileChooser(
                                               WebView webView, ValueCallback<Uri[]> filePathCallback,
                                               WebChromeClient.FileChooserParams fileChooserParams) {
                                           if (mUMA != null) {
                                               mUMA.onReceiveValue(null);
                                           }
                                           mUMA = filePathCallback;
                                           Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                           if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                               File photoFile = null;
                                               try {
                                                   photoFile = createImageFile();
                                                   takePictureIntent.putExtra("PhotoPath", mCM);
                                               } catch (IOException ex) {
                                                   Log.e(TAG, "Image file creation failed", ex);
                                               }
                                               if (photoFile != null) {
                                                   mCM = "file:" + photoFile.getAbsolutePath();
                                                   takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                               } else {
                                                   takePictureIntent = null;
                                               }
                                           }
                                           Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                           contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                           contentSelectionIntent.setType("image/*");
                                           Intent[] intentArray;
                                           if (takePictureIntent != null) {
                                               intentArray = new Intent[]{takePictureIntent};
                                           } else {
                                               intentArray = new Intent[0];
                                           }

                                           Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                                           chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                                           chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                                           chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                                           startActivityForResult(chooserIntent, FCR);
                                           return true;
                                       }
                                   });
        setContentView(webView);
    }
    // Create an image file
    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode){
                case KeyEvent.KEYCODE_BACK:
                    if(webView.canGoBack()){
                        webView.goBack();
                    }else{
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }


}
