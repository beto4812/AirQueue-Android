package com.beto4812.airqueue.ui;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class BaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BaseActivity";

    public static GoogleApiClient googleApiClient;
    public SharedPreferences sharedPreferences;

    @Nullable
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "---------5");

        AWSClientManager.initializeMobileClientIfNecessary(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    //.addConnectionCallbacks(this)
                    //.addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        googleApiClient.registerConnectionCallbacks(
            new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    Log.v(LOG_TAG, "onConnected");
                    try{
                        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        if (location != null) {
                            sharedPreferences.edit().putString("lastLatitude", String.valueOf(location.getLatitude())).commit();
                            sharedPreferences.edit().putString("lastLongitude", String.valueOf(location.getLongitude())).commit();
                            Log.v(LOG_TAG, "lastLocation: " + String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude()));
                        }else{
                            Log.v(LOG_TAG, "null location");
                        }
                    }catch (SecurityException e){
                        Log.v(LOG_TAG, Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onConnectionSuspended(int i) {

                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    protected void bindViews() {
        setupToolbar();
    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void setupToolbar() {
        //Log.v(LOG_TAG, "setupToolbar");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        }
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }
}
