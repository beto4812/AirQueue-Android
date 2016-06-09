package com.beto4812.airqueue.aws;


import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.beto4812.airqueue.model.SensorReading;

import java.util.ArrayList;

//This class will handle DB operations
public class AWSDynamoDbManager {

    private static final String LOG_TAG = "AWSDynamoDbManager";

    private DynamoDBMapper dynamoDBMapper;

    public AWSDynamoDbManager(AmazonDynamoDBClient dynamoDBClient) {
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
    }

    public ArrayList<SensorReading> getReadingsList() {
        Log.v(LOG_TAG, "getReadingsList(): ");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        Log.v(LOG_TAG, "getReadingsList(): 1");
        try {
            PaginatedScanList<SensorReading> result = dynamoDBMapper.scan(
                    SensorReading.class, scanExpression);
            Log.v(LOG_TAG, "getReadingsList(): 2");

            ArrayList<SensorReading> resultList = new ArrayList<>();
            Log.v(LOG_TAG, "getReadingsList(): 3");
            for (SensorReading up : result) {
                Log.v(LOG_TAG, "result: " + up.getSourceID());
                resultList.add(up);
            }
            return resultList;

        } catch (Exception ex) {
            Log.v(LOG_TAG, "AmazonServiceException");
            Log.e(LOG_TAG, Log.getStackTraceString(ex));
            //clientManager.wipeCredentialsOnAuthError(ex);
        }

        return null;
    }
}
