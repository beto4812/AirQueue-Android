package com.beto4812.airqueue.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.beto4812.airqueue.ui.login.LoginActivity;
import com.beto4812.airqueue.utils.Constants;
import com.firebase.client.Firebase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {

    private static final String LOG_TAG = "BaseActivity";
    /*Firebase*/
    protected Firebase mFirebaseRef;
    protected Firebase.AuthStateListener mAuthStateListener;
    /*User information*/
    protected String userUid;
    protected String userEmail;
    protected String userName;
    protected String userLastName;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "---------7");

        AWSClientManager.initializeMobileClientIfNecessary(getApplicationContext());

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);

        userUid = sharedPreferences.getString(Constants.KEY_USER_UID, null);
        userEmail = sharedPreferences.getString(Constants.KEY_USER_EMAIL, null);
        userName = sharedPreferences.getString(Constants.KEY_USER_NAME, null);
        userLastName = sharedPreferences.getString(Constants.KEY_USER_LAST_NAME, null);

        /*if(!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
            mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
            mAuthStateListener = new Firebase.AuthStateListener() {
                @Override
                public void onAuthStateChanged(AuthData authData) {
                    if(authData == null) {
                        SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();
                        sharedPrefsEditor.putString(Constants.KEY_USER_UID, null);
                        sharedPrefsEditor.putString(Constants.KEY_USER_EMAIL, null);
                        sharedPrefsEditor.putString(Constants.KEY_USER_NAME, null);
                        sharedPrefsEditor.putString(Constants.KEY_USER_LAST_NAME, null);
                        sharedPrefsEditor.apply();

                        //onUnauth();
                    }
                }
            };
            mFirebaseRef.addAuthStateListener(mAuthStateListener);
        }*/
    }


    protected void logout() {
        mFirebaseRef.unauth();
    }

    private void onUnauth() {
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if (!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
            mFirebaseRef.removeAuthStateListener(mAuthStateListener);
        }*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

}