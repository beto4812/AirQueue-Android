package com.beto4812.airqueue.ui.main.settings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.utils.Constants;
import com.gc.materialdesign.views.CheckBox;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment {

    private static final String LOG_TAG = "SettingsFragment";

    private CheckBox asthmaSuffererCheckBox;
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
        String userGender = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(Constants.KEY_USER_GENDER,"");
        String userAge = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(Constants.KEY_USER_BIRTHDAY,"");

        profilePicture = (ImageView) rootView.findViewById(R.id.image_view_profile_picture);
        editTextAge = (EditText) rootView.findViewById(R.id.editTextAge);
        radioButtonGroup = (RadioGroup) rootView.findViewById(R.id.radioButtonGroup);

        profilePicture.requestFocus();

        if(userProfilePictureUrl!="") {
            //Log.v(LOG_TAG, "onCreate() rendering profile picture");
            Picasso.with(getContext()).load(userProfilePictureUrl).into(profilePicture);
        }

        if(userGender!=""){
            if(userGender.equals("male")){
                radioButtonGroup.check(R.id.radioButtonMale);
            }else{
                radioButtonGroup.check(R.id.radioButtonFemale);
            }
            radioButtonGroup.setEnabled(false);
        }

        this.asthmaSuffererCheckBox = (CheckBox) rootView.findViewById(R.id.checkBox_asthma_sufferer);
        asthmaSuffererCheckBox.setChecked(false);
        asthmaSuffererCheckBox.refreshDrawableState();

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
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
