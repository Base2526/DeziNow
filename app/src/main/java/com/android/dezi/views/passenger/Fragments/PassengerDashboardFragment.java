package com.android.dezi.views.passenger.Fragments;
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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dezi.BaseActivity;
import com.android.dezi.Permissions.PermissionsAndroid;
import com.android.dezi.R;
import com.android.dezi.beans.NotificationAcceptRideBean;
import com.android.dezi.beans.SourceLocationBean;
import com.android.dezi.customClasses.TouchableWrapper;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.views.passenger.Activities.SearchActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mobilyte on 4/26/2016.
 */
public class PassengerDashboardFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,TouchableWrapper.UpdateMapAfterUserInterection {

    Context mContext;
    View rootView = null;
    private ImageView mMyLocation;
    GoogleApiClient mGoogleApiClient;
    private GoogleMap googleMap;
    private Location mLastLocation;
    LocationRequest mLocationRequest;
    private int PROXIMITY_RADIUS = 5000;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3000;
    private int LOCATION_REQUEST_CODE = 1;
    private LatLng mlastLatlng;
    private Geocoder geocoder;
    private List<android.location.Address> addresses;
    private Button mPickupLocation;
    private LatLng centerLatLng;
    private LinearLayout markerLayout, searchLayout,mBottomLayout;
    private TextView Address;
    public TouchableWrapper mTouchView;
    private boolean isSearchLocation = false;

    NotificationAcceptRideBean beanObj;
    Bundle bundle;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_passanger_dashboard, container, false);
        mContext = getActivity();
        ((BaseActivity)mContext).mTitle.setVisibility(View.GONE);
        ((BaseActivity)mContext).mTitleImageview.setVisibility(View.VISIBLE);
        initViews();
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
        mTouchView = new TouchableWrapper(getActivity());
        mTouchView.setUpdateMapAfterUserInterection(this);
        mTouchView.addView(rootView);
        return mTouchView;
    }
    /*
      Initialize Views
    */
    void initViews() {
        mMyLocation = (ImageView) rootView.findViewById(R.id.mylocation);
        mPickupLocation = (Button) rootView.findViewById(R.id.setPickupLocation);
        Address = (TextView) rootView.findViewById(R.id.adressText);
        markerLayout = (LinearLayout) rootView.findViewById(R.id.locationMarker);
        searchLayout = (LinearLayout) rootView.findViewById(R.id.search_layout);
        mBottomLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
        getNotificationData();
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
                            setUpMap("location");
                        }

                    }
                }
            }
        });

        /*
        Set Pickup Location Click Listener
         */
        mPickupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addresses != null && !Address.getText().toString().equalsIgnoreCase("Getting location")) {
//                    Intent intent = new Intent(mContext, ConfirmationActivity.class);
//                    intent.putExtra("pickup_location", Address.getText().toString());
//                    startActivity(intent);
                    Bundle bundle = new Bundle();
                    bundle.putString("pickup_location",SourceLocationBean.getInstance().getSourceAddress().getAddressLine(0)+" " +
                    SourceLocationBean.getInstance().getSourceAddress().getAddressLine(1));
                    ((BaseActivity)mContext).navigateToWithBundle(new ConfirmFragment(),bundle);
                } else {
                    CommonMethods.getInstance().displaySnackBar(mContext, "Looking for Location", rootView);
                }
            }
        });




        /*
        Search Area Click Listener
         */
//        AutocompleteFilter filter = new AutocompleteFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to Search Screen
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }
    private void getNotificationData() {
        try {
            bundle = this.getArguments();
            beanObj = (NotificationAcceptRideBean) bundle.getSerializable("accept_ride");
            if (beanObj.getNotification_type().equalsIgnoreCase("2")) {
                bundle = this.getArguments();
                if (bundle != null) {
                    Toast.makeText(mContext, "Ride Accepted by Driver", Toast.LENGTH_SHORT).show();
//                    ((BaseActivity) mContext).navigateToWithBundle(new PickupRequestFragment(), bundle);
//                    ((BaseActivity) mContext).overridePendingTransition(R.anim.slide_in, R.anim.no_change);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    set Up map
     */
    private void setUpMap(String location) {

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
        googleMap.setPadding(0,0,0,mBottomLayout.getHeight()+30);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation == null){
//            CommonMethods.getInstance().DisplayToast(mContext,"Unable to find Location");
            return;
        }
        LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        if (mlastLatlng != null && location.equalsIgnoreCase("location")) {
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(
                            latlng.latitude,
                            latlng.longitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            centerLatLng = googleMap.getCameraPosition().target;
            setPickUpAddress(centerLatLng.latitude, centerLatLng.longitude);
        } else if (mlastLatlng == null && location.equalsIgnoreCase("destination")) {
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(
                            latlng.latitude,
                            latlng.longitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            centerLatLng = googleMap.getCameraPosition().target;
            mlastLatlng = googleMap.getCameraPosition().target;
            markerLayout.setVisibility(View.VISIBLE);

            try {
                CommonMethods.getInstance().e("", "Lat-> " + centerLatLng.latitude + " Long-> " + centerLatLng.longitude);
                new GetLocationAsync(centerLatLng.latitude, centerLatLng.longitude)
                        .execute();

            } catch (Exception e) {
            }
            setPickUpAddress(centerLatLng.latitude, centerLatLng.longitude);
        }

    }

    @Override
    public void onUpdateMapAfterUserInterection() {
        centerLatLng = googleMap.getCameraPosition().target;
        markerLayout.setVisibility(View.VISIBLE);
        try {
            CommonMethods.getInstance().e("", "Lat-> " + centerLatLng.latitude + " Long-> " + centerLatLng.longitude);
            new GetLocationAsync(centerLatLng.latitude, centerLatLng.longitude)
                    .execute();

        } catch (Exception e) {
        }
    }

    private class GetLocationAsync extends AsyncTask<String, Void, String> {
        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
            Address.setText(" Getting location ");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                geocoder = new Geocoder(mContext, Locale.ENGLISH);
                addresses = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    try {
                        android.location.Address returnAddress = addresses.get(0);
//
                        String localityString = returnAddress.getLocality();
                        String city = returnAddress.getCountryName();
                        String region_code = returnAddress.getCountryCode();
                        String zipcode = returnAddress.getPostalCode();

                        str.append(localityString + "");
                        str.append(city + "" + region_code + "");
                        str.append(zipcode + "");

                        CommonMethods.getInstance().e("", "Location found-> " + str.toString());
                        return str.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    CommonMethods.getInstance().DisplayToast(mContext, "Location Not Found");
                }
            } catch (IOException e) {
                CommonMethods.getInstance().e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                CommonMethods.getInstance().e("", "Address line 0 --> " + addresses.get(0).getAddressLine(0));
                CommonMethods.getInstance().e("", "Address line 1 --> " + addresses.get(0).getAddressLine(1));
//                Address.setText(addresses.get(0).getAddressLine(0)
//                        + " " + addresses.get(0).getAddressLine(1) + " ");
                setPickUpAddress(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleMap != null)
            setUpMap("destination");
    }

    @Override
    public void onConnected(Bundle bundle) {
        /*
        Check permissions for Location
         */
        if(!isSearchLocation)
        {
            if (!PermissionsAndroid.getInstance().checkLocationPermission((Activity) mContext)) {
                PermissionsAndroid.getInstance().requestForLocationPermission((Activity) mContext);
            } else {
            /*
            Check GPS is Enable or not
            */
                if(CommonMethods.getInstance().checkLocationService(mContext)){
                    // Check if we were successful in obtaining the map.
                    if (googleMap != null)
                        setUpMap("destination");
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

    // A place has been received; use requestCode to track the request.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                isSearchLocation = true;
//                    CommonMethods.getInstance().DisplayToast(mContext,"on activity result");
                String places_lat = data.getStringExtra("lat");
                String places_lng = data.getStringExtra("lng");
                Double latitude = Double.parseDouble(places_lat);
                Double longitude = Double.parseDouble(places_lng);
//                    CommonMethods.getInstance().DisplayToast(mContext,"search done");
                try {
                    if(geocoder == null){
                        CommonMethods.getInstance().DisplayToast(mContext,"GeoCode is not initialized yet");
                        return;
                    }
                    CameraUpdate center =
                            CameraUpdateFactory.newLatLng(new LatLng(
                                    latitude,
                                    longitude));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                    setPickUpAddress(latitude,longitude);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
                CommonMethods.getInstance().DisplayToast(mContext , "Cancelled Request ");
            }


                /*
                Activity Result in case of Location services
                 */
            else if(requestCode == LOCATION_REQUEST_CODE){
                CommonMethods.getInstance().DisplayToast(mContext,data.toString());

            }


        }
        else if(resultCode == getActivity().RESULT_CANCELED){
            //Cancelled By User
        }
    }

    /*
   Set Source Location Information
    */
    private void setSourceLocationInfo(android.location.Address address){
        // set Source Information to model class
        if(address!=null){
            SourceLocationBean.getInstance().setSourceAddress(address);
            SourceLocationBean.getInstance().setSourceLatitude(address.getLatitude());
            SourceLocationBean.getInstance().setSourceLongitude(address.getLongitude());
        }
        else{
            CommonMethods.getInstance().DisplayToast(mContext,"Address is null");
        }

    }

    /*
    Set Current Address to Pickup Area
     */
    private void setPickUpAddress(double latitude, double longitude){
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            StringBuilder str = new StringBuilder();
            if (geocoder.isPresent()) {
                try {
                    // set Source Information to model class
                    setSourceLocationInfo(addresses.get(0));

                    android.location.Address returnAddress = addresses.get(0);
                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    Address.setText(addresses.get(0).getAddressLine(0)
                            + " " + addresses.get(0).getAddressLine(1) + " ");
                    /*
                       Google Camera
                      */
//                    CameraUpdate center =
//                            CameraUpdateFactory.newLatLng(new LatLng(
//                                    latitude,
//                                    longitude));
//                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
//                    googleMap.moveCamera(center);
//                    googleMap.animateCamera(zoom);

                    // set Source Information to model class
                    setSourceLocationInfo(addresses.get(0));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                CommonMethods.getInstance().DisplayToast(mContext, "Location Not Found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
