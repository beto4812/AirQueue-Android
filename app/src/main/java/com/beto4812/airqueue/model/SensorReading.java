package com.beto4812.airqueue.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.beto4812.airqueue.aws.AWSConstants;

@DynamoDBTable(tableName = AWSConstants.TABLE_NAME)
public class SensorReading {
    private String sourceIDLastUpdated;
    private String coordinates;
    private String dateInserted;
    private String lastUpdated;
    private String source;
    private String sourceID;
    private String no;
    private String no_2;
    private String no_x;
    private String pm_10;
    private String v_pm_10;
    private String pm_2p5;
    private String pm_1;
    private String nv_pm_2p5;
    private String v_pm_2p5;
    private String so_2;
    private String co;

    @DynamoDBHashKey(attributeName = "sourceID_lastUpdated")
    public String getKey() {
        return sourceIDLastUpdated;
    }

    @DynamoDBAttribute(attributeName = "coordinates")
    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates){
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
    public String getNo() {
        return no;
    }

    public void setNo(String no){
        this.no = no;
    }

    @DynamoDBAttribute(attributeName = "no_2")
    public String getNo2() {
        return no_2;
    }

    public void setNo_2(String no_2){
        this.no_2 = no_2;
    }

    @DynamoDBAttribute(attributeName = "no_x")
    public String getNo_x() {
        return no_x;
    }

    public void setNo_x(String no_x){
        this.no_x = no_x;
    }

    @DynamoDBAttribute(attributeName = "pm_10")
    public String getPm_10() {
        return pm_10;
    }

    public void setPm_10(String pm_10){
        this.pm_10 = pm_10;
    }

    @DynamoDBAttribute(attributeName = "v_pm_10")
    public String getV_pm_10() {
        return v_pm_10;
    }

    public void setV_pm_10(String v_pm_10){
        this.v_pm_10 = v_pm_10;
    }

    @DynamoDBAttribute(attributeName = "pm_2p5")
    public String getPm_2p5() {
        return pm_2p5;
    }

    public void setPm_2p5(String pm_2p5){
        this.pm_2p5 = pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "pm_1")
    public String getPm1() {
        return pm_1;
    }

    public void setPm_1(String pm_1){
        this.pm_1 = pm_1;
    }

    @DynamoDBAttribute(attributeName = "nv_pm_2p5")
    public String getNv_pm_2p5() {
        return nv_pm_2p5;
    }

    public void setNv_pm_2p5(String nv_pm_2p5){
        this.nv_pm_2p5 = nv_pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "v_pm_2p5")
    public String getV_pm_2p5() {
        return v_pm_2p5;
    }

    public void setV_pm_2p5(String v_pm_2p5){
        this.v_pm_2p5 = v_pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "so_2")
    public String getSo_2() {
        return so_2;
    }

    public void setSo_2(String so_2){
        this.so_2 = so_2;
    }

    @DynamoDBAttribute(attributeName = "co")
    public String getCo() {
        return co;
    }

    public void setCo(String co){
        this.co = co;
    }


}