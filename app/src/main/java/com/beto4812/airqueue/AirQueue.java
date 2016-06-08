package com.beto4812.airqueue;

import android.app.Application;

import com.firebase.client.Firebase;
import com.firebase.client.Logger;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;


public class AirQueue extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);


    }
}