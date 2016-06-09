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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dezi.api.APIHandler;
import com.android.dezi.api.APIResponseInterface;
import com.android.dezi.beans.PickupRequestResponse;
import com.android.dezi.beans.UpdatePositionResponse;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.ConstantFile;
import com.android.dezi.utility.MobileConnectivity;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.common.Activities.LoginActivity;
import com.android.dezi.views.driver.Activities.DriverDashboard;
import com.android.dezi.views.passenger.Activities.PassangerDashboard;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by Mobilyte on 2/18/2016.
 */
public class BaseActivity extends AppCompatActivity implements APIResponseInterface {
    private static final double EARTHRADIUS = 6366198;
    public DrawerLayout mDrawerLayout;
    public Menu menu;
    public MenuItem mShareMenuItem, mToggleMenuItem;
    public Toolbar toolbar;
    public TextView mTitle;
    public ProgressDialog mProgressDialog;
    public ImageView mTitleImageview;
    Context mContext;
    private Boolean isDriverMode = false;

    /**
     * Create a new LatLng which lies toNorth meters north and toEast meters
     * east of startLL
     */
    private static LatLng move(LatLng startLL, double toNorth, double toEast) {
        double lonDiff = meterToLongitude(toEast, startLL.latitude);
        double latDiff = meterToLatitude(toNorth);
        return new LatLng(startLL.latitude + latDiff, startLL.longitude
                + lonDiff);
    }

    private static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * EARTHRADIUS;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }

    private static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / EARTHRADIUS;
        return Math.toDegrees(rad);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext = BaseActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.title);
        mTitleImageview = (ImageView) findViewById(R.id.title_image);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayUseLogoEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
            boolean isDriver = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriver));
            if (isDriver) {
                toolbar.setNavigationIcon(R.drawable.ic_back_black);
            } else
                toolbar.setNavigationIcon(R.drawable.ic_back);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneStepBack();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (menu != null)
            manageMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        this.menu = menu;
        manageMenu(menu);
        return true;
    }

    private void manageMenu(Menu menu) {
        boolean isDriverMode = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriver));
        boolean isDriverActive = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriverActive));

        mShareMenuItem = menu.findItem(R.id.action_share);
        mToggleMenuItem = menu.findItem(R.id.action_toggle);
        if (isDriverMode) {
            //Driver mode
            mToggleMenuItem.setVisible(true);
            mShareMenuItem.setVisible(false);
            // Check if Driver is Active or not
            if (isDriverActive) {
                // Change Toggle icon green
                mToggleMenuItem.setIcon(ContextCompat.getDrawable(mContext, R.drawable.toggle_green));
            } else {
                //Change Toggle icon gray
                mToggleMenuItem.setIcon(ContextCompat.getDrawable(mContext, R.drawable.toggle_white));
            }
        } else {
            //Passanger Mode
            mToggleMenuItem.setVisible(false);
            mShareMenuItem.setVisible(true);

        }
    }

    /* @Override
     public boolean onPrepareOptionsMenu(Menu menu) {
         boolean isDriverMode = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriver));
         boolean isDriverActive = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriverActive));

         mShareMenuItem = menu.findItem(R.id.action_share);
         mToggleMenuItem = menu.findItem(R.id.action_toggle);
         if(isDriverMode){
             //Driver mode
             mToggleMenuItem.setVisible(true);
             mShareMenuItem.setVisible(false);
             // Check if Driver is Active or not
             if(isDriverActive){
                 // Change Toggle icon green
                 mToggleMenuItem.setIcon(ContextCompat.getDrawable(mContext,R.drawable.toggle_green));
             }
             else{
                 //Change Toggle icon gray
                 mToggleMenuItem.setIcon(ContextCompat.getDrawable(mContext, R.drawable.toggle_white));
             }
         }
         else{
             //Passanger Mode
             mToggleMenuItem.setVisible(false);
             mShareMenuItem.setVisible(true);

         }
        return super.onPrepareOptionsMenu(menu);
     }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            //Shaer Action perform
        } else if (item.getItemId() == R.id.action_toggle) {
            toggleDriverProfile();
        }
        return true;

    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction fts = ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction();
        fts.add(R.id.fragmentHolder, fragment);
        fts.addToBackStack(fragment.getClass().getSimpleName());
        fts.commit();
    }

    public void addFragmentWithBundle(Fragment fragment, Bundle bundle) {
        FragmentTransaction fts = ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        fts.add(R.id.fragmentHolder, fragment);
        fts.addToBackStack(fragment.getClass().getSimpleName());
        fts.commit();
    }

    public void navigateTo(Fragment fragment) {
        FragmentTransaction fts = ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction();
        fts.replace(R.id.fragmentHolder, fragment);
        fts.addToBackStack(fragment.getClass().getSimpleName());
        fts.commit();
    }

    public void navigateToWithBundle(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction fts = ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction();
        fts.replace(R.id.fragmentHolder, fragment);
        fts.addToBackStack(fragment.getClass().getSimpleName());
        fts.commit();
    }

    public void navigateReplacingCurrent(Fragment currentFragment, Fragment fragmentToNavigate) {
        FragmentTransaction fts = ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction();
        ((BaseActivity) mContext).getSupportFragmentManager().popBackStack();
        fts.replace(R.id.fragmentHolder, fragmentToNavigate);
        fts.addToBackStack(fragmentToNavigate.getClass().getSimpleName());
        fts.remove(currentFragment).commit();
    }

    public void navigateReplacingCurrentWithBundle(Fragment currentFragment, Fragment fragmentToNavigate, Bundle bundle) {
        fragmentToNavigate.setArguments(bundle);
        FragmentTransaction fts = ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction();
        ((BaseActivity) mContext).getSupportFragmentManager().popBackStack();
        fts.replace(R.id.fragmentHolder, fragmentToNavigate);
        fts.addToBackStack(fragmentToNavigate.getClass().getSimpleName());
        fts.remove(currentFragment).commit();
    }

    public void oneStepBack() {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        FragmentManager fragmentManager = getSupportFragmentManager();
//        CommonMethods.getInstance().DisplayToast(mContext,"Count-> " + backStackCount);
        if (fragmentManager.getBackStackEntryCount() >= 2) {
            fragmentManager.popBackStackImmediate();
            fts.commit();
        } else {
            (((BaseActivity) mContext)).finish();
        }
    }

    public void removeFromBackStack(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        manager.popBackStack();
    }

    public void hideNavigationButton() {
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    public void showNavigationButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    /*
    Logout User
     */
    public void logout() {
        try {
            String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
            if (userID == null || userID.equalsIgnoreCase("")) {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                return;
            }
            showProgressDialog("Logging out..");
            Map<String, String> param = new HashMap<>();
            param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
            param.put("user_id", userID);
            CommonMethods.getInstance().e("Logout PARAMS", "Param-> " + param.toString());
            APIHandler.getInstance(ConstantFile.APIURL).logoutUser(param, this);
        } catch (Exception e) {

        }

    }

    /*
    Toggle Driver Profile
     */
    public void toggleDriverProfile() {
        String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
        boolean isDriverActive = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriverActive));
        if (isDriverActive) {
showMessage("INACTIVE");
//            ((BaseActivity) mContext).showProgressDialog("Driver going ofline");
            if (MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive()) {
                if (userID == null || userID.equalsIgnoreCase("")) {
                    CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                    return;
                }
//                ((BaseActivity) mContext).showProgressDialog("Driver going Offline");
                Map<String, String> param = new HashMap<>();
                param.put("token", ConstantFile.DEF_TOKEN);
                param.put("user_id", userID);
                param.put("active", "0");
                APIHandler.getInstance(ConstantFile.APIURL).toggle_Profile(param, this);
            } else {
//                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_internet), mParent);
            }


        } else if (!isDriverActive) {
            if (MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive()) {
                if (userID == null || userID.equalsIgnoreCase("")) {
                    CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                    return;
                }
                showMessage("ACTIVE");

//                ((BaseActivity) mContext).showProgressDialog("Driver going Online");
                Map<String, String> param = new HashMap<>();
                param.put("token", ConstantFile.DEF_TOKEN);
                param.put("user_id", userID);
                param.put("active", "1");
                APIHandler.getInstance(ConstantFile.APIURL).toggle_Profile(param, this);
            } else {
//                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_internet), mParent);
            }
        }
    }

    /*
    Change Mode
     */
    public void changeMode(String msg, final String mode) {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    showProgressDialog("Changing Mode..");
                    if (mode.equalsIgnoreCase("driver")) {
                        isDriverMode = true;
                    } else {
                        isDriverMode = false;
                    }
                    String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
                    if (userID == null || userID.equalsIgnoreCase("")) {
                        CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                        hideProgressDialog();
                        return;
                    }
                    Map<String, String> param = new HashMap<>();
                    param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
                    param.put("user_id", userID);
                    param.put("mode", mode);
                    CommonMethods.getInstance().e("Change Mode PARAMS", "Param-> " + param.toString());
                    APIHandler.getInstance(ConstantFile.APIURL).changeMode(param, BaseActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    /*
    show dialog box
    */
    private void showMessage(String msg) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(mContext);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);
        // set values for custom dialog components - text, image and button
        dialog.show();
        TextView errorMessage = (TextView) dialog.findViewById(R.id.error_message);
        Button okButton = (Button) dialog.findViewById(R.id.ok_btn);
        okButton.setVisibility(View.GONE);
        //set error message
        errorMessage.setText(msg);
        dialog.dismiss();
        Handler mHandler = new Handler();
        Runnable mRunnable = new Runnable () {

            public void run() {
                if(dialog != null && dialog.isShowing()) dialog.dismiss();
            }
        };
        mHandler.postDelayed(mRunnable,2000);
    }

    /*
    Forcefully Change Mode
     */
    public void forcefullyChangeMode(String msg, final String mode) {
        try {
            showProgressDialog("Changing Mode..");
            if (mode.equalsIgnoreCase("driver")) {
                isDriverMode = true;
            } else {
                isDriverMode = false;
            }
            String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
            if (userID == null || userID.equalsIgnoreCase("")) {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                hideProgressDialog();
                return;
            }
            Map<String, String> param = new HashMap<>();
            param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
            param.put("user_id", userID);
            param.put("mode", mode);
            CommonMethods.getInstance().e("Change Mode PARAMS", "Param-> " + param.toString());
            APIHandler.getInstance(ConstantFile.APIURL).changeMode(param, BaseActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    update Driver Location
     */
    public void updateDriverLocation(Location location) {
        if (location != null) {
            CommonMethods.getInstance().DisplayToast(mContext, "Driver location changed from previous location");
            if (MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive()) {
                String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
                if (userID == null || userID.equalsIgnoreCase("")) {
                    CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                    return;
                }
                Map<String, String> param = new HashMap<>();
                param.put("token", ConstantFile.DEF_TOKEN);
                param.put("user_id", userID);
                param.put("latitude", location.getLatitude() + "");
                param.put("longitude", location.getLongitude() + "");
                APIHandler.getInstance(ConstantFile.APIURL).updateDriverPosition(param, this);
            } else {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_internet));
            }
        }
    }

    /*
    For Marker bound on Map
     */
    public LatLngBounds createBoundsWithMinDiagonal(MarkerOptions firstMarker, MarkerOptions secondMarker) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(firstMarker.getPosition());
        builder.include(secondMarker.getPosition());

        LatLngBounds tmpBounds = builder.build();
        /** Add 2 points 1000m northEast and southWest of the center.
         * They increase the bounds only, if they are not already larger
         * than this.
         * 1000m on the diagonal translates into about 709m to each direction. */
        LatLng center = tmpBounds.getCenter();
        LatLng northEast = move(center, 709, 709);
        LatLng southWest = move(center, -709, -709);
        builder.include(southWest);
        builder.include(northEast);
        return builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(Response response, Retrofit retrofit, String tag) {
        hideProgressDialog();
        if (tag.equalsIgnoreCase("logout")) {
            try {
                if (response.isSuccessful()) {
                    PickupRequestResponse obj = (PickupRequestResponse) response.body();
                    if (obj.getStatus() == 1) {
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_userId), null);
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_role), null);
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_phone), null);
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_email), null);
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_isdriver_requested), null);
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_isdriver_approved), null);
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_gcm_token), null);
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_stripe_customer_id), null);

                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(intent);
                    } else {
                        CommonMethods.getInstance().DisplayToast(mContext, obj.getMessage() + "");
                    }
                } else {
                    CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
                }
            } catch (Exception e) {

            }
        } else if (tag.equalsIgnoreCase("mode")) {
            if (response.isSuccessful()) {
                PickupRequestResponse obj = (PickupRequestResponse) response.body();
                if (obj.getStatus() == 1) {
                    // Change Mode Successfull, Change Screen
                    if (isDriverMode) {
                        // Move to Driver Dashboard
                        // Set pref_isdriver to true in sharedPref
                        SharedPreferencesHandler.setBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriver), true);
                        SharedPreferencesHandler.setBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriverActive), true);
                        Intent intent = new Intent(mContext, DriverDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        // Mode to Passanger Dashboard
                        SharedPreferencesHandler.setBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriver), false);
                        SharedPreferencesHandler.setBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriverActive), false);
                        Intent intent = new Intent(mContext, PassangerDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                } else {
                    CommonMethods.getInstance().DisplayToast(mContext, obj.getMessage() + "");
                }
            } else {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
            }
        } else if (tag.equalsIgnoreCase("update_position")) {
            if (response.isSuccessful()) {
                UpdatePositionResponse obj = (UpdatePositionResponse) response.body();
                if (obj.getStatus() == 0) {
//                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus() == 1) {
//                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus() == 3) {
                    //Blocked
                    CommonMethods.getInstance().DisplayToast(mContext, obj.getMessage() + "");
                    ((BaseActivity) mContext).logout();
                }
            } else {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
            }
        } else if (tag.equalsIgnoreCase("toggle_Profile")) {
            if (response.isSuccessful()) {
                boolean isDriverActive = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriverActive));
                if (!isDriverActive) {
                    mToggleMenuItem.setIcon(ContextCompat.getDrawable(mContext, R.drawable.toggle_green));
                    SharedPreferencesHandler.setBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriverActive), true);

                } else {
                    mToggleMenuItem.setIcon(ContextCompat.getDrawable(mContext, R.drawable.toggle_white));
                    SharedPreferencesHandler.setBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriverActive), false);
                }
            } else {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        try {
            hideProgressDialog();
            CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
