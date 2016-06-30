package com.beto4812.airqueue.utils;

import android.support.v4.content.ContextCompat;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.beto4812.airqueue.R;

public class Constants {
    /**
     * Shared Preferences and Extras keys
     */
    public static final String KEY_USER_UID = "USER_UID";
    public static final String KEY_USER_EMAIL = "EMAIL";
    public static final String KEY_USER_FIRST_NAME = "USER_FIRST_NAME";
    public static final String KEY_USER_LAST_NAME = "USER_LAST_NAME";
    public static final String KEY_USER_PICTURE_URL = "USER_PICTURE_URL";
    public static final String KEY_USER_GENDER = "GENDER";
    public static final String KEY_USER_BIRTHDAY = "BIRTHDAY";

    public static final String KEY_USER_SENSIBILITY = "SENSIBILITY";
    //public static final String KEY_USER_SENSIBILITY = "SENSIBILITY";

    /**
     * Constant values
     */
    public static final String VALUE_IS_TEMPORARY_PASSWORD = "isTemporaryPassword";

    /**
     *
     * @param level 1, 2, 3.
     * @param progressBar
     */
    public static void setLevel(int level, RoundCornerProgressBar progressBar){
        switch (level){
            case 1:
                progressBar.setProgress(3.3f);
                progressBar.setProgressColor(ContextCompat.getColor(progressBar.getContext(), R.color.green_traffic_light));
                break;
            case 2:
                progressBar.setProgress(6.6f);
                progressBar.setProgressColor(ContextCompat.getColor(progressBar.getContext(), R.color.yellow_traffic_light));
                break;
            case 3:
                progressBar.setProgress(10f);
                progressBar.setProgressColor(ContextCompat.getColor(progressBar.getContext(), R.color.red_traffic_light));
                break;
        }
    }
}
