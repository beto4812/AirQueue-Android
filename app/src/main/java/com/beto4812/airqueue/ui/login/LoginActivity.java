package com.beto4812.airqueue.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.User;
import com.beto4812.airqueue.ui.BaseActivity;
import com.beto4812.airqueue.ui.MainActivity;
import com.beto4812.airqueue.ui.register.CreateAccountActivity;
import com.beto4812.airqueue.utils.Constants;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoginActivity extends BaseActivity {

    private ProgressDialog mAuthProgressDialog;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mSharedPrefsEditor;

    private Firebase mFirebase;
    private Firebase.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        initializeScreen();

        mEditTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN) {
                    signInPassword();
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.dismiss();

                if(authData != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mFirebaseRef.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseRef.removeAuthStateListener(mAuthStateListener);
    }

    public void onSignInPressed(View view) {
        signInPassword();
    }

    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    private void initializeScreen() {
        mEditTextEmail = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPassword = (EditText) findViewById(R.id.edit_text_password);

        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_title_please_wait));
        mAuthProgressDialog.setMessage(getString(R.string.progress_message_loading));
        mAuthProgressDialog.setCancelable(false);
    }

    public void signInPassword() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        /*String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();

        if(email.matches("")){
            mEditTextEmail.setError(getString(R.string.error_field_required));
            return;
        }

        if(password.matches("")) {
            mEditTextPassword.setError(getString(R.string.error_field_required));
            return;
        }

        mAuthProgressDialog.show();
        mFirebaseRef.authWithPassword(email, password, new LoginAuthResultHandler());*/
    }

    private class LoginAuthResultHandler implements Firebase.AuthResultHandler {

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.dismiss();

            if(authData != null) {
                final String userUid = authData.getUid();
                final boolean isTemporaryPassword = (Boolean) authData.getProviderData().get(Constants.VALUE_IS_TEMPORARY_PASSWORD);

                Log.i("NSAN", "Is temporary password: " + isTemporaryPassword);
                // TODO: 28/05/2016 if isTemporaryPassword -> Send to ResetPasswordActivity

                Firebase userRef = new Firebase(Constants.FIREBASE_URL_USERS).child(userUid);

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        mSharedPrefsEditor = mSharedPrefs.edit();

                        mSharedPrefsEditor.putString(Constants.KEY_USER_UID, userUid);
                        mSharedPrefsEditor.putString(Constants.KEY_USER_EMAIL, user.getEmail());
                        mSharedPrefsEditor.putString(Constants.KEY_USER_NAME, user.getName());
                        mSharedPrefsEditor.putString(Constants.KEY_USER_LAST_NAME, user.getLastName());

                        mSharedPrefsEditor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.dismiss();

            switch (firebaseError.getCode()) {
                case FirebaseError.INVALID_EMAIL:
                    mEditTextEmail.setError(getString(R.string.error_invalid_email));
                    break;
                case FirebaseError.USER_DOES_NOT_EXIST:
                    mEditTextEmail.setError(getString(R.string.error_email_not_registered));
                    break;
                case FirebaseError.INVALID_PASSWORD:
                    mEditTextPassword.setError(getString(R.string.error_incorrect_password));
                    break;
                case FirebaseError.NETWORK_ERROR:
                    Toast.makeText(LoginActivity.this, getString(R.string.error_newtork_error), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(LoginActivity.this, getString(R.string.error_default_error), Toast.LENGTH_LONG).show();
            }
        }
    }
}