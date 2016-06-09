package com.beto4812.airqueue.aws;


import android.content.Context;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class AWSClientManager {

    private static final String LOG_TAG = "AWSClientManager";

    private AmazonDynamoDBClient dynamoDBClient = null;
    private AWSIdentityManager identityManager;
    private DynamoDBMapper dynamoDBMapper;
    private ClientConfiguration clientConfiguration;
    private Context context;

    public AWSClientManager(Context context) {
        this.context = context;
        this.clientConfiguration = new ClientConfiguration();
        this.identityManager = new AWSIdentityManager(context, clientConfiguration);
        this.dynamoDBClient = new AmazonDynamoDBClient(identityManager.getCredentialsProvider(), clientConfiguration);
        initClients();
    }

    public AmazonDynamoDBClient getDynamoDBClient() {
        Log.v(LOG_TAG, "ddb() called");
        return dynamoDBClient;
    }

    private void initClients() {
        identityManager.getUserID(new AWSIdentityManager.IdentityHandler() {
            @Override
            public void handleIdentityID(String identityId) {
                Log.d(LOG_TAG, "handleIdentityID(): " + identityId);
            }
            @Override
            public void handleError(Exception exception) {
                Log.d(LOG_TAG, "initClients(): " + Log.getStackTraceString(exception));
            }
        });
    }
}