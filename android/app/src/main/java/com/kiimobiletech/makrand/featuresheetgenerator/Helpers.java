package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

    public String readFileFromInternal(String fileName) {
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
}
