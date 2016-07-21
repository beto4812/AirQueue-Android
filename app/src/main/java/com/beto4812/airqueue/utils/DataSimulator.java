package com.beto4812.airqueue.utils;

import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.PollutantCategoryInfo;
import com.beto4812.airqueue.model.PollutantThreshold;
import com.beto4812.airqueue.model.SensorReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DataSimulator {

    private static String closestSimulatedSourceID = "ED3";
    private HashMap<String, String[]> pollutant_thresholds_data;
    private HashMap<String, PollutantThreshold> pollutantThresholds;
    private HashMap<String, String> pollutant_categories_info_data;
    private HashMap<String, PollutantCategoryInfo> pollutantCategoryInfo;

    public DataSimulator(){
        initPollutantThresholds();
        initPollutantCategoriesInfo();
    }

    private void initPollutantThresholds(){
        pollutant_thresholds_data = new HashMap();
        //"black (S)","green (S)","red (S)","yellow (S)","above_black (S)"
        pollutant_thresholds_data.put("PM2P5", new String[]{"71","0","54","36","140"});
        pollutant_thresholds_data.put("NO2", new String[]{"601","0","401","201","700"});
        pollutant_thresholds_data.put("SO2", new String[]{"1064","0","532","266","1264"});
        pollutant_thresholds_data.put("PM10", new String[]{"101","0","76","51","150"});
        pollutant_thresholds_data.put("O3", new String[]{"241","0","161","81","320"});
        pollutant_thresholds_data.put("CO", new String[]{"50","2","30","4","100"});
        //missing some values from original table (Maybe unused)

        pollutantThresholds = new HashMap<>();

        Iterator it = pollutant_thresholds_data.values().iterator();

        while (it.hasNext()){
            String currentCode = (String) it.next();
            String currentThresholds[] = pollutant_thresholds_data.get(currentCode);
            pollutantThresholds.put(currentCode, new PollutantThreshold(currentCode, currentThresholds[1], currentThresholds[3], currentThresholds[2], currentThresholds[0], currentThresholds[4]));
        }
    }

    private void initPollutantCategoriesInfo(){
        pollutant_categories_info_data = new HashMap<>();
        pollutant_categories_info_data.put("Sulphur Dioxide", "http://albertovazquez.nfshost.com/airqueue/so2.html");
        pollutant_categories_info_data.put("Carbon Monoxide","http://albertovazquez.nfshost.com/airqueue/co.html");
        pollutant_categories_info_data.put("Ozone","http://albertovazquez.nfshost.com/airqueue/o3.html");
        pollutant_categories_info_data.put("Nitrogen Oxides","http://albertovazquez.nfshost.com/airqueue/no.html");
        pollutant_categories_info_data.put("Particulate Matter","http://albertovazquez.nfshost.com/airqueue/pm.html");

        pollutantCategoryInfo = new HashMap<>();
        Iterator it = pollutant_categories_info_data.keySet().iterator();
        while (it.hasNext()){
            String currentCategory = (String)it.next();
            pollutantCategoryInfo.put(currentCategory, new PollutantCategoryInfo(currentCategory, pollutant_categories_info_data.get(currentCategory)));
        }
    }

    public HashMap<String, PollutantThreshold> getPollutantThresholds(){
        return pollutantThresholds;
    }



    public HashMap<String, PollutantCategoryInfo> getPollutantCategoriesInfo(){
        return pollutantCategoryInfo;
    }

    /*public List<SensorReading> getSensorReadings(){
        List<SensorReading> sensorReadings = new ArrayList<>();

        Set<String> renderedPollutants = Pollutant.RENDERED_POLLUTANTS;
        Iterator it = renderedPollutants.iterator();

        //Start hour

        /*while (it.hasNext()){
            sensorReadings.add(new SensorReading());

        }

    }*/

}