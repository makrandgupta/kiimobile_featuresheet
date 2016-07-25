package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mvolpe on 2016-07-13.
 */
public class Helpers {
    public final static String TAG = "HELPERS";
    Context context;



    public Helpers(Context context){
        this.context = context;
    }

    public String loadFileFromAssets(String inFile) {
        String output = "";

        try {
            InputStream stream = this.context.getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            output = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return output;

    }

    public String readFromInternal(String fileName) throws IOException{
        FileInputStream in = null;
        String line;

        Log.d("FILESDIR", this.context.getFilesDir().toString());

        //open file stream
        try {
            in = this.context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //initialize stream + buffer
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        //build string
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                Log.d(TAG, line);
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("File not found!");
        }
        Log.d(TAG, sb.toString());
        return sb.toString();
    }

    public void writeToInternal (String fileName, String data) {
        //open file stream to output the file
        FileOutputStream fOut = null;
        try {
            fOut = this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //write file to the filestream
        try {
            assert fOut != null;
            fOut.write(data.getBytes());
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    public String getFilePath(File file) {
        // Save a file: path for use with ACTION_VIEW intents
        return "file:" + file.getAbsolutePath();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
