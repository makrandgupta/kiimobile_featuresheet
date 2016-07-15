package com.kiimobiletech.makrand.featuresheetgenerator;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Makrand Gupta on 2016-07-12.
 */

public class Generator {
    String template  = null;
    DataContainer data = DataContainer.getInstance();
    Document doc;
    Helpers helpers = null;
    public final static String TAG = "GENERATOR";

    private static Generator instance = new Generator();

    public static Generator getInstance() {
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
        this.doc.getElementById("agent_name").text(this.data.agentName);
        this.doc.getElementById("agent_email").text(this.data.agentEmail);
        this.doc.getElementById("agent_phone_number").text(this.data.agentPhone.toString());
        this.doc.getElementById("price_value").text(this.data.propertyPrice.toString());
        this.doc.getElementById("address_value").text(this.data.propertyAddress);
    }

    public void saveDocument() throws TemplateException{
        if(this.template != null) {
            //inflate template with data
            try {
                this.inflateTemplate();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //write to file
            this.helpers.writeToInternal(Constants.GENERATED_FILE, this.doc.toString());
        } else {
            throw new TemplateException("No Template Selected");
        }
    }

    /**
     * Custom template exception class.
     */
    class TemplateException extends Exception
    {
        public TemplateException(String message)
        {
            super(message);
        }
    }

}
