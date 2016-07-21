package com.kiimobiletech.makrand.featuresheetgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.kiimobiletech.makrand.featuresheetgenerator.dataInput.DataContainer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by Makrand Gupta on 2016-07-12
 */

public class Generator {
    static Document doc;
    static DataContainer dataContainer = DataContainer.getInstance();
    static Helpers helpers = null;
    static Context context = null;
    public final static String TAG = "GENERATOR";

    public Elements propertyTextElements = new Elements();
    public Elements propertyImageElements = new Elements();
    public Elements agentImageElements = new Elements();
    public Elements agentTextElements = new Elements();

    private static Generator instance = new Generator();

    public static Generator getInstance(Context ctxt) throws TemplateException {
        if(dataContainer.templateName == null){
            throw new TemplateException("Template not selected");
        }
        context = ctxt;
        helpers = new Helpers(ctxt);

        String template = helpers.loadFileFromAssets("templates/" + dataContainer.templateName);
        doc = Jsoup.parse(template);
        Log.d(TAG, "raw template: " + template);

        return instance;
    }

    //singleton constructor to prevent instantiation
    public Generator() {}

    public void inflateTemplate() throws IOException, TemplateException {
        Log.i(TAG, "Inflating Template");
        //fetch templateName from assets
//        this.templateName = helpers.loadFileFromAssets(Constants.TEMP_TEMPLATE);
        if(dataContainer.templateName == null) {
            throw new TemplateException("Template not selected");
        }

        //set the element place holders
        // TODO: Update this block to automatically populate all existing fields in templateName
        // Avoid hardcoding

        Log.d(TAG, dataContainer.agentEmail);
        doc.getElementById("agent_name").text(dataContainer.agentName);
        doc.getElementById("agent_email").text(dataContainer.agentEmail);
        doc.getElementById("agent_phone_number").text(dataContainer.agentPhone.toString());
        doc.getElementById("property_price_value").text(dataContainer.propertyPrice.toString());
        doc.getElementById("property_address_value").text(dataContainer.propertyAddress);

        /*
        * Encode entire image into a string and then display in HTML
        * */
        ByteArrayOutputStream tempImageStream = new ByteArrayOutputStream();
        dataContainer.tempImage.compress(Bitmap.CompressFormat.PNG, 100, tempImageStream);
        byte[] tempImageByteArray = tempImageStream.toByteArray();
        String imgToString = Base64.encodeToString(tempImageByteArray, Base64.DEFAULT);
        doc.getElementById("property_banner_image").attr("src", "data:image/png;base64," + imgToString);

        /*
        * Directly point to stored file in external storage
        * */
//
//        String imagePath = "";
//        String[] imgData = { MediaStore.Images.Media.DATA };
//        Cursor imgCursor = context.getContentResolver().query(dataContainer.tempImageURI, imgData, null, null, null);
//        if(imgCursor!=null) {
//            int index = imgCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            imgCursor.moveToFirst();
//            imagePath = imgCursor.getString(index);
//        }
//        else
//            imagePath = dataContainer.tempImageURI.getPath();
//        Log.d(TAG, base);
//        String imagePath = "file://"+ base + "/test.jpg";
//        Log.d(TAG, tempFile.getAbsolutePath());
//        doc.getElementById("banner_image").attr("src", imagePath);
//        assert imgCursor != null;
//        imgCursor.close();
    }

    public void saveDocument(){
        //inflate templateName with dataContainer
        try {
            this.inflateTemplate();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        //write to file
        helpers.writeToInternal(Constants.GENERATED_FILE, doc.toString());

    }

    public void parseTemplate() {
        Log.d(TAG, "Template parsing started");
        Log.d(TAG, doc.html());
        Log.d(TAG, "Test" + doc.getElementsByTag("body").html());

        for(Element test : doc.getElementsByAttribute("editable")){
            Log.d(TAG, "ID: " + test.id() + "; editable=" + test.attr("editable"));
        }

        for(Element temp : doc.getElementsByAttributeValueStarting("id", "property_")) {
            Log.d(TAG, "Element found:" + temp.id() + "; editable=" + temp.attr("editable"));
            if(Objects.equals(temp.attr("editable"), "image")){
                Log.d(TAG, "Element saved: " + temp.id());
                this.propertyImageElements.add(temp);
            }
            if(Objects.equals(temp.attr("editable"), "text")){
                Log.d(TAG, "Element saved: " + temp.id());
                this.propertyTextElements.add(temp);
            }
        }

        for(Element temp : doc.getElementsByAttributeValueStarting("id", "agent_")) {
            if(Objects.equals(temp.attr("editable"), "image")){
                this.agentImageElements.add(temp);
            }
            if(Objects.equals(temp.attr("editable"), "text")){
                this.agentTextElements.add(temp);
            }
        }
    }

    /**
     * Custom templateName exception class.
     */
    public static class TemplateException extends Exception
    {
        public TemplateException(String message)
        {
            super(message);
        }
    }

}
