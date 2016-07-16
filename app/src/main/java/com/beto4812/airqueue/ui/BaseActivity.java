package com.beto4812.airqueue.ui;

import android.content.Intent;
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
import android.view.MenuItem;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSClientManager;
import com.beto4812.airqueue.ui.login.LoginActivity;
import com.beto4812.airqueue.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BaseActivity";

    public static GoogleApiClient googleApiClient;
    public SharedPreferences sharedPreferences;

    /*User information*/
    protected String userId;
    protected String userFirstName;
    protected String userLastName;
    protected String userProfilePictureUrl;

    private AccessTokenTracker accessTokenTracker;

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
                            sharedPreferences.edit().putString("lastLatitude", String.valueOf(location.getLatitude()));
                            sharedPreferences.edit().putString("lastLongitude", String.valueOf(location.getLongitude()));
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

        userId = sharedPreferences.getString(Constants.KEY_USER_UID, "");
        userFirstName = sharedPreferences.getString(Constants.KEY_USER_FIRST_NAME, "");
        userLastName = sharedPreferences.getString(Constants.KEY_USER_LAST_NAME, "");
        userProfilePictureUrl = sharedPreferences.getString(Constants.KEY_USER_PICTURE_URL, "");

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null) {
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
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
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
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
        ButterKnife.bind(this);
        setupToolbar();
    }

    public void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void setupToolbar() {
        Log.v(LOG_TAG, "setupToolbar");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        }
    }

    @Nullable
    public Toolbar getToolbar() {
        return toolbar;
    }

    protected void logout() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();

        sharedPrefsEditor.putString(Constants.KEY_USER_UID, null).apply();
        sharedPrefsEditor.putString(Constants.KEY_USER_EMAIL, null).apply();
        sharedPrefsEditor.putString(Constants.KEY_USER_FIRST_NAME, null).apply();
        sharedPrefsEditor.putString(Constants.KEY_USER_LAST_NAME, null).apply();
        sharedPrefsEditor.putString(Constants.KEY_USER_PICTURE_URL, null).apply();
        sharedPrefsEditor.putString(Constants.KEY_USER_BIRTHDAY, null).apply();
        sharedPrefsEditor.putString(Constants.KEY_USER_GENDER, null).apply();

        LoginManager.getInstance().logOut();
    }
}
