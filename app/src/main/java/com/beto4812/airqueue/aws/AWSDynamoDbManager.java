package com.beto4812.airqueue.aws;


import android.util.Log;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.beto4812.airqueue.model.PollutantCategoryInfo;
import com.beto4812.airqueue.model.PollutantThreshold;
import com.beto4812.airqueue.model.SensorCoordinates;
import com.beto4812.airqueue.model.SensorReading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//This class will handle DB operations
public class AWSDynamoDbManager {

    private static final String LOG_TAG = "AWSDynamoDbManager";

    private DynamoDBMapper dynamoDBMapper;

    public AWSDynamoDbManager(AmazonDynamoDBClient dynamoDBClient) {
        this.dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
    }

    public ArrayList<SensorReading> getAllSensorReadings() {
        Log.v(LOG_TAG, "getAllSensorReadings(): ");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        try {
            PaginatedScanList<SensorReading> result = dynamoDBMapper.scan(
                    SensorReading.class, scanExpression);

            ArrayList<SensorReading> resultList = new ArrayList<>();
            for (SensorReading up : result) {
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

    /**
     *
     * @param sourceID
     * @param since yyyy-mm-dd
     * @return
     */
    public List<SensorReading> getReadingsBySourceID(String sourceID, String since, String to) {
        Log.v(LOG_TAG, "getReadingsBySourceID("+sourceID+") since: "+ since+ " to: " + to);

        SensorReading sens = new SensorReading();
        sens.setSourceID(sourceID);
        List<SensorReading> results = new ArrayList<>();
        Condition rangeKeyCondition = new Condition().
                withComparisonOperator(ComparisonOperator.BETWEEN.toString()).
                withAttributeValueList(new AttributeValue().withS(since),new AttributeValue().withS(to));

        DynamoDBQueryExpression<SensorReading> queryExpression = new DynamoDBQueryExpression().withHashKeyValues(sens).withRangeKeyCondition("lastUpdated", rangeKeyCondition);

        try{
            List<SensorReading> latestReplies = dynamoDBMapper.query(SensorReading.class, queryExpression);
            for (SensorReading up : latestReplies) {
                results.add(up);
            }

        }catch(Exception ex){
            Log.v(LOG_TAG, "AmazonServiceException");
            Log.e(LOG_TAG, Log.getStackTraceString(ex));
        }
        return results;
    }

    public SensorReading getLastSensorReadingBySourceID(String sourceID){
        Calendar c = Calendar.getInstance();
        Date from = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        Date to = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)+1);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String toS = dateFormatter.format(to);
        String fromS = dateFormatter.format(from);

        List<SensorReading> list = getReadingsBySourceID(sourceID, fromS, toS);//Should be today

        if(list.size()>1){
            return list.get(list.size()-1);
        }else{
            return null;
        }
    }

    public ArrayList<SensorCoordinates> getAllSensorCoordinates() {
        Log.v(LOG_TAG, "getAllSensorCoordinates(): ");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        try {
            PaginatedScanList<SensorCoordinates> result = dynamoDBMapper.scan(
                    SensorCoordinates.class, scanExpression);

            ArrayList<SensorCoordinates> resultList = new ArrayList<>();
            for (SensorCoordinates up : result) {
                //Log.v(LOG_TAG, up.toString());
                resultList.add(up);
            }
            return resultList;

        } catch (Exception ex) {
            Log.v(LOG_TAG, "AmazonServiceException");
            Log.e(LOG_TAG, ex.toString());
            //clientManager.wipeCredentialsOnAuthError(ex);
        }

        return null;
    }

    public SensorCoordinates getClosestSensorCoordinates(double currentLat, double currentLong){
        List<SensorCoordinates> l = getAllSensorCoordinates();

        if(l!=null){
            Iterator it = l.iterator();

            SensorCoordinates closestSensor = null, temp;

            while(it.hasNext()){
                temp = (SensorCoordinates)it.next();
                if(closestSensor==null || temp.getDistanceToCoordinates(currentLat, currentLong)<closestSensor.getDistanceToCoordinates(currentLat, currentLong)){
                    closestSensor = temp;
                }
            }
            return closestSensor;
        }
        return null;
    }

    public HashMap<String, PollutantThreshold> getPollutantThresholds(){

        Log.v(LOG_TAG, "getPollutantThresholds(): ");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        try {
            PaginatedScanList<PollutantThreshold> result = dynamoDBMapper.scan(
                    PollutantThreshold.class, scanExpression);

            HashMap<String, PollutantThreshold> resultList = new HashMap<>();
            for (PollutantThreshold up : result) {
                //Log.v(LOG_TAG, up.toString());
                resultList.put(up.getCode(), up);
            }
            return resultList;

        } catch (Exception ex) {
            Log.v(LOG_TAG, "AmazonServiceException");
            Log.e(LOG_TAG, Log.getStackTraceString(ex));
        }
        return  null;
    }

    public HashMap<String, PollutantCategoryInfo> getPollutantCategoryInfo(){
        Log.v(LOG_TAG, "getPollutantThresholds(): ");

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        try {
            PaginatedScanList<PollutantCategoryInfo> result = dynamoDBMapper.scan(
                    PollutantCategoryInfo.class, scanExpression);

            HashMap<String, PollutantCategoryInfo> resultList = new HashMap<>();
            for (PollutantCategoryInfo up : result) {
                //Log.v(LOG_TAG, up.toString());
                resultList.put(up.getCode(), up);
            }
            return resultList;

        } catch (Exception ex) {
            Log.v(LOG_TAG, "AmazonServiceException");
            Log.e(LOG_TAG, Log.getStackTraceString(ex));
        }
        return  null;
    }
}
