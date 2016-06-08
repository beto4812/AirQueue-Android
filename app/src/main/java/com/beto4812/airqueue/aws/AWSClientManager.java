package com.beto4812.airqueue.aws;


import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

public class AWSClientManager {

    private static final String LOG_TAG = "AmazonClientManager";

    private AmazonDynamoDBClient ddb = null;
    private Context context;

    public AWSClientManager(Context context) {
        this.context = context;
    }

    public AmazonDynamoDBClient ddb() {
        Log.v(LOG_TAG, "ddb() called");
        validateCredentials();
        return ddb;
    }

    public void validateCredentials() {
        if (ddb == null) {
            initClients();
        }
    }

    private void initClients() {
        CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
                context,
                AWSConstants.IDENTITY_POOL_ID,
                Regions.US_EAST_1);

        ddb = new AmazonDynamoDBClient(credentials);
        ddb.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
    }

    public boolean wipeCredentialsOnAuthError(AmazonServiceException ex) {
        Log.e(LOG_TAG, "Error, wipeCredentialsOnAuthError called" + ex);
        if (
            // STS
            // http://docs.amazonwebservices.com/STS/latest/APIReference/CommonErrors.html
                ex.getErrorCode().equals("IncompleteSignature")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InvalidClientTokenId")
                        || ex.getErrorCode().equals("OptInRequired")
                        || ex.getErrorCode().equals("RequestExpired")
                        || ex.getErrorCode().equals("ServiceUnavailable")

                        // DynamoDB
                        // http://docs.amazonwebservices.com/amazondynamodb/latest/developerguide/ErrorHandling.html#APIErrorTypes
                        || ex.getErrorCode().equals("AccessDeniedException")
                        || ex.getErrorCode().equals("IncompleteSignatureException")
                        || ex.getErrorCode().equals(
                        "MissingAuthenticationTokenException")
                        || ex.getErrorCode().equals("ValidationException")
                        || ex.getErrorCode().equals("InternalFailure")
                        || ex.getErrorCode().equals("InternalServerError")) {

            return true;
        }

        return false;
    }
}