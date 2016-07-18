package com.beto4812.airqueue.utils;

import com.beto4812.airqueue.model.PollutantCategoryInfo;
import com.beto4812.airqueue.model.PollutantThreshold;
import com.beto4812.airqueue.model.SensorCoordinates;
import com.beto4812.airqueue.model.SensorReading;

import java.util.HashMap;
import java.util.List;


public class DataSingelton {

    private HashMap<String, PollutantThreshold> pollutantThresholds;
    private SensorCoordinates sensorCoordinates;
    private HashMap<String, PollutantCategoryInfo> pollutantCategoriesInfo;
    private List<SensorReading> sensorReadings;
    private static DataSingelton instance;

    public static DataSingelton getInstance(){
        if(instance == null){
            instance = new DataSingelton();
        }
        return  instance;
    }


    public HashMap<String, PollutantThreshold> getPollutantThresholds() {
        return pollutantThresholds;
    }

    public void setPollutantThresholds(HashMap<String, PollutantThreshold> pollutantThresholds) {
        this.pollutantThresholds = pollutantThresholds;
    }

    public SensorCoordinates getSensorCoordinates() {
        return sensorCoordinates;
    }

    public void setSensorCoordinates(SensorCoordinates sensorCoordinates) {
        this.sensorCoordinates = sensorCoordinates;
    }

    public HashMap<String, PollutantCategoryInfo> getPollutantCategoriesInfo() {
        return pollutantCategoriesInfo;
    }

    public void setPollutantCategoriesInfo(HashMap<String, PollutantCategoryInfo> pollutantCategoriesInfo) {
        this.pollutantCategoriesInfo = pollutantCategoriesInfo;
    }

    public List<SensorReading> getSensorReadings() {
        return sensorReadings;
    }

    public void setSensorReadings(List<SensorReading> sensorReadings) {
        this.sensorReadings = sensorReadings;
    }

    public boolean isEmpty(){
        if(sensorReadings==null){
            return true;
        }else return false;
    }

}
