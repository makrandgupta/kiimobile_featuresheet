package com.kiimobiletech.makrand.featuresheetgenerator.dataInput;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Makrand Gupta on 2016-07-12.
 */
public class DataContainer {
    public String agentName;
    public String agentEmail;
    public Integer agentPhone;

    public Integer propertyPrice;
    public String propertyAddress;

    public Bitmap tempImage;

    private static final String TAG = "DataContainer";

    private static DataContainer instance = new DataContainer();


    //singleton
    private DataContainer() {}

    public static DataContainer getInstance() {
        return instance;
    }

    public void setAgentInfo(String agentName, String agentEmail, Integer agentPhone) {
        this.agentName = agentName;
        this.agentEmail = agentEmail;
        this.agentPhone = agentPhone;

        Log.d(TAG, instance.toString());
    }

    public void setPropertyInfo(String listingAddress, Integer listingPrice) {
         this.propertyAddress = listingAddress;
        this.propertyPrice = listingPrice;
    }

    public boolean completed() {
        if(agentName != null
                && agentEmail != null
                && agentPhone != null
                && propertyPrice != null
                && propertyAddress != null)
            return true;
        return false;
    }

    public void empty() {
        agentName = null;
        agentEmail = null;
        agentPhone = null;
        propertyPrice = null;
        propertyAddress = null;
        tempImage = null;
    }

    @Override
    public String toString(){
        return "agentName: " + agentName
                + "; agentEmail: " + agentEmail
                + "; agentPhone: " + agentPhone
                + "; propertyPrice: " + propertyPrice
                + "; propertyAddress: " + propertyAddress;
    }

}
