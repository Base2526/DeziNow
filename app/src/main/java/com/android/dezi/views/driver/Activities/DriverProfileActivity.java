package com.android.dezi.views.driver.Activities;
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
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.driver.Fragments.DriverProfileFragment;

/**
 * Created by Mobilyte on 4/8/2016.
 */
public class DriverProfileActivity extends BaseActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = DriverProfileActivity.this;
        modifyTolbar();
        navigateTo(new DriverProfileFragment());
    }

    /*
  Modify Appearance of Toolbar for Driver Mode
   */
    private void modifyTolbar() {
        /*
        Set Toolbar Color for Driver Side
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackground(ContextCompat.getDrawable(mContext, R.drawable.driver_toolbar_bg));
        }
        getSupportActionBar().setTitle("");
        mTitleImageview.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        oneStepBack();
    }
}