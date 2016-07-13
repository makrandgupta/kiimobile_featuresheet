package com.kiimobiletech.makrand.featuresheetgenerator;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by mvolpe on 2016-07-12.
 */
public class Generator {
    String template;
    DataContainer data;
    Document doc;

    public final static String TAG = "GENERATOR";

    public Generator(String template, DataContainer data) {
//        Log.d("Generator", "Constructor Invoked");
//        Log.d("Generator Tem", template);
//        Log.d("TemplateDone", "Done");
        this.template = template;
        this.data = data;
    }

    public void generateFeatureSheet() throws IOException {
        Log.i(TAG, "Inflating Template");
        doc = Jsoup.parse(template);

        doc.getElementById("agent_name").text(data.agentName);
        doc.getElementById("agent_email").text(data.agentEmail);
        doc.getElementById("agent_phone_number").text(data.agentPhone.toString());
        doc.getElementById("price_value").text(data.listingPrice.toString());
        doc.getElementById("address_value").text(data.listingAddress);

        Log.d(TAG, doc.getElementById("agent_name").toString());
    }

}
