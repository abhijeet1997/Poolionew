package com.travelwithpoolio;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lenovo on 11-06-2016.
 */
public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    // shared pref mode


    // Shared preferences file name
    private static final String PREF_NAME = "Poolio-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean b) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, b);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
