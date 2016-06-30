package com.beto4812.airqueue.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.beto4812.airqueue.aws.AWSConstants;

@DynamoDBTable(tableName = AWSConstants.POLLUTANT_THESHOLD_TABLE_NAME)
public class PollutantThreshold {

    private static final String LOG_TAG = "PollutantThreshold";

    private String code;
    private String green;
    private String black;
    private String red;
    private String yellow;

    public String getCode(){
        return code;
    }

    public String getGreen(){
        return green;
    }

    public String getBlack(){
        return black;
    }

    public String getRed(){
        return red;
    }

    public String getYellow(){
        return yellow;
    }

    public void setCode(String code){
        this.code = code;
    }

    public void setGreen(String value){
        this.green = value;
    }

    public void setBlack(String value){
        this.black = value;
    }

    public void setRed(String value){
        this.red = value;
    }

    public void setYellow(String value){
        this.yellow = value;
    }

    public String toString(){
        return " code: " + code + " green: " + green + " yellow: " + yellow + " red: " + red + " black: " + black;
    }

}
