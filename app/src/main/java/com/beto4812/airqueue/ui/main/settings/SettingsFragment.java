package com.beto4812.airqueue.ui.main.settings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.utils.Constants;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment {

    private static final String LOG_TAG = "SettingsFragment";

    private ImageView profilePicture;
    private EditText editTextAge;
    private RadioGroup radioButtonGroup;
    private FloatingActionButton fab;

    private View rootView;

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_settings, container, false);

        String userProfilePictureUrl = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(Constants.KEY_USER_PICTURE_URL,"");

        profilePicture = (ImageView) rootView.findViewById(R.id.image_view_profile_picture);
        editTextAge = (EditText) rootView.findViewById(R.id.editTextAge);
        radioButtonGroup = (RadioGroup) rootView.findViewById(R.id.radioButtonGroup);

        profilePicture.requestFocus();

        if(userProfilePictureUrl!="") {
            Picasso.with(getContext()).load(userProfilePictureUrl).into(profilePicture);
        }

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                try{
                    int age = Integer.parseInt(editTextAge.getText().toString());
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt("userAge", age).commit();
                }catch (Exception e){

                }
                Snackbar.make(rootView, "Settings saved", Snackbar.LENGTH_SHORT).show();
            }
        });

        return  rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
