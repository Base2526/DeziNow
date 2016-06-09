package com.android.dezi.views.driver.Activities;

/*
*
* * Copyright © 2016, Mobilyte Inc. and/or its affiliates. All rights reserved.
* *
* * Redistribution and use in source and binary forms, with or without
* * modification, are permitted provided that the following conditions are met:
* *
* * - Redistributions of source code must retain the above copyright
* *    notice, this list of conditions and the following disclaimer.
* *
* * - Redistributions in binary form must reproduce the above copyright
* * notice, this list of conditions and the following disclaimer in the
* * documentation and/or other materials provided with the distribution.
*
*/
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.beans.NotificationPickUpRequestBean;
import com.android.dezi.notifications.RegistrationIntentService;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.common.Activities.ReportIssue;
import com.android.dezi.views.driver.Fragments.DriverDashboardFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Mobilyte on 4/11/2016.
 */
public class DriverDashboard extends BaseActivity{
    public DrawerLayout mDrawerLayout;
    public Toolbar toolbar;
    public NotificationPickUpRequestBean beanObj;
    Context mContext;
    private RelativeLayout mParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);
        mContext = DriverDashboard.this;
        /*
        Initialize view items
         */
        initView();
        /*
        Hamburger Icon on Toolbar
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setNavigationIcon(R.drawable.ic_menu);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }

        };
//        toggle.set
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        /*
        Register for GCM
         */
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(DriverDashboard.this, RegistrationIntentService.class);
            startService(intent);
        } else {
            CommonMethods.getInstance().DisplayToast(mContext, "Play service not installed here");
        }
        // Move to Dashboard Fragment first time user came
        getNotificationData();
    }


    /*
        Initialize View on Activity
         */
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mParent = (RelativeLayout) findViewById(R.id.parent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        /*
        Set Toolbar Color for Driver Side
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackground(ContextCompat.getDrawable(mContext,R.drawable.driver_toolbar_bg));
        }
    }

    private void getNotificationData() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                beanObj = (NotificationPickUpRequestBean) bundle.getSerializable("driver_pickup");
                if (beanObj != null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("driver_pickup", beanObj);
                    navigateToWithBundle(new DriverDashboardFragment(), bundle1);
                }
            } else {
                navigateTo(new DriverDashboardFragment());
            }
        } catch (Exception e) {

        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(DriverDashboard.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(DriverDashboard.this, resultCode, 9000)
                        .show();
            } else {
                Log.i("", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /*
       Set Up Drawer Content
        */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
//                                navigateTo(new DriverDashboardFragment());
                                break;
                            case R.id.nav_profile:
                                Intent intent = new Intent(mContext, DriverProfileActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in, R.anim.no_change);
                                break;
                            case R.id.nav_earnings:
                                Intent earningIntent = new Intent(DriverDashboard.this,DriverEarningsActivity.class);
                                startActivity(earningIntent);
                                break;
                            case R.id.nav_payments:
                                Intent paymentIntent = new Intent(DriverDashboard.this,DriverPaymentActivity.class);
                                startActivity(paymentIntent);
                                break;
                            case R.id.nav_report:
                                Intent reportIntent = new Intent(DriverDashboard.this,ReportIssue.class);
                                startActivity(reportIntent);
                                break;
                            case R.id.nav_refer_driver:
                                Intent referIntent = new Intent(DriverDashboard.this,DriverReferalActivity.class);
                                startActivity(referIntent);
                                break;
                            case R.id.nav_rewards:
                                Intent rewardIntent = new Intent(DriverDashboard.this,DriverRewardsActivity.class);
                                startActivity(rewardIntent);
                                break;
                            case R.id.nav_faq:
                                Intent faqIntent = new Intent(DriverDashboard.this,DriverFaqActivity.class);
                                startActivity(faqIntent);
                                break;
                            case R.id.nav_passenger_mode:
                                ((BaseActivity) mContext).changeMode(mContext.getString(R.string.passenger_mode), "passanger");
                                break;
                        }
                        return true;
                    }
                });
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ((BaseActivity)mContext).oneStepBack();
    }
}
