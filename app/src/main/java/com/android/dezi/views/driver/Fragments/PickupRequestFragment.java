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
import android.app.Activity;
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
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.Permissions.PermissionsAndroid;
import com.android.dezi.R;
import com.android.dezi.api.APIResponseInterface;
import com.android.dezi.beans.NotificationPickUpRequestBean;
import com.android.dezi.beans.UpdatePositionResponse;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.ConstantFile;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mobilyte on 3/5/16.
 */
public class PickupRequestFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, APIResponseInterface {

    View rootView = null;
    Context mContext;
    Bundle bundle;
    private Marker mcurrentmarker;
    private RelativeLayout mParent;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LocationManager mLocationManager;
    android.location.LocationListener mLocationListener;
    private GoogleMap googleMap;
    private Location mLastLocation;
    private LatLng centerLatLng;
    NotificationPickUpRequestBean mNotificationData;
    private boolean isSearchLocation = false;
    private TextView mAddress, mAddress2, mPassengerName, mPassengerDistance, mCareName, mCarNo, mCarTransmission;
    CircleImageView mProfileImage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_passanger_info, container, false);
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
    public void onStart() {
        if(mGoogleApiClient!=null)
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected())
        mGoogleApiClient.disconnect();
        super.onStop();
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
                    String loc_lat = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_driver_latitude));
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
                                ((BaseActivity)mContext).updateDriverLocation(location);
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


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation == null) {
            CommonMethods.getInstance().DisplayToast(mContext, "Unable to find Location");
            return;
        }

        double latitude= Double.parseDouble(mNotificationData.getPickup_address().getLatitude());
        double longitude= Double.parseDouble(  mNotificationData.getPickup_address().getLongitude());
        LatLng position = new LatLng(latitude, longitude);
        Marker marker = googleMap.addMarker(new MarkerOptions().position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//        marker.setTitle(""+mNotificationData.getEta().get(0).getDuration());

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,15));
        /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);*/

        /*
        Google map camera change listener
         */
        /*googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition arg0) {
                // TODO Auto-generated method stub
                centerLatLng = googleMap.getCameraPosition().target;
            }
        });*/
    }

    @Override
    public void onClick(View v) {

    }
    private void dropPinEffect(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post again 15ms later.
                    handler.postDelayed(this, 15);
                } else {
                    marker.showInfoWindow();

                }
            }
        });
    }
    @Override
    public void onSuccess(Response response, Retrofit retrofit, String tag) {
        if (tag.equalsIgnoreCase("update_position")) {
            if (response.isSuccessful()) {
                UpdatePositionResponse obj = (UpdatePositionResponse) response.body();
                if (obj.getStatus() == 0) {
//                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus() == 1) {
//                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus() == 3) {
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                    ((BaseActivity) mContext).logout();
                }
            } else {
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_api), mParent);
            }
        } else {

        }
    }

    @Override
    public void onFailure(Throwable t) {
        try {
            CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
        } catch (Exception e) {

        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bundle = this.getArguments();
        mNotificationData = (NotificationPickUpRequestBean) bundle.getSerializable("driver_pickup");
        if(mNotificationData!=null)
        setPassengerData();
    }

    private void initView() {
        mProfileImage = (CircleImageView) rootView.findViewById(R.id.passenger_image);
        mParent= (RelativeLayout) rootView.findViewById(R.id.parent);
        mAddress = (TextView) rootView.findViewById(R.id.address_one);
        mAddress2 = (TextView) rootView.findViewById(R.id.address_two);
        mCareName = (TextView) rootView.findViewById(R.id.car_name);
        mPassengerName = (TextView) rootView.findViewById(R.id.passenger_name);
        mPassengerDistance = (TextView) rootView.findViewById(R.id.passenger_distance);
        mCarNo = (TextView) rootView.findViewById(R.id.car_no);
        mCarTransmission = (TextView) rootView.findViewById(R.id.car_transmission);

    }

    private void setPassengerData() {
        try {
            String profilePic = ConstantFile.PROFILE_BASE+mNotificationData.getProfile_pic();
            Picasso.with(mContext).load(profilePic).into(mProfileImage);
            mCareName.setText(mNotificationData.getCar_detail().getName() + "");
            mCarNo.setText(mNotificationData.getCar_detail().getNumber() + "," + mNotificationData.getCar_detail().getModel() + "");
            mPassengerName.setText(mNotificationData.getFirst_name() + "");
            if (mNotificationData.getCar_detail().getTransmission().equalsIgnoreCase("both")) {
                mCarTransmission.setText("B");
            } else if (mNotificationData.getCar_detail().getTransmission().equalsIgnoreCase("Automatic")) {
                mCarTransmission.setText("A");
            } else if (mNotificationData.getCar_detail().getTransmission().equalsIgnoreCase("Manual")) {
                mCarTransmission.setText("M");
            }
            /*
            Set Passenger profile Pic
             */
            Geocoder geocoder = new Geocoder(mContext, Locale.ENGLISH);
            List<Address> destinationAddress = geocoder.getFromLocation(Double.parseDouble(mNotificationData.getPickup_address().getLatitude()), Double.parseDouble(mNotificationData.getPickup_address().getLongitude()), 1);
            mAddress.setText(destinationAddress.get(0).getAddressLine(0));
            mAddress2.setText(destinationAddress.get(0).getAddressLine(1));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
