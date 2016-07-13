package com.beto4812.airqueue.model;

import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.beto4812.airqueue.aws.AWSConstants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@DynamoDBTable(tableName = AWSConstants.SENSOR_READINGS_TABLE_NAME)
/**
 * Each sensor provides information of different pollutants at a given time
 */
public class SensorReading {
    private static final String LOG_TAG = "SensorReading";
    //private String sourceIDLastUpdated;
    private String image;
    private List<String> coordinates;
    private String dateInserted;
    private String lastUpdated;
    private String source;
    private String sourceID;
    private String airQualityIndex;
    private List<String> no;
    private List<String> no_2;
    private List<String> no_x;
    private List<String> pm_10;
    private List<String> v_pm_10;
    private List<String> pm_2p5;
    private List<String> pm_1;
    private List<String> nv_pm_2p5;
    private List<String> v_pm_2p5;
    private List<String> so_2;
    private List<String> co;
    private List<String> o3;
    private HashMap<String, Pollutant> pollutants = new HashMap<>();

    @DynamoDBHashKey(attributeName = "sourceID")
    public String getKey() {
        return sourceID;
    }

    public void setKey(String key){
        this.sourceID = key;
    }

    @DynamoDBAttribute(attributeName = "coordinates")
    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates){
        this.coordinates = coordinates;
    }

    public String getDateInserted() {
        return dateInserted;
    }

    public void setDateInserted(String dateInserted){
        this.dateInserted = dateInserted;
    }

    @DynamoDBRangeKey(attributeName = "lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    public int getLastUpdatedHour(){
        return Integer.parseInt(lastUpdated.split(" ")[1].split(":")[0]);
    }

    public void setLastUpdated(String lastUpdated){
        this.lastUpdated = lastUpdated;
    }

    @DynamoDBAttribute(attributeName = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source){
        this.source = source;
    }

    @DynamoDBAttribute(attributeName = "sourceID")
    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID){
        this.sourceID = sourceID;
    }

    @DynamoDBAttribute(attributeName = "no")
    public List<String> getNo() {
        return no;
    }

    @DynamoDBAttribute(attributeName = "no_2")
    public List<String> getNo2() {
        return no_2;
    }

    @DynamoDBAttribute(attributeName = "no_x")
    public List<String> getNox() {
        return no_x;
    }

    @DynamoDBAttribute(attributeName = "pm_10")
    public List<String> getPm10() {
        return pm_10;
    }

    @DynamoDBAttribute(attributeName = "v_pm_10")
    public List<String> getVpm10() {
        return v_pm_10;
    }

    @DynamoDBAttribute(attributeName = "pm_2p5")
    public List<String> getPm2p5() {
        return pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "pm_1")
    public List<String> getPm1() {
        return pm_1;
    }

    @DynamoDBAttribute(attributeName = "nv_pm_2p5")
    public List<String> getNvpm2p5() {
        return nv_pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "v_pm_2p5")
    public List<String> getVpm2p5() {
        return v_pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "so_2")
    public List<String> getSo2() {
        return so_2;
    }

    @DynamoDBAttribute(attributeName = "co")
    public List<String> getCo() {
        return co;
    }

    @DynamoDBAttribute(attributeName = "o3")
    public List<String> getO3() {
        return o3;
    }

    @DynamoDBAttribute(attributeName = "site_map")
    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    @DynamoDBAttribute(attributeName = "air_quality_index")
    public String getAirQualityIndex(){
        return airQualityIndex;
    }

    public void setAirQualityIndex(String airQualityIndex){
        this.airQualityIndex = airQualityIndex;
    }

    public Pollutant getPollutant(String key){
        Pollutant temp = pollutants.get(key);
        return temp;
    }

    public void setNo(List<String> p){
        this.pollutants.put(Pollutant.NITRIC_OXIDE,
                new Pollutant(Pollutant.NITRIC_OXIDE, p.get(0), p.get(1)));
    }


    public void setNo2(List<String> p){
        this.pollutants.put(Pollutant.NITROGEN_DIOXIDE,
                new Pollutant(Pollutant.NITROGEN_DIOXIDE, p.get(0), p.get(1)));
    }

    public void setNox(List<String> p){
        this.pollutants.put(Pollutant.OXIDES_OF_NITROGEN,
                new Pollutant(Pollutant.OXIDES_OF_NITROGEN, p.get(0), p.get(1)));
    }

    public void setPm10(List<String> p){
        this.pollutants.put(Pollutant.PARTICULATE_MATTER_10_MICROMETRE,
                new Pollutant(Pollutant.PARTICULATE_MATTER_10_MICROMETRE, p.get(0), p.get(1)));
    }

    public void setVpm10(List<String> p){
        this.pollutants.put(Pollutant.VOLATILE_PARTICULATE_MATTER_10_MICROMETRE,
                new Pollutant(Pollutant.VOLATILE_PARTICULATE_MATTER_10_MICROMETRE, p.get(0), p.get(1)));
    }


    public void setPm2p5(List<String> p){
        this.pollutants.put(Pollutant.PARTICULATE_MATTER_2_5_MICROMETRE,
                new Pollutant(Pollutant.PARTICULATE_MATTER_2_5_MICROMETRE, p.get(0), p.get(1)));
    }


    public void setPm1(List<String> p){
        this.pollutants.put(Pollutant.PARTICULATE_MATTER_1_MICROMETRE,
                new Pollutant(Pollutant.PARTICULATE_MATTER_1_MICROMETRE, p.get(0), p.get(1)));
    }


    public void setNvpm2p5(List<String> p){
        this.pollutants.put(Pollutant.NON_VOLATILE_PARTICULATE_MATTER_2_5_MICROMETRE,
                new Pollutant(Pollutant.NON_VOLATILE_PARTICULATE_MATTER_2_5_MICROMETRE, p.get(0), p.get(1)));
    }


    public void setVpm2p5(List<String> p){
        this.pollutants.put(Pollutant.VOLATILE_PARTICULATE_MATTER_2_5_MICROMETRE,
                new Pollutant(Pollutant.VOLATILE_PARTICULATE_MATTER_2_5_MICROMETRE, p.get(0), p.get(1)));
    }


    public void setSo2(List<String> p){
        this.pollutants.put(Pollutant.SULPHUR_DIOXIDE,
                new Pollutant(Pollutant.SULPHUR_DIOXIDE, p.get(0), p.get(1)));
    }


    public void setCo(List<String> p){
        this.pollutants.put(Pollutant.CARBON_MONOXIDE,
                new Pollutant(Pollutant.CARBON_MONOXIDE, p.get(0), p.get(1)));
    }

    public void setO3(List<String> p){
        this.pollutants.put(Pollutant.OZONE,
                new Pollutant(Pollutant.OZONE, p.get(0), p.get(1)));
    }

    public String getTextCoordinates() {
        return coordinates.get(0) + "," + coordinates.get(1);
    }

    public double getDistanceToCoordinates(double currentLat, double currentLong) {
        double latitude = Double.parseDouble(coordinates.get(0));
        double longitude = Double.parseDouble(coordinates.get(1));
        return Math.sqrt(Math.pow(latitude - currentLat, 2) + Math.pow(longitude - currentLong, 2));
    }


    public void printPollutants(){
        Iterator it = this.pollutants.values().iterator();
        while(it.hasNext()){
            Log.v(LOG_TAG,it.next().toString());
        }
    }

    public Collection<Pollutant> getAvailablePollutants(){
        return pollutants.values();
    }

    public Pollutant getPollutantByCode(String code){
        return pollutants.get(code);
    }

    public String toString(){
        String out = "lastUpdated: " + lastUpdated + " sourceID: " + sourceID + " pollutants: ";
        Iterator it = pollutants.keySet().iterator();
        while(it.hasNext()){
            out+=it.next()+ " ";
        }
        out += " image: " + image;
        return out;
    }
}