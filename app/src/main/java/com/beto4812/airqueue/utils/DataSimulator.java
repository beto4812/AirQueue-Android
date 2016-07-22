package com.beto4812.airqueue.utils;

import com.beto4812.airqueue.model.Pollutant;
import com.beto4812.airqueue.model.PollutantCategoryInfo;
import com.beto4812.airqueue.model.PollutantThreshold;
import com.beto4812.airqueue.model.SensorReading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DataSimulator {

    private static String closestSimulatedSourceID = "ED3";
    private HashMap<String, String[]> pollutant_thresholds_data;
    private HashMap<String, PollutantThreshold> pollutantThresholds;
    private HashMap<String, String> pollutant_categories_info_data;
    private HashMap<String, PollutantCategoryInfo> pollutantCategoryInfo;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String[] dummyImages = {"http://maps.googleapis.com/maps/api/staticmap?center=55.945589,-3.182186&zoom=13&size=150x150&markers=55.945589,-3.182186&key=ABQIAAAALonWf75zcZF8Bu6ImssOphTSqKOsFoqTKlcvoyzYz7RM4CR3PxSvR5XcNiEFUO6LyFxkH-O9ko57oQ",
    "http://maps.googleapis.com/maps/api/staticmap?center=57.134195,-2.093968&zoom=13&size=150x150&markers=57.134195,-2.093968&key=ABQIAAAALonWf75zcZF8Bu6ImssOphTSqKOsFoqTKlcvoyzYz7RM4CR3PxSvR5XcNiEFUO6LyFxkH-O9ko57oQ",
    "http://maps.googleapis.com/maps/api/staticmap?center=55.859170,-4.258889&zoom=13&size=150x150&markers=55.859170,-4.258889&key=ABQIAAAALonWf75zcZF8Bu6ImssOphTSqKOsFoqTKlcvoyzYz7RM4CR3PxSvR5XcNiEFUO6LyFxkH-O9ko57oQ"};
    private String[] dummyCoordinates = {"55.945589", "-3.182186"};
    private String dummySource = "scottishairquality.co.uk";
    private String sourceID = "ED3";

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

        Iterator it = pollutant_thresholds_data.keySet().iterator();

        while (it.hasNext()){
            String currentCode = (String) it.next();
            String currentThresholds[] = pollutant_thresholds_data.get(currentCode);
            PollutantThreshold currentPollutantThreshold = new PollutantThreshold();
            currentPollutantThreshold.setCode(currentCode);
            currentPollutantThreshold.setGreen(currentThresholds[1]);
            currentPollutantThreshold.setYellow(currentThresholds[3]);
            currentPollutantThreshold.setRed(currentThresholds[2]);
            currentPollutantThreshold.setBlack(currentThresholds[0]);
            currentPollutantThreshold.setAboveBlack(currentThresholds[4]);
            pollutantThresholds.put(currentCode, currentPollutantThreshold);
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
            PollutantCategoryInfo PollutantCategoryInfoObj = new PollutantCategoryInfo();
            PollutantCategoryInfoObj.setCode(currentCategory);
            PollutantCategoryInfoObj.setUrl(pollutant_categories_info_data.get(currentCategory));
            pollutantCategoryInfo.put(currentCategory, PollutantCategoryInfoObj);
        }
    }

    public HashMap<String, PollutantThreshold> getPollutantThresholds(){
        return pollutantThresholds;
    }



    public HashMap<String, PollutantCategoryInfo> getPollutantCategoriesInfo(){
        return pollutantCategoryInfo;
    }

    public List<SensorReading> getSensorReadings(){
        List<SensorReading> sensorReadings = new ArrayList<>();
        //Start hour
        GregorianCalendar sensorIDLastUpdated = (GregorianCalendar) Calendar.getInstance();
        sensorIDLastUpdated.set(Calendar.HOUR, 1);
        Random r = new Random();
        for(int i = 1; i<=24; i++){
            HashMap<String, String> pollutantValues = new HashMap<>();
            Set<String> renderedPollutants = Pollutant.RENDERED_POLLUTANTS;
            Iterator it = renderedPollutants.iterator();
            while(it.hasNext()){
                String currentPollutant = (String) it.next();
                int currentPollutantValue = 0;
                int usingThreshold = r.nextInt(3)+1;
                int deviation = r.nextInt(49)+1;
                boolean sum = r.nextBoolean();
                PollutantThreshold currentPollutantThreshold = pollutantThresholds.get(currentPollutant);
                int thresholdValue = 0;
                switch(usingThreshold){
                    case 1:
                        thresholdValue = Integer.parseInt(currentPollutantThreshold.getGreen());
                        break;
                    case 2:
                        thresholdValue = Integer.parseInt(currentPollutantThreshold.getYellow());
                        break;
                    case 3:
                        thresholdValue = Integer.parseInt(currentPollutantThreshold.getRed());
                        break;
                    case 4:
                        thresholdValue = Integer.parseInt(currentPollutantThreshold.getBlack());
                        break;
                }
                if(sum){
                    currentPollutantValue = thresholdValue + deviation;
                }else{
                    currentPollutantValue = thresholdValue - deviation;
                }
                pollutantValues.put(currentPollutant, ""+currentPollutantValue);
            }
            SensorReading sensorReadingObj = new SensorReading();
            sensorReadingObj.setImage(dummyImages[r.nextInt(dummyImages.length-1)]);
            sensorReadingObj.setCoordinates(Arrays.asList(dummyCoordinates));
            sensorReadingObj.setDateInserted("");
            sensorReadingObj.setLastUpdated(dateFormatter.format(sensorIDLastUpdated.getTime()));
            sensorReadingObj.setSource(dummySource);
            sensorReadingObj.setSourceID("ED3");
            sensorReadingObj.setAirQualityIndex(""+(r.nextInt(9)+1));
            sensorReadingObj.setNo(Arrays.asList(new String[]{"0", "ugm"}));
            sensorReadingObj.setNo2(Arrays.asList(new String[]{pollutantValues.get("NO2"), "ugm"}));
            sensorReadingObj.setNox(Arrays.asList(new String[]{"0", "ugm"}));
            sensorReadingObj.setPm10(Arrays.asList(new String[]{pollutantValues.get("PM10"), "ugm"}));
            sensorReadingObj.setVpm10(Arrays.asList(new String[]{"0", "ugm"}));
            sensorReadingObj.setPm2p5(Arrays.asList(new String[]{pollutantValues.get("PM2P5"), "ugm"}));
            sensorReadingObj.setPm1(Arrays.asList(new String[]{"0", "ugm"}));
            sensorReadingObj.setNvpm2p5(Arrays.asList(new String[]{"0", "ugm"}));
            sensorReadingObj.setVpm2p5(Arrays.asList(new String[]{"0", "ugm"}));
            sensorReadingObj.setSo2(Arrays.asList(new String[]{pollutantValues.get("SO2"), "ugm"}));
            sensorReadingObj.setCo(Arrays.asList(new String[]{pollutantValues.get("CO"), "ugm"}));
            sensorReadingObj.setO3(Arrays.asList(new String[]{pollutantValues.get("O3"), "ugm"}));

            sensorReadings.add(sensorReadingObj);
            dateFormatter.format(sensorIDLastUpdated.getTime());
            sensorIDLastUpdated.set(Calendar.HOUR,sensorIDLastUpdated.get(Calendar.HOUR)+1);
        }
        return sensorReadings;
    }

}