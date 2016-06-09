package com.android.dezi;
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
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.twitter.sdk.android.core.TwitterAuthConfig;


/**
 * Created by Mobilyte on 2/18/2016.
 */
public class GlobalActivity extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        if(GlobalActivity.context == null){
            GlobalActivity.context = getApplicationContext();
        }
        FacebookSdk.sdkInitialize(getApplicationContext()); //Initialize Facebook SDK
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized Context getGlobalContext(){
        return GlobalActivity.context;
    }
}
