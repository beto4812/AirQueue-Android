package com.beto4812.airqueue.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.ui.BaseActivity;
import com.beto4812.airqueue.ui.MainActivity;

//TODO: Refactor class according new parent classes
public class LoginActivity extends BaseActivity {

    private static final String LOG_TAG = "LoginActivity";

    private SharedPreferences.Editor mSharedPrefsEditor;
    private EditText editTextAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPrefsEditor = sharedPrefs.edit();
        editTextAge = (EditText)findViewById(R.id.edit_text_email);

        initializeScreen();
        if(PreferenceManager.getDefaultSharedPreferences(this).getInt("userAge", -1)!=-1) {
            login();
        }
    }

    private void login() {
        if(editTextAge.getText().toString()!=null){
            try{
                mSharedPrefsEditor.putInt("userAge", Integer.parseInt(editTextAge.getText().toString())).commit();
            }catch (Exception e){

            }
        }
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onContinuePressed(View view) {

        login();
    }

    private void initializeScreen() {
        //buttonContinue = (Button) findViewById(R.id.buttonContinue);
    }
}