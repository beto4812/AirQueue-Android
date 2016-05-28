package com.beto4812.airqueue.utils;

import com.beto4812.airqueue.BuildConfig;

public class Constants {
    /**
     * Firebase locations for URLs
     */
    private static final String FIREBASE_LOCATION_USERS = "users";

    /**
     * Firebase URLs
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;

    /**
     * Database properties
     */
    public static final String FIREBASE_PROPERTY_EMAIL = "email";

    /**
     * Shared Preferences and Extras keys
     */
    public static final String KEY_USER_UID = "USER_UID";
    public static final String KEY_USER_EMAIL = "EMAIL";
    public static final String KEY_USER_NAME = "USER_NAME";
    public static final String KEY_USER_LAST_NAME = "USER_LASTNAME";

    /**
     * Constant values
     */
    public static final String VALUE_IS_TEMPORARY_PASSWORD = "isTemporaryPassword";

}
