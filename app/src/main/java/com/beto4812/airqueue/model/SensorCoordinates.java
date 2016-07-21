package com.beto4812.airqueue.model;


import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.beto4812.airqueue.aws.AWSConstants;

import java.util.List;

@DynamoDBTable(tableName = AWSConstants.SENSOR_COORDINATES_TABLE_NAME)
/**
 * Coordinates of a sensor
 */
public class SensorCoordinates {

    private static final String LOG_TAG = "SensorReading";

    private String sourceID;
    private List<String> coordinates;

    public SensorCoordinates(String sourceID, List<String> coordinates) {
        this.sourceID = sourceID;
        this.coordinates = coordinates;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public List<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }

    public double getDistanceToCoordinates(double currentLat, double currentLong) {
        double latitude = Double.parseDouble(coordinates.get(0));
        double longitude = Double.parseDouble(coordinates.get(1));
        return Math.sqrt(Math.pow(latitude - currentLat, 2) + Math.pow(longitude - currentLong, 2));
    }

    public String toString() {
        return "SensorCoordinates{" +
                "sourceID='" + sourceID + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
