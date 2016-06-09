package com.android.dezi.views.passenger.Activities;
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
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.notifications.RegistrationIntentService;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.passenger.Fragments.DriverInfoFragment;
import com.android.dezi.views.passenger.Fragments.PassengerDashboardFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Mibilyte on 4/25/2016.
 */
public class PassangerDashboard extends BaseActivity {
    Context mContext;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
    public TextView mTitle;
    private RelativeLayout mparent;
//    NotificationAcceptRideBean beanObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_dashboard);
        mContext = com.android.dezi.views.passenger.Activities.PassangerDashboard.this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mparent = (RelativeLayout)findViewById(R.id.parent);

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


        if (mNavigationView != null) {
            setupDrawerContent(mNavigationView);
        }
        /*
        Check if passanger applied for driver or not
         */
        /*try {
            String isDriverRequested = SharedPreferencesHandler.getStringValues(mContext,mContext.getResources().getString(R.string.pref_isdriver_requested));
            if(isDriverRequested!=null && isDriverRequested.equals("0")){
                mNavigationView.getMenu().findItem(R.id.nav_driver_mode).setVisible(false);
            }
            else if(isDriverRequested!=null && isDriverRequested.equals("1")){
                mNavigationView.getMenu().findItem(R.id.nav_driver_mode).setVisible(true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }*/

        /*
        Hamburger Icon on Toolbar
         */

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
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
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

         /*
        Register for GCM
         */
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(PassangerDashboard.this, RegistrationIntentService.class);
            startService(intent);
        } else {
            CommonMethods.getInstance().DisplayToast(mContext, "Play service not installed here");
        }
        // Move to Dashboard Fragment first time user came
        getNotificationData();

    }

    private void getNotificationData() {
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                    navigateToWithBundle(new DriverInfoFragment(), bundle);
            } else {
                navigateTo(new PassengerDashboardFragment());   // Default Screen for Passanger
            }
        } catch (Exception e) {

        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(PassangerDashboard.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(PassangerDashboard.this, resultCode, 9000)
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
//                                navigateTo(new PassengerDashboardFragment());
                                break;
                            case R.id.nav_profile:
                                Intent profiletIntent = new Intent(PassangerDashboard.this,PassengerProfile.class);
                                startActivity(profiletIntent);
//                                navigateTo(new PassengerProfileFragment());
                                break;
                            case R.id.nav_favourite_place:
                                Intent placestIntent = new Intent(PassangerDashboard.this,FavoritePlacesActivity.class);
                                startActivity(placestIntent);
                                break;
                            case R.id.nav_payments:
                                Intent paymentIntent = new Intent(PassangerDashboard.this,PassangerPaymentActivity.class);
                                startActivity(paymentIntent);
                                break;
                            case R.id.nav_report:
                                Intent reportIntent = new Intent(PassangerDashboard.this,ReportIssueActivity.class);
                                startActivity(reportIntent);
                                break;
                            case R.id.nav_cars:
                                Intent carIntent = new Intent(PassangerDashboard.this,CarsActivity.class);
                                startActivity(carIntent);
                                break;
                            case R.id.nav_history:
                                Intent historyIntent = new Intent(PassangerDashboard.this,TripHistoryActivity.class);
                                startActivity(historyIntent);
                                break;
                            case R.id.nav_refer_history:
                                Intent referIntent = new Intent(PassangerDashboard.this,ReferralHistoryActivity.class);
                                startActivity(referIntent);
                                break;
                            case R.id.nav_driver_mode:
                                // Check if he is approved then navigate to driver screen
                                String isApproved = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_isdriver_approved));
                                if (isApproved != null && isApproved.equals("0")) {
                                    CommonMethods.getInstance().displaySnackBar(mContext, "You are not Approved yet", mparent);
                                } else if (isApproved != null && isApproved.equals("1")) {
                                    ((BaseActivity)mContext).changeMode(mContext.getString(R.string.driver_mode),"driver");
                                }
                                break;
                            case R.id.nav_about:
                                Intent aboutIntent = new Intent(PassangerDashboard.this,AboutUsPassengerActivity.class);
                                startActivity(aboutIntent);
                                break;
                        }
                        return true;
                    }

                });
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        oneStepBack();
    }
}
