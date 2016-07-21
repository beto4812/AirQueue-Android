package com.beto4812.airqueue.model;

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
    private String aboveBlack;

    public PollutantThreshold(String code, String green, String black, String red, String yellow, String aboveBlack) {
        this.code = code;
        this.green = green;
        this.black = black;
        this.red = red;
        this.yellow = yellow;
        this.aboveBlack = aboveBlack;
    }

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

    public String getAboveBlack(){return aboveBlack;}

    public Double getAboveBlackDouble(){return getBlackDouble()+getBlackDouble()*.5;}

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

    public void setAboveBlack(String value){this.aboveBlack = value;}

    public String toString(){
        return " code: " + code + " green: " + green + " yellow: " + yellow + " red: " + red + " black: " + black;
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
}
