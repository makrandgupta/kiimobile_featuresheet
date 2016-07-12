package com.kiimobiletech.makrand.featuresheetgenerator;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

/**
 * Created by mvolpe on 2016-07-12.
 */
public class TemplateParser {
    String template;
    DataContainer data;
    Document doc;

    public TemplateParser(String template, DataContainer data) {
        Log.d("TemplateParser", "Constructor Invoked");
        Log.d("TemplateParser Tem", template);
        Log.d("TemplateDone", "Done");
        this.template = template;
        this.data = data;
    }

    public void inflate() throws IOException {
        Log.d("TemplateParser", "Inflating Template");
        doc = Jsoup.parse(template);

        doc.getElementById("agent_name").text(data.agentName);
        doc.getElementById("agent_email").text(data.agentEmail);
        doc.getElementById("agent_phone_number").text(data.agentPhone.toString());
        doc.getElementById("price_value").text(data.listingPrice.toString());
        doc.getElementById("address_value").text(data.listingAddress);

        Log.d("dump finished HTML", doc.getElementById("agent_name").toString());
    }

}
