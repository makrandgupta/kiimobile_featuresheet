package com.kiimobiletech.makrand.featuresheetgenerator;

/**
 * Created by Makrand Gupta on 2016-07-12.
 */
public class DataContainer {
    public String agentName;

    public String agentEmail;
    public Integer agentPhone;
    public Integer listingPrice;
    public String listingAddress;
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
    }

    public void setListgingInfo (String listingAddress, Integer listingPrice) {
         this.listingAddress = listingAddress;
        this.listingPrice = listingPrice;
    }

    @Override
    public String toString(){
        return "agentName: " + agentName + "; agentEmail: " + agentEmail + "; agentPhone: " + agentPhone + "; listingPrice: " + listingPrice + "; listingAddress: " + listingAddress;
    }

}
