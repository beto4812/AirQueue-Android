package com.beto4812.airqueue.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.beto4812.airqueue.aws.AWSDynamoDbManager;
import com.beto4812.airqueue.aws.AWSIdentityManager;
import com.beto4812.airqueue.ui.register.BaseDrawerActivity;

public class MainActivity extends BaseDrawerActivity {

    private static final String LOG_TAG = "AWSClientManager";

    private static AWSDynamoDbManager dynamoDbManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        fetchUserIdentity();

        this.dynamoDbManager = AWSClientManager.defaultMobileClient().getDynamoDbManager();
        new GetReadings().execute();

    }

    private void fetchUserIdentity() {
        Log.d(LOG_TAG, "fetchUserIdentity");

        AWSClientManager.defaultMobileClient()
                .getIdentityManager()
                .getUserID(new AWSIdentityManager.IdentityHandler() {

                    @Override
                    public void handleIdentityID(String identityId) {
                        Log.d(LOG_TAG, "handleIdentityID(): " + identityId);
                    }

                    @Override
                    public void handleError(Exception exception) {
                        Log.v(LOG_TAG,  Log.getStackTraceString(exception));
                    }
                });
    }

    private class GetReadings extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            dynamoDbManager.getReadingsList();
            return null;
        }
    }

}