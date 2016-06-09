package com.android.dezi.views.driver.Fragments;
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
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.dezi.BaseActivity;
import com.android.dezi.Permissions.PermissionsAndroid;
import com.android.dezi.R;
import com.android.dezi.api.APIHandler;
import com.android.dezi.api.APIResponseInterface;
import com.android.dezi.beans.NotificationPickUpRequestBean;
import com.android.dezi.beans.PickupRequestResponse;
import com.android.dezi.beans.UpdatePositionResponse;
import com.android.dezi.customClasses.CircularProgressBar;
import com.android.dezi.customClasses.GPSTracker;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.ConstantFile;
import com.android.dezi.utility.MobileConnectivity;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.driver.Activities.DriverDashboard;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mobilyte on 4/11/2016.
 */
public class DriverDashboardFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, APIResponseInterface {

    Context mContext;
    View rootView = null;
    NotificationPickUpRequestBean beanObj;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LocationManager mLocationManager;
    android.location.LocationListener mLocationListener;
    // Fade view
    RelativeLayout mFadeView;
    CircularProgressBar mCircularProgress;
    Vibrator vibrator;
    /*
    Recursive call method to update time till 10 seconds
     */
    int maxCount = 10;
    int percentage = 0;
    Handler handler;
    private CoordinatorLayout mParent;
    private AppBarLayout mAppbar;
    private GoogleMap googleMap;
    private Location mLastLocation;
    private Geocoder geocoder;
    private List<Address> addresses;
    private LatLng centerLatLng;
    private boolean isSearchLocation = false;
    // Main Dashboard for Driver view
    private ImageView mMyLocation;
    private LinearLayout mBottomLaout;

    private boolean isActive = true; // For Toggle Button

    Bundle bundle;
    boolean isArgumentsRead= false;

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_driver_dashboard, container, false);
        mContext = getActivity();
        //set Title
        ((DriverDashboard) mContext).mTitle.setVisibility(View.GONE);
        ((DriverDashboard) mContext).mTitle.setTextColor(CommonMethods.getInstance().getColor(mContext,R.color.black));
        /*
        Initialize Maps
         */
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
            mapFragment.getMapAsync(this);
        }
        initViews();
        // Initially hide the fade view
        mFadeView.setVisibility(View.GONE);
        if(!isArgumentsRead){
            getNotificationData();
            isArgumentsRead = true;
        }
        return rootView;
    }

    private void getNotificationData() {
        try {
                bundle = this.getArguments();
                beanObj = (NotificationPickUpRequestBean) bundle.getSerializable("driver_pickup");
                if (beanObj.getNotification_type().equalsIgnoreCase("1")) {
                    // Show Timer for 10 sec to Pick Ride
                    showRideTimer();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showRideTimer() {
        mFadeView.setVisibility(View.VISIBLE);
        // Get instance of Vibrator from current Context
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 10000 milliseconds
        vibrator.vibrate(10000);
        pickupTimer();
    }

    /*
    Initialize Views
     */
    private void initViews() {
        mBottomLaout= (LinearLayout) rootView.findViewById(R.id.bottom_layout);
        mParent = (CoordinatorLayout) rootView.findViewById(R.id.parent);
        mAppbar = (AppBarLayout) rootView.findViewById(R.id.appbar);
        mMyLocation = (ImageView) rootView.findViewById(R.id.mylocation);
        //Fade view
        mFadeView = (RelativeLayout) rootView.findViewById(R.id.fade_view);
        mCircularProgress = (CircularProgressBar) rootView.findViewById(R.id.circular_progressbar);
        mFadeView.setOnClickListener(this);
        // Switch to passenger click listener
        /*
        My Location Imageview click listener
         */
        mMyLocation.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                     /*
                    Check permissions for Location
                     */
                    if (!PermissionsAndroid.getInstance().checkLocationPermission((Activity) mContext)) {
                        PermissionsAndroid.getInstance().requestForLocationPermission((Activity) mContext);
                    } else {
                        /*
                        Check GPS is Enable or not
                        */
                        if (CommonMethods.getInstance().checkLocationService(mContext)) {
                            setUpMap();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
/*
        Check permissions for Location
         */
        if (!isSearchLocation) {
            if (!PermissionsAndroid.getInstance().checkLocationPermission((Activity) mContext)) {
                PermissionsAndroid.getInstance().requestForLocationPermission((Activity) mContext);
            } else {
            /*
            Check GPS is Enable or not
            */
                if (CommonMethods.getInstance().checkLocationService(mContext)) {
                    // Check if we were successful in obtaining the map.
                    if (googleMap != null)
                        setUpMap();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /*
    update Driver Location
     */
    private void updateDriverLocation(Location location) {
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
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_internet), rootView);
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setAllGesturesEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isSearchLocation = true;
    }

    /*
        set Up map
         */
    private void setUpMap() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setPadding(0,0,0,mBottomLaout.getHeight()+35);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        mLocationManager = (LocationManager) getActivity().getSystemService(mContext.LOCATION_SERVICE);
        String best = mLocationManager.getBestProvider(criteria, false);
        mLocationListener = new android.location.LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    String loc_lat = SharedPreferencesHandler.getStringValues(mContext,mContext.getResources().getString(R.string.pref_driver_latitude));
                    String loc_lng = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_longitude));
                    if (loc_lat != null && loc_lng != null) {
                        try {
                            double latitude = Double.parseDouble(loc_lat);
                            double longitude = Double.parseDouble(loc_lng);
//                        LatLng oldLatlng = new LatLng(latitude, longitude);
                            Location oldLocation = new Location("oldLocation");
                            oldLocation.setLatitude(latitude);
                            oldLocation.setLongitude(longitude);

                            if (oldLocation.distanceTo(location) >= 30) {
                                // update new location

                                SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_latitude), String.valueOf(location.getLatitude()));
                                SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_longitude), String.valueOf(location.getLongitude()));
                                // Hit API to store Driver Location
                                updateDriverLocation(location);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        // No location saved previously, update this for first time
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_latitude), String.valueOf(location.getLatitude()));
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_longitude), String.valueOf(location.getLongitude()));
                    }
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        mLocationManager.requestLocationUpdates(best, 1L, 1f, mLocationListener);

        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation == null) {
            CommonMethods.getInstance().DisplayToast(mContext, "Unable to find Location");
            return;
        }
        LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        String loc_lat = SharedPreferencesHandler.getStringValues(mContext,mContext.getResources().getString(R.string.pref_driver_latitude));
        String loc_lng = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_longitude));
        if(loc_lat==null && loc_lng == null){
            SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_latitude), String.valueOf(latlng.latitude));
            SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_longitude), String.valueOf(latlng.longitude));

        }
        /*
        Google Camera
         */
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(
                        latlng.latitude,
                        latlng.longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);

        /*
        Google map camera change listener
         */
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // TODO Auto-generated method stub
                centerLatLng = googleMap.getCameraPosition().target;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mFadeView) {
            handlePickupRequest();
        }
    }

    /*
    Manage PickupRequest
     */
    private void handlePickupRequest() {
        if (handler != null) handler.removeCallbacksAndMessages(null);
        mFadeView.setVisibility(View.GONE);
        // Stop Vibration
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.cancel();
        }
        // Hit API to for accepting Pickup by Driver
//        CommonMethods.getInstance().DisplayToast(mContext, "PICKUP REQUEST ACCEPTED");
        ((BaseActivity)mContext).showProgressDialog("Accepting Ride..");
        String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
        if (userID == null || userID.equalsIgnoreCase("")) {
            CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
            ((BaseActivity) mContext).hideProgressDialog();
            return;
        }
        String loc_lat = SharedPreferencesHandler.getStringValues(mContext,mContext.getResources().getString(R.string.pref_driver_latitude));
        String loc_lng = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_longitude));
        GPSTracker gpsObj = new GPSTracker(mContext);
        if(gpsObj.getIsGPSTrackingEnabled() && loc_lat==null && loc_lng==null){
            loc_lat = String.valueOf(gpsObj.getLatitude());
            loc_lng = String.valueOf(gpsObj.getLongitude());
        }
        if(TextUtils.isEmpty(loc_lat) || TextUtils.isEmpty(loc_lng) || beanObj ==null){
            CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_driver_location));
            ((BaseActivity) mContext).hideProgressDialog();
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
        param.put("user_id", userID);
        param.put("ride_id", beanObj.getRide_id()+"");
        param.put("driver_latitude", loc_lat);
        param.put("driver_longitude", loc_lng);
        CommonMethods.getInstance().e("ACCEPT REQUEST PARAMS", "Param-> " + param.toString());
        APIHandler.getInstance(ConstantFile.APIURL).acceptRide(param, this);
    }

    //    Runnable mRunnable;
    private void pickupTimer() {
        handler = new Handler();

        if (maxCount >= 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    /*
                    Logic to set percentage as Progress
                     */
                    percentage = percentage + ((10 * 100) / 100);
                    mCircularProgress.setProgress(percentage);
                    mCircularProgress.setTitle(maxCount + " SEC");
                    mCircularProgress.setTitleColor(CommonMethods.getInstance().getColor(mContext, R.color.app_textcolor));
                    mCircularProgress.setTitleSize(mContext.getResources().getDimension(R.dimen.text_size_heading_superbig));
                    maxCount = maxCount - 1;
                    pickupTimer();

                }
            }, 1000);
        } else {
            mFadeView.setVisibility(View.GONE);
            showMissedRequestDialog();
//            ((BaseActivity)mContext).forcefullyChangeMode(mContext.getString(R.string.passenger_mode), "passanger");
        }
    }

    @Override
    public void onSuccess(Response response, Retrofit retrofit, String tag) {
        ((BaseActivity)mContext).hideProgressDialog();
        if (tag.equalsIgnoreCase("update_position")) {
            if (response.isSuccessful()) {
                UpdatePositionResponse obj = (UpdatePositionResponse) response.body();
                if (obj.getStatus() == 0) {
//                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus() == 1) {
//                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus() == 3) {
                    //Blocked
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                    ((BaseActivity) mContext).logout();
                }
            } else {
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_api), mParent);
            }
        } else if (tag.equalsIgnoreCase("accept_ride")){
            // PickUp Request Accepted by Driver
            if (response.isSuccessful()) {
                PickupRequestResponse obj = (PickupRequestResponse) response.body();
                if(obj.getStatus()==0){
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus()==1){
                    //Success
                    bundle = this.getArguments();
                    if (bundle != null) {
                        ((BaseActivity) mContext).navigateToWithBundle(new PickupRequestFragment(), bundle);
                        ((BaseActivity) mContext).overridePendingTransition(R.anim.slide_in, R.anim.no_change);
                    }
                }
                else if(obj.getStatus() ==3){
                    //Blocked
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                    ((BaseActivity) mContext).logout();
                }
            }
        }

    }

    private void showMissedRequestDialog() {
            // Create custom dialog object
            final Dialog dialog = new Dialog(mContext);
            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_missed_ride);
            // set values for custom dialog components - text, image and button
            dialog.show();
            Button okButton = (Button) dialog.findViewById(R.id.ok_btn);
            // if decline button is clicked, close the custom dialog
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Change the status inactive
                    // Close dialog
                    dialog.dismiss();
                }
            });
    }

    @Override
    public void onFailure(Throwable t) {
        try {
            CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
        } catch (Exception e) {

        }
    }
}
