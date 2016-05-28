package com.beto4812.airqueue.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.beto4812.airqueue.ui.login.LoginActivity;
import com.beto4812.airqueue.ui.register.CreateAccountActivity;
import com.beto4812.airqueue.utils.Constants;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class BaseActivity extends AppCompatActivity {

    /*Firebase*/
    protected Firebase mFirebaseRef;
    protected Firebase.AuthStateListener mAuthStateListener;
    /*User information*/
    protected String userUid;
    protected String userEmail;
    protected String userName;
    protected String userLastName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);

        userUid = sharedPreferences.getString(Constants.KEY_USER_UID, null);
        userEmail = sharedPreferences.getString(Constants.KEY_USER_EMAIL, null);
        userName = sharedPreferences.getString(Constants.KEY_USER_NAME, null);
        userLastName = sharedPreferences.getString(Constants.KEY_USER_LAST_NAME, null);

        if(!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
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

                        onUnauth();
                    }
                }
            };
            mFirebaseRef.addAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
            mFirebaseRef.removeAuthStateListener(mAuthStateListener);
        }
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
}