package com.beto4812.airqueue.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.ui.BaseActivity;
import com.beto4812.airqueue.ui.MainActivity;
import com.beto4812.airqueue.ui.register.CreateAccountActivity;
import com.beto4812.airqueue.utils.Constants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

//TODO: Refactor class according new parent classes
public class LoginActivity extends BaseActivity {

    private static final String LOG_TAG = "LoginActivity";

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    private SharedPreferences.Editor mSharedPrefsEditor;

    LoginButton mLoginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPrefsEditor = sharedPrefs.edit();
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        initializeScreen();

        callbackManager = CallbackManager.Factory.create();

        mLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if(response != null) {
                            try {
                                JSONObject data = response.getJSONObject();
                                String id = "";
                                String email = "";
                                String firstName = "";
                                String lastName = "";
                                String profileImageUrl = "";
                                String gender = "";
                                String birthday = "";

                                id = data.getString("id");
                                email = data.getString("email");
                                if(data.has("first_name")) {
                                    firstName = data.getString("first_name");
                                }
                                if(data.has("last_name")) {
                                    lastName = data.getString("last_name");
                                }
                                if(data.has("picture")) {
                                    profileImageUrl = "https://graph.facebook.com/" + id + "/picture?width=400&height=400";
                                }

                                if(data.has("gender")) {
                                    gender = data.getString("gender");
                                }

                                if(data.has("birthday")) {
                                    birthday = data.getString("birthday");
                                }

                                Log.i("NSAN", "Name: " + firstName);
                                Log.i("NSAN", "Last Name: " + lastName);
                                Log.i("NSAN", "Picture: " + profileImageUrl);

                                mSharedPrefsEditor.putString(Constants.KEY_USER_UID, id).apply();
                                mSharedPrefsEditor.putString(Constants.KEY_USER_EMAIL, email).apply();
                                mSharedPrefsEditor.putString(Constants.KEY_USER_FIRST_NAME, firstName).apply();
                                mSharedPrefsEditor.putString(Constants.KEY_USER_LAST_NAME, lastName).apply();
                                mSharedPrefsEditor.putString(Constants.KEY_USER_PICTURE_URL, profileImageUrl).apply();
                                mSharedPrefsEditor.putString(Constants.KEY_USER_BIRTHDAY, birthday).apply();
                                mSharedPrefsEditor.putString(Constants.KEY_USER_GENDER, gender).apply();

                                login();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, picture.type(large), gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.i("NSAN", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("NSAN", "onError");
            }
        });

        AccessToken accesToken = AccessToken.getCurrentAccessToken();

        if(accesToken != null) {
            login();
        }
    }

    private void login() {
        printLoginDetails();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void printLoginDetails(){
        Log.v(LOG_TAG, "KEY_USER_UID: " + PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_USER_UID, null));
        Log.v(LOG_TAG, "KEY_USER_EMAIL: " + PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_USER_EMAIL, null));
        Log.v(LOG_TAG, "KEY_USER_GENDER: " + PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_USER_GENDER, null));
        Log.v(LOG_TAG, "KEY_USER_BIRTHDAY: " + PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.KEY_USER_BIRTHDAY, null));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onSignInPressed(View view) {
        login();
    }

    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    private void initializeScreen() {
        mEditTextEmail = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPassword = (EditText) findViewById(R.id.edit_text_password);
        mLoginButton = (LoginButton) findViewById(R.id.loginButtonFacebook);
    }
}