package com.kiimobiletech.makrand.featuresheetgenerator;

/**
 * Created by mvolpe on 2016-07-12.
 */
public class DataContainer {
    public String agentName;
    public String agentEmail;
    public Integer agentPhone;
    public Integer listingPrice;
    public String listingAddress;

    public DataContainer(String agentName, String agentEmail, Integer agentPhone, Integer listingPrice, String listingAddress) {
        this.agentName = agentName;
        this.agentEmail = agentEmail;
        this.agentPhone = agentPhone;
        this.listingPrice = listingPrice;
        this.listingAddress = listingAddress;
    }

    @Override
    public String toString(){
        return "agentName: " + agentName + "; agentEmail: " + agentEmail + "; agentPhone: " + agentPhone + "; listingPrice: " + listingPrice + "; listingAddress: " + listingAddress;
    }

}
