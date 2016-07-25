package com.beto4812.airqueue.model;

import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
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
    private String above_black;


    public String getCode(){
        return code;
    }

    public String getGreen(){
        return green;
    }

    public Double getGreenDouble(){
        return Double.parseDouble(green);
    }

    public String getBlack(){
        return black;
    }

    public Double getBlackDouble(){
        return Double.parseDouble(black);
    }

    public String getRed(){
        return red;
    }

    public Double getRedDouble(){
        return Double.parseDouble(red);
    }

    public String getYellow(){
        return yellow;
    }

    public Double getYellowDouble(){
        return Double.parseDouble(yellow);
    }

    @DynamoDBAttribute(attributeName = "above_black")
    public String getAboveBlack(){return above_black;}

    public Double getAboveBlackDouble(){return Double.parseDouble(above_black);}

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

    public void setAboveBlack(String value){
        Log.v(LOG_TAG, "setAboveBlack: " + value);
        this.above_black = value;
    }

    public String toString(){
        return " code: " + code + " green: " + green + " yellow: " + yellow + " red: " + red + " black: " + black + " aboveBlack: " + above_black;
    }

    public double getPercentage(String color){
        switch (color){
            case "green":
                return getYellowDouble()*100/getAboveBlackDouble();
            case "yellow":
                return getRedDouble()*100/getAboveBlackDouble();
            case "red":
                return getBlackDouble()*100/getAboveBlackDouble();
            case "black":
                return getAboveBlackDouble()*100/getAboveBlackDouble();
        }
        return -1;
    }

    public PollutantThreshold(){

    }
}
