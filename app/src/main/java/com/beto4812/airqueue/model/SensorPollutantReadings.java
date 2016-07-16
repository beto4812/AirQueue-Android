package com.beto4812.airqueue.model;


import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
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
    private List<Entry> lineEntries;
    private Boolean empty = true;
    private int lastEntryHour = 0;

    public SensorPollutantReadings(List<SensorReading> readings, String pollutantCode){
        Log.v(LOG_TAG, " pollutantCode: " + pollutantCode);
        pollutants = new HashMap<>();
        lineEntries = new ArrayList<>();
        this.pollutantCode = pollutantCode;

        for(int i = 0; i <readings.size(); i++){
            if(readings.get(i).getPollutant(pollutantCode)!=null){
                empty = false;
                Log.v(LOG_TAG, " readings: : " + readings.get(i));
                pollutants.put(readings.get(i).getLastUpdated(), readings.get(i).getPollutant(pollutantCode));
                lineEntries.add(new Entry(readings.get(i).getPollutant(pollutantCode).getFloatValue(), i));
                lastEntryHour = readings.get(i).getLastUpdatedHour()>lastEntryHour? readings.get(i).getLastUpdatedHour(): lastEntryHour;
                //Log.v(LOG_TAG, " entry: : " + new Entry(readings.get(i).getPollutant(pollutantCode).getFloatValue(), readings.get(i).getLastUpdatedHour()));
            }
        }
    }

    public Boolean hasData(){
        return !empty;
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

    public List<Entry> getLineEntries(){
        return lineEntries;
    }

    public int getLastEntryHour(){
        return lastEntryHour;
    }
}
