package com.beto4812.airqueue.model;


import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Contains all readings of a specific sensor and pollutant over a period of time
 */
public class SensorPollutantReadings {

    private static final String LOG_TAG = "SensorPollutantReadings";

    //List<Pollutant> pollutants;
    private String sourceID;
    private String pollutantCode;
    private HashMap<String, Pollutant> pollutants;

    public SensorPollutantReadings(List<SensorReading> readings, String pollutantCode){
        Log.v(LOG_TAG, " pollutantCode: " + pollutantCode);
        pollutants = new HashMap<>();
        this.pollutantCode = pollutantCode;
        for(SensorReading sens: readings){
            if(sens.getPollutant(pollutantCode)!=null){
                Log.v(LOG_TAG, " readings: : " + sens);
                pollutants.put(sens.getLastUpdated(), sens.getPollutant(pollutantCode));
            }
        }
    }


    public HashMap<String, Pollutant> getTimePollutants(){
        return pollutants;
    }

    public Collection<Pollutant> getPollutants() {
        Log.v(LOG_TAG, "getPollutants size: " + pollutants.values().size());
        return pollutants.values();
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getPollutantCode(){
        return pollutantCode;
    }
}
