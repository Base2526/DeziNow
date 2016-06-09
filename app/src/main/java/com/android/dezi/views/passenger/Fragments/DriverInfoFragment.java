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

package com.android.dezi.views.passenger.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.dezi.Permissions.PermissionsAndroid;
import com.android.dezi.R;
import com.android.dezi.api.APIHandler;
import com.android.dezi.api.APIResponseInterface;
import com.android.dezi.beans.GetDriverLocationResponse;
import com.android.dezi.beans.NotificationAcceptRideBean;
import com.android.dezi.beans.SourceLocationBean;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.ConstantFile;
import com.android.dezi.utility.MobileConnectivity;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mobilyte on 5/5/2016.
 */
public class DriverInfoFragment extends Fragment implements OnClickListener, APIResponseInterface, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Context mContext;
    View rootView;
    Handler mDriverLocationHandler;

    NotificationAcceptRideBean beanObj;
    // Google map
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LocationManager mLocationManager;
    private GoogleMap googleMap;
    private Location mLastLocation;
    private boolean isSearchLocation = false;

    // View inside fragment
    CircleImageView mProfileImage;
    RatingBar mDriverRating;
    TextView mDriverName, mDriverDistance, mETA;
    Button mContactDriver, mCancelRide, mChangeDestination;

    Marker driverMarker, passengerMarker;

    RelativeLayout coordinatorLayout;
//    private FrameLayout mBottomSheet;
    private BottomSheetBehavior behavior;

    @Override
    public void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_driver_info, container, false);
        mContext = getActivity();
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

        initView();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNotificationData();
    }


    private void initView() {
        coordinatorLayout = (RelativeLayout) rootView.findViewById(R.id.root_view);
        mProfileImage = (CircleImageView) rootView.findViewById(R.id.driver_profile);
        mDriverRating = (RatingBar) rootView.findViewById(R.id.driver_rating);
        mDriverName = (TextView) rootView.findViewById(R.id.driver_name);
        mDriverDistance = (TextView) rootView.findViewById(R.id.driver_distance);
        mETA = (TextView) rootView.findViewById(R.id.eta);
        mContactDriver = (Button) rootView.findViewById(R.id.contact_driver);
        mCancelRide = (Button) rootView.findViewById(R.id.cancel_pickup);
        mChangeDestination = (Button) rootView.findViewById(R.id.change_destination);
        //Click Listeners
        mCancelRide.setOnClickListener(this);
        mContactDriver.setOnClickListener(this);
        mChangeDestination.setOnClickListener(this);


    }

    private void getNotificationData() {
        try {
            Bundle bundle = getActivity().getIntent().getExtras();
            if (bundle != null) {
                beanObj = (NotificationAcceptRideBean) bundle.getSerializable("accept_ride");
                if (beanObj != null) {
                    // update UI
                    String profilePic = ConstantFile.PROFILE_BASE+beanObj.getProfile_pic();
                    Picasso.with(mContext).load(profilePic).into(mProfileImage);
                    mDriverName.setText(beanObj.getFirst_name());
                    mDriverDistance.setText(beanObj.getEta().get(0).getDuration().getText());
                    mETA.setText(beanObj.getEta().get(0).getDuration().getText());

                    /*
                    Create Timer that will get Driver Position after every 30 Seconds
                     */
                    getDriverPosition();
                }
            }
        } catch (Exception e) {

        }
    }

    /*
    After Notification of accept ride, Get Driver Position after every 30 seconds
     */
    private void getDriverPosition() {
        mDriverLocationHandler = new Handler();
        final int delay = 30000; //milliseconds

        mDriverLocationHandler.postDelayed(new Runnable(){
            public void run(){
                //do something
                updateDriverPosition();
                mDriverLocationHandler.postDelayed(this, delay);
            }
        }, delay);
    }

    /*
    Hit api and Update Driver Position
     */
    private void updateDriverPosition() {
        if (MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive()) {
            String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
            if (userID == null || userID.equalsIgnoreCase("")) {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                return;
            }
            Map<String, String> param = new HashMap<>();
            param.put("token", ConstantFile.DEF_TOKEN);
            param.put("user_id", beanObj.getDriver_id());   // send Driver ID whose location you need to know

            APIHandler.getInstance(ConstantFile.APIURL).getDriverPosition(param, this);
        } else {
            CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_internet));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_driver:
                break;
            case R.id.cancel_pickup:
                break;
            case R.id.change_destination:
                break;
        }

    }

    @Override
    public void onSuccess(Response response, Retrofit retrofit, String tag) {
        try {
            if(tag.equalsIgnoreCase("get_position")){
                if(response.isSuccessful()){
                    GetDriverLocationResponse obj = (GetDriverLocationResponse)response.body();
                    if(obj.getStatus()==1){
                        //Successfull, update Driver marker position
                        driverMarker.setPosition(new LatLng(Double.parseDouble(obj.getData().getLatitude()),Double.parseDouble(obj.getData().getLongitude())));
                    }
                }
                else{
                    CommonMethods.getInstance().displaySnackBar(mContext,mContext.getResources().getString(R.string.error_api),rootView);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        try {
            CommonMethods.getInstance().displaySnackBar(mContext,mContext.getResources().getString(R.string.error_api),rootView);
        }
        catch (Exception e){

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation == null) {
            CommonMethods.getInstance().DisplayToast(mContext, "Unable to find Location");
            return;
        }
        setMarkersPosition(Double.parseDouble(beanObj.getLatitude()),Double.parseDouble(beanObj.getLongitude()));
        /*
        Google map camera change listener
         */
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // TODO Auto-generated method stub
//                centerLatLng = googleMap.getCameraPosition().target;
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

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

    /*
    Set Pickup Marker along With Driver Position
     */
    private void setMarkersPosition(double driver_lat, double driver_lng) {
        // Set marker of Driver Position
        List<Marker> totalMarkers = new ArrayList<>();

        MarkerOptions driverOption = new MarkerOptions()
                .position(new LatLng(driver_lat,driver_lng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        driverMarker = googleMap.addMarker(driverOption);
        totalMarkers.add(driverMarker); // Add marker to list
// Set marker of passanger Pickup Position
        double pickup_latitude = SourceLocationBean.getInstance().getSourceLatitude();
        double pickup_longitude = SourceLocationBean.getInstance().getSourceLongitude();
        LatLng pickup_position;

        if (pickup_latitude != 0d && pickup_longitude != 0d) {
            pickup_position = new LatLng(pickup_latitude, pickup_longitude);
            passengerMarker = googleMap.addMarker(new MarkerOptions().position(pickup_position)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            totalMarkers.add(passengerMarker);
        } else {
            CommonMethods.getInstance().displaySnackBar(mContext, "Pickup Location not found", rootView);
        }
        /*
        Google Camera
         */
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker markers : totalMarkers) {
            builder.include(markers.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        if(googleMap.getCameraPosition().zoom>15.0){
            CameraUpdateFactory.zoomTo(15);
        }
        else
        googleMap.animateCamera(cu);
        /*CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(
                        position.latitude,
                        position.longitude));
        googleMap.moveCamera(center);

        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 800, null);*/
    }
}
