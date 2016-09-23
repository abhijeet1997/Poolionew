package com.travelwithpoolio.onesignal;

import android.app.Application;
import com.onesignal.OneSignal;


/**
 * Created by kjaganmohan on 22/07/16.
 */
public class Onesignal extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this).init();
    }
}