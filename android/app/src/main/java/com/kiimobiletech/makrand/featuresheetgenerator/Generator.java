package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.kiimobiletech.makrand.featuresheetgenerator.dataInput.DataContainer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Makrand Gupta on 2016-07-12.
 */

public class Generator {
    String template  = null;
    DataContainer data = DataContainer.getInstance();
    Document doc;
    static Helpers helpers = null;
    static Context context = null;
    public final static String TAG = "GENERATOR";

    private static Generator instance = new Generator();

    public static Generator getInstance(Context ctxt) {
        context = ctxt;
        helpers = new Helpers(ctxt);
        return instance;
    }

    //singleton constructor to prevent instantiation
    public Generator() {}

    public void inflateTemplate() throws IOException {
        Log.i(TAG, "Inflating Template");
        //fetch template from assets
        this.template = helpers.loadFileFromAssets(Constants.TEMP_TEMPLATE);

        //parse using Jsoup
        this.doc = Jsoup.parse(this.template);

        //set the element place holders
        // TODO: Update this block to automatically populate all existing fields in template
        // Avoid hardcoding

        Log.d(TAG, this.data.agentEmail);
        this.doc.getElementById("agent_name").text(this.data.agentName);
        this.doc.getElementById("agent_email").text(this.data.agentEmail);
        this.doc.getElementById("agent_phone_number").text(this.data.agentPhone.toString());
        this.doc.getElementById("price_value").text(this.data.propertyPrice.toString());
        this.doc.getElementById("address_value").text(this.data.propertyAddress);

        /*
        * Encode entire image into a string and then display in HTML
        * */
        ByteArrayOutputStream tempImageStream = new ByteArrayOutputStream();
        data.tempImage.compress(Bitmap.CompressFormat.PNG, 100, tempImageStream);
        byte[] tempImageByteArray = tempImageStream.toByteArray();
        String imgToString = Base64.encodeToString(tempImageByteArray, Base64.DEFAULT);
        this.doc.getElementById("banner_image").attr("src", "data:image/png;base64," + imgToString);

        /*
        * Directly point to stored file in external storage
        * */

        String imagePath = "";
        String[] imgData = { MediaStore.Images.Media.DATA };
        Cursor imgCursor = context.getContentResolver().query(data.tempImageURI, imgData, null, null, null);
        if(imgCursor!=null) {
            int index = imgCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            imgCursor.moveToFirst();
            imagePath = imgCursor.getString(index);
        }
        else
            imagePath = data.tempImageURI.getPath();
//        Log.d(TAG, base);
//        String imagePath = "file://"+ base + "/test.jpg";
//        Log.d(TAG, tempFile.getAbsolutePath());
        this.doc.getElementById("banner_image").attr("src", imagePath);
    }

    public void saveDocument() throws TemplateException{
        this.template = helpers.loadFileFromAssets(Constants.TEMP_TEMPLATE);
        if(this.template != null) {
            //inflate template with data
            try {
                this.inflateTemplate();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //write to file
            helpers.writeToInternal(Constants.GENERATED_FILE, this.doc.toString());
        } else {
            throw new TemplateException("No Template Selected");
        }
    }

    /**
     * Custom template exception class.
     */
    public class TemplateException extends Exception
    {
        public TemplateException(String message)
        {
            super(message);
        }
    }

}
