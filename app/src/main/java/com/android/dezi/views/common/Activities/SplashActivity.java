package com.android.dezi.views.common.Activities;
/*
 *
 *
 *  * Copyright © 2016, Mobilyte Inc. and/or its affiliates. All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  *
 *  * - Redistributions of source code must retain the above copyright
 *  *    notice, this list of conditions and the following disclaimer.
 *  *
 *  * - Redistributions in binary form must reproduce the above copyright
 *  * notice, this list of conditions and the following disclaimer in the
 *  * documentation and/or other materials provided with the distribution.
 *
 * /
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.android.dezi.R;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.driver.Activities.DriverDashboard;
import com.android.dezi.views.passenger.Activities.PassangerDashboard;

/**
 * Created by Mobilyte on 4/8/2016.
 */
public class SplashActivity extends Activity {
    Context mContext;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;

        new Handler().postDelayed(new Runnable() {


              /*Showing splash screen with a timer. This will be useful when you
              want to show case your app logo / company*/


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
//                String ID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.id));
                String ID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
                Boolean isDriver = SharedPreferencesHandler.getBooleanValues(mContext,mContext.getResources().getString(R.string.pref_isdriver));
//                String ROLE = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_role));
                if (!TextUtils.isEmpty(ID) && !isDriver) {
                    Intent intent = new Intent(mContext, PassangerDashboard.class);
                    startActivity(intent);
                }
                else if(!TextUtils.isEmpty(ID) && isDriver){
                    Intent intent = new Intent(mContext, DriverDashboard.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
