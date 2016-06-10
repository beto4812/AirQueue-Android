package com.beto4812.airqueue.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.beto4812.airqueue.aws.AWSConstants;

import java.util.List;

@DynamoDBTable(tableName = AWSConstants.TABLE_NAME)
public class SensorReading {
    private String sourceIDLastUpdated;
    private List<String> coordinates;
    private String dateInserted;
    private String lastUpdated;
    private String source;
    private String sourceID;
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

    @DynamoDBHashKey(attributeName = "sourceID_lastUpdated")
    public String getKey() {
        return sourceIDLastUpdated;
    }

    public void setKey(String key){
        this.sourceIDLastUpdated = key;
    }

    @DynamoDBAttribute(attributeName = "coordinates")
    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates){
        this.coordinates = coordinates;
    }

    @DynamoDBAttribute(attributeName = "date_inserted")
    public String getDateInserted() {
        return dateInserted;
    }

    public void setDateInserted(String dateInserted){
        this.dateInserted = dateInserted;
    }

    @DynamoDBAttribute(attributeName = "lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
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

    public void setNo(List<String> no){
        this.no = no;
    }

    @DynamoDBAttribute(attributeName = "no_2")
    public List<String> getNo2() {
        return no_2;
    }

    public void setNo2(List<String> no_2){
        this.no_2 = no_2;
    }

    @DynamoDBAttribute(attributeName = "no_x")
    public List<String> getNox() {
        return no_x;
    }

    public void setNox(List<String> no_x){
        this.no_x = no_x;
    }

    @DynamoDBAttribute(attributeName = "pm_10")
    public List<String> getPm10() {
        return pm_10;
    }

    public void setPm10(List<String> pm_10){
        this.pm_10 = pm_10;
    }

    @DynamoDBAttribute(attributeName = "v_pm_10")
    public List<String> getVpm10() {
        return v_pm_10;
    }

    public void setVpm10(List<String> v_pm_10){
        this.v_pm_10 = v_pm_10;
    }

    @DynamoDBAttribute(attributeName = "pm_2p5")
    public List<String> getPm2p5() {
        return pm_2p5;
    }

    public void setPm2p5(List<String> pm_2p5){
        this.pm_2p5 = pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "pm_1")
    public List<String> getPm1() {
        return pm_1;
    }

    public void setPm1(List<String> pm_1){
        this.pm_1 = pm_1;
    }

    @DynamoDBAttribute(attributeName = "nv_pm_2p5")
    public List<String> getNvpm2p5() {
        return nv_pm_2p5;
    }

    public void setNvpm2p5(List<String> nv_pm_2p5){
        this.nv_pm_2p5 = nv_pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "v_pm_2p5")
    public List<String> getVpm2p5() {
        return v_pm_2p5;
    }

    public void setVpm2p5(List<String> v_pm_2p5){
        this.v_pm_2p5 = v_pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "so_2")
    public List<String> getSo2() {
        return so_2;
    }

    public void setSo2(List<String> so_2){
        this.so_2 = so_2;
    }

    @DynamoDBAttribute(attributeName = "co")
    public List<String> getCo() {
        return co;
    }

    public void setCo(List<String> co){
        this.co = co;
    }


}