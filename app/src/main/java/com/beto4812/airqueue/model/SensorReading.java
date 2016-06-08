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

    @DynamoDBAttribute(attributeName = "date_inserted")
    public String getDateInserted() {
        return dateInserted;
    }

    @DynamoDBAttribute(attributeName = "lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    @DynamoDBAttribute(attributeName = "source")
    public String getSource() {
        return source;
    }

    @DynamoDBAttribute(attributeName = "sourceID")
    public String getSourceID() {
        return sourceID;
    }

    @DynamoDBAttribute(attributeName = "no")
    public String getNo() {
        return no;
    }

    @DynamoDBAttribute(attributeName = "no_2")
    public String getNo2() {
        return no_2;
    }

    @DynamoDBAttribute(attributeName = "no_x")
    public String getNo_x() {
        return no_x;
    }

    @DynamoDBAttribute(attributeName = "pm_10")
    public String getPm_10() {
        return pm_10;
    }

    @DynamoDBAttribute(attributeName = "v_pm_10")
    public String getV_pm_10() {
        return v_pm_10;
    }

    @DynamoDBAttribute(attributeName = "pm_2p5")
    public String getPm_2p5() {
        return pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "pm_1")
    public String getPm1() {
        return pm_1;
    }

    @DynamoDBAttribute(attributeName = "nv_pm_2p5")
    public String getNv_pm_2p5() {
        return nv_pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "v_pm_2p5")
    public String getV_pm_2p5() {
        return v_pm_2p5;
    }

    @DynamoDBAttribute(attributeName = "so_2")
    public String getSo_2() {
        return so_2;
    }

    @DynamoDBAttribute(attributeName = "co")
    public String getCo() {
        return co;
    }


}