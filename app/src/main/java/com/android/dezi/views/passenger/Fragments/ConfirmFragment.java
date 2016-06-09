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
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.Permissions.PermissionsAndroid;
import com.android.dezi.R;
import com.android.dezi.adapters.CarListAdapter;
import com.android.dezi.adapters.PaymentModeListAdapter;
import com.android.dezi.api.APIHandler;
import com.android.dezi.api.APIResponseInterface;
import com.android.dezi.beans.EstimatedFareResponse;
import com.android.dezi.beans.PassangerRideInfoResponse;
import com.android.dezi.beans.PickupRequestResponse;
import com.android.dezi.beans.SourceLocationBean;
import com.android.dezi.customClasses.TouchableWrapper;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.ConstantFile;
import com.android.dezi.utility.MobileConnectivity;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mobilyte on 3/29/2016.
 */
public class ConfirmFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener, APIResponseInterface, TouchableWrapper.UpdateMapAfterUserInterection {
    public TouchableWrapper mTouchView;
    Context mContext;
    View rootView = null;
    GoogleApiClient mGoogleApiClient;
    ArrayList<LatLng> markerPoints;
    LocationRequest mLocationRequest;
    private CoordinatorLayout mParent;
    private int PLACE_DESTINATION_REQUEST_CODE = 4000;
    private int LOCATION_REQUEST_CODE = 1;
    private GoogleMap googleMap;
    private Location mLastLocation;
    private LatLng mlastLatlng;
    private Geocoder geocoder;
    private List<Address> destinationAddress;
    private List<Address> pickupAddress;


    private ImageView mMyLocation;
    //    private TextView markerText;
    private LatLng centerLatLng;
    private LinearLayout markerLayout, destinationLayout, mFareEstimate, mBack_btn;
    private TextView pickup_Address, destination_Address, mConfirmRide;

    private LinearLayout mContentLayout;
    private Spinner mCarsSpinner, mPaymentModeSpinner;
    private CarListAdapter mCarAdapter = null;
    private PaymentModeListAdapter mPaymentModeAdapter = null;
    private String mSelectedCarID = null, mSelectedPaymentMethodID = null;

    private Handler handler;

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

    @Override
    public void onDestroyView() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_confirmation, container, false);
        mContext = getActivity();
        ((BaseActivity) mContext).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((BaseActivity)mContext).mTitle.setVisibility(View.GONE);
        ((BaseActivity)mContext).mTitleImageview.setVisibility(View.VISIBLE);
        initViews();
        /*
        Set pickup location
         */
        try {
            Bundle bundle = this.getArguments();
            String pickupLocation = bundle.getString("pickup_location");
            pickup_Address.setText(pickupLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        mapFragment.getMapAsync(this);

        mTouchView = new TouchableWrapper(getActivity());
        mTouchView.setUpdateMapAfterUserInterection(this);
        mTouchView.addView(rootView);
        /*
        Fetch User Cars List and payment methods
         */
        if (MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive()) {
            ((BaseActivity) mContext).showProgressDialog("Loading Info..");
            fetchUserInfo();
        } else {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_internet), rootView);
        }

        return mTouchView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /*
    Initialize Views
     */
    void initViews() {
        mParent = (CoordinatorLayout) rootView.findViewById(R.id.parent);
        mContentLayout = (LinearLayout) rootView.findViewById(R.id.confirm_content);
        mMyLocation = (ImageView) rootView.findViewById(R.id.mylocation);
        markerLayout = (LinearLayout) rootView.findViewById(R.id.locationMarker);

//        pickupLayout = (LinearLayout) rootView.findViewById(R.id.pickup_layout);
        destinationLayout = (LinearLayout) rootView.findViewById(R.id.destination_layout);
        mBack_btn = (LinearLayout) rootView.findViewById(R.id.back);
        pickup_Address = (TextView) rootView.findViewById(R.id.pickup_adressText);
        destination_Address = (TextView) rootView.findViewById(R.id.destination_adressText);

        mCarsSpinner = (Spinner) rootView.findViewById(R.id.cars_spinner);
        mPaymentModeSpinner = (Spinner) rootView.findViewById(R.id.payment_spinner);
        mFareEstimate = (LinearLayout) rootView.findViewById(R.id.fare_estimate);
        mConfirmRide = (TextView) rootView.findViewById(R.id.confirm_ride);

        /*
        Set Click Listeners
         */
        mContentLayout.setOnClickListener(this);
        mMyLocation.setOnClickListener(this);
        destinationLayout.setOnClickListener(this);
        mFareEstimate.setOnClickListener(this);
        mConfirmRide.setOnClickListener(this);
        mBack_btn.setOnClickListener(this);

        mCarsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedCarID = ((TextView) view.findViewById(R.id.id)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPaymentModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPaymentMethodID = ((TextView) view.findViewById(R.id.id)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == mMyLocation) {
            if (googleMap != null) {
                     /*
                    Check permissions for Location
                     */
                if (!PermissionsAndroid.getInstance().checkLocationPermission((Activity) mContext)) {
                    PermissionsAndroid.getInstance().requestForLocationPermission((Activity) mContext);
                } else {
//                        Check GPS is Enable or not
                    if (CommonMethods.getInstance().checkLocationService(mContext)) {
                        setUpMap("location");
                    }
                }
            }
        } else if (v == mFareEstimate) {
            if (destinationAddress != null) {
                ((BaseActivity) mContext).showProgressDialog("Getting Fare Estimate");
                getFareEstimate();
            } else {
                CommonMethods.getInstance().displaySnackBar(mContext, "Please Enter Destination First", mParent);
            }
        } else if (v == destinationLayout) {
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                                    .setFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                                .build(getActivity());
                startActivityForResult(intent, PLACE_DESTINATION_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
                CommonMethods.getInstance().DisplayToast(mContext, "Google Play Service Exception Occured");
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
                CommonMethods.getInstance().DisplayToast(mContext, "Google Play Service is not Available");
            }
        } else if (v == mConfirmRide) {
            if (!TextUtils.isEmpty(mSelectedCarID) && !TextUtils.isEmpty(mSelectedPaymentMethodID) &&
                    SourceLocationBean.getInstance().getDestinationLongitude() != 0d &&
                    SourceLocationBean.getInstance().getDestinationLatitude() != 0d) {
                ((BaseActivity) mContext).showProgressDialog("Requesting Pickup..");
                pickupRequest();
            } else if (TextUtils.isEmpty(mSelectedCarID)) {
                CommonMethods.getInstance().displaySnackBar(mContext, "Please Add car before Ride", rootView);
            } else if (TextUtils.isEmpty(mSelectedPaymentMethodID)) {
                CommonMethods.getInstance().displaySnackBar(mContext, "Please Add Payment method before Ride", rootView);
            } else if (destinationAddress == null) {
                CommonMethods.getInstance().displaySnackBar(mContext, "Please enter destination first", rootView);
            }

        } else if (v == mBack_btn) {
            ((BaseActivity) mContext).oneStepBack();
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
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation == null) {
            CommonMethods.getInstance().DisplayToast(mContext, "Unable to find Location");
            return;
        }
        LatLng latlng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        /*
        Google Camera
         */
        if (mlastLatlng != null && location.equalsIgnoreCase("location")) {
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(
                            latlng.latitude,
                            latlng.longitude));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
            setDestinationAddress(latlng.latitude, latlng.longitude);
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
            try {
                CommonMethods.getInstance().e("", "Lat-> " + centerLatLng.latitude + " Long-> " + centerLatLng.longitude);
                new GetLocationAsync(centerLatLng.latitude, centerLatLng.longitude)
                        .execute();
//                    latitude=centerLatLng.latitude;

            } catch (Exception e) {
                e.printStackTrace();
            }
            setDestinationAddress(latlng.latitude, latlng.longitude);
        }
        /*
        Google map camera change listener
         */
    }

    private void setDestinationAddress(double latitude, double longitude) {
        try {
            destinationAddress = geocoder.getFromLocation(latitude, longitude, 1);
            StringBuilder str = new StringBuilder();
            if (geocoder.isPresent()) {
                try {
                    // set Source Information to model class
                    setDestinationLocationInfo(destinationAddress.get(0));

                    android.location.Address returnAddress = destinationAddress.get(0);
                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    destination_Address.setText(destinationAddress.get(0).getAddressLine(0)
                            + " " + destinationAddress.get(0).getAddressLine(1) + " ");


                    // set Source Information to model class
                    setDestinationLocationInfo(destinationAddress.get(0));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                CommonMethods.getInstance().DisplayToast(mContext, "Location Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Response response, Retrofit retrofit, String tag) {
        ((BaseActivity) mContext).hideProgressDialog();
        if (tag.equalsIgnoreCase("passanger_ride_info")) {
            if (response.isSuccessful()) {
                PassangerRideInfoResponse obj = (PassangerRideInfoResponse) response.body();
                if (obj.getStatus() == 0) {
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus() == 1) {
                    // API Successfull, Update Car info and payment methods
                    if (obj.getData().getCars().size() > 0) {
                        mCarAdapter = new CarListAdapter(mContext, obj, ConfirmFragment.this);
                        mCarsSpinner.setAdapter(mCarAdapter);
                    } else {

                        ArrayList<PassangerRideInfoResponse.DataEntity.CarsEntity> defCar = new ArrayList<PassangerRideInfoResponse.DataEntity.CarsEntity>();
                        PassangerRideInfoResponse.DataEntity.CarsEntity entity = new PassangerRideInfoResponse.DataEntity.CarsEntity();
                        entity.setId("");
                        entity.setName("Select Car");
                        defCar.add(entity);
                        obj.getData().setCars(defCar);
                        mCarAdapter = new CarListAdapter(mContext, obj, ConfirmFragment.this);
                        mCarsSpinner.setAdapter(mCarAdapter);
                    }

                    if (obj.getData().getPayment_methods().size() > 0) {
                        mPaymentModeAdapter = new PaymentModeListAdapter(mContext, obj, ConfirmFragment.this);
                        mPaymentModeSpinner.setAdapter(mPaymentModeAdapter);
                    } else {
                        ArrayList<PassangerRideInfoResponse.DataEntity.PaymentMethodsEntity> defPayment = new ArrayList<PassangerRideInfoResponse.DataEntity.PaymentMethodsEntity>();
                        PassangerRideInfoResponse.DataEntity.PaymentMethodsEntity entity = new PassangerRideInfoResponse.DataEntity.PaymentMethodsEntity();
                        entity.setId("");
                        entity.setAccount_type("Select payment Mode");
                        defPayment.add(entity);
                        obj.getData().setPayment_methods(defPayment);
                        mPaymentModeAdapter = new PaymentModeListAdapter(mContext, obj, ConfirmFragment.this);
                        mPaymentModeSpinner.setAdapter(mPaymentModeAdapter);
                    }

                } else if (obj.getStatus() == 3) {
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                    ((BaseActivity) mContext).logout();
                }
            } else {
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_api), rootView);
            }
        } else if (tag.equalsIgnoreCase("estimate_fare")) {
            if (response.isSuccessful()) {
                EstimatedFareResponse obj = (EstimatedFareResponse) response.body();
                if (obj.getStatus() == 0) {
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus() == 1) {
                    // API Successfull, Open Alert Dialog with Information
                    showFareEstimate(obj);
//                    CommonMethods.getInstance().displaySnackBar(mContext, "Estimated Fare " + obj.getData().getAmount() + " " + obj.getData().getCurrency(), rootView);
                } else if (obj.getStatus() == 3) {
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                    ((BaseActivity) mContext).logout();
                }
            } else {
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_api), rootView);
            }
        } else if (tag.equalsIgnoreCase("pickup_request")) {
            if (response.isSuccessful()) {
                PickupRequestResponse obj = (PickupRequestResponse) response.body();
                if (obj.getStatus() == 0) {
                    showErrorMessage();
//                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                } else if (obj.getStatus() == 1) {
                    // API Successfull, Open Alert Dialog with Information
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", rootView);
                    rejectTimer();
                } else if (obj.getStatus() == 3) {
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mParent);
                    ((BaseActivity) mContext).logout();
                }
            } else {
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_api), rootView);
            }
        }
    }

    private void showErrorMessage() {
        // Create custom dialog object
        final Dialog dialog = new Dialog(mContext);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);

        // set values for custom dialog components - text, image and button
        dialog.show();
        TextView errorMessage = (TextView)dialog.findViewById(R.id.error_message);
        Button okButton = (Button) dialog.findViewById(R.id.ok_btn);
        // if decline button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });
        //set error message
        errorMessage.setText(R.string.alart_message);
    }

    private void showFareEstimate(EstimatedFareResponse obj) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(mContext);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fare_estimate);

        // set values for custom dialog components - text, image and button
        dialog.show();
        TextView minAmount = (TextView)dialog.findViewById(R.id.min_amount);
        TextView maxAmount = (TextView)dialog.findViewById(R.id.max_amount);
        Button okButton = (Button) dialog.findViewById(R.id.ok_btn);
        // if decline button is clicked, close the custom dialog
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });
        /*
        Insert Data into dialog
         */
        minAmount.setText(obj.getData().getAmount()+"");
        maxAmount.setText((obj.getData().getAmount()+10)+"");

    }

    private void rejectTimer() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mContext != null)
                    rejectRide();
            }
        }, 15000);
    }

    private void rejectRide() {
        //
        CommonMethods.getInstance().DisplayToast(getActivity(), "Please search for another Driver");
    }

    @Override
    public void onFailure(Throwable t) {
        try {
            ((BaseActivity) mContext).hideProgressDialog();
            CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateMapAfterUserInterection() {
        centerLatLng = googleMap.getCameraPosition().target;
        mlastLatlng = googleMap.getCameraPosition().target;

        try {
            CommonMethods.getInstance().e("", "Lat-> " + centerLatLng.latitude + " Long-> " + centerLatLng.longitude);
            new GetLocationAsync(centerLatLng.latitude, centerLatLng.longitude)
                    .execute();
//                    latitude=centerLatLng.latitude;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDestinationLocationInfo(android.location.Address address) {
        // set Source Information to model class
        if (address != null) {
            SourceLocationBean.getInstance().setDestinationAddress(address);
            SourceLocationBean.getInstance().setDestinationLatitude(address.getLatitude());
            SourceLocationBean.getInstance().setDestinationLongitude(address.getLongitude());
        } else {
            CommonMethods.getInstance().DisplayToast(mContext, "Address is null");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
                setUpMap("destination");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

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
    on Activity Result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == PLACE_DESTINATION_REQUEST_CODE) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                try {
                    if (geocoder == null) {
                        geocoder = new Geocoder(mContext, Locale.ENGLISH);  // Initialize GeoCoder
                    }

                    destinationAddress = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    CameraUpdate center =
                            CameraUpdateFactory.newLatLng(new LatLng(
                                    destinationAddress.get(0).getLatitude(),
                                    destinationAddress.get(0).getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                    setDestinationAddress(destinationAddress.get(0).getLatitude(), destinationAddress.get(0).getLongitude());

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(mContext, data);
                // TODO: Handle the error.
                CommonMethods.getInstance().DisplayToast(mContext, status.getStatusMessage());

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
                CommonMethods.getInstance().DisplayToast(mContext, "Cancelled Request ");
            }


                /*
                Activity Result in case of Location services
                 */
            else if (requestCode == LOCATION_REQUEST_CODE) {
                CommonMethods.getInstance().DisplayToast(mContext, data.toString());

            }


        } else if (resultCode == getActivity().RESULT_CANCELED) {
            //Cancelled By User
        }
    }

    /*
    Get Estimated Price
     */
    private void getFareEstimate() {
        try {
            if (SourceLocationBean.getInstance().getSourceLatitude() == 0.0 || SourceLocationBean.getInstance().getSourceLongitude() == 0.0 ||
                    SourceLocationBean.getInstance().getDestinationLatitude() == 0.0 || SourceLocationBean.getInstance().getDestinationLongitude() == 0.0) {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                ((BaseActivity) mContext).hideNavigationButton();
                return;
            }
            String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
            if (userID == null || userID.equalsIgnoreCase("")) {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                return;
            }
            Map<String, String> param = new HashMap<>();
            param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
            param.put("user_id", userID);
            param.put("pickup_latitude", String.valueOf(SourceLocationBean.getInstance().getSourceLatitude()));
            param.put("pickup_longitude", String.valueOf(SourceLocationBean.getInstance().getSourceLongitude()));
            param.put("destination_latitude", String.valueOf(SourceLocationBean.getInstance().getDestinationLatitude()));
            param.put("destination_longitude", String.valueOf(SourceLocationBean.getInstance().getDestinationLongitude()));
            CommonMethods.getInstance().e("Estimated Fare PARAMS", "Param-> " + param.toString());
            APIHandler.getInstance(ConstantFile.APIURL).getEstimatedFare(param, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    PICKUP REQUEST
    */
    private void pickupRequest() {
        try {
            if (SourceLocationBean.getInstance().getSourceLatitude() == 0.0 || SourceLocationBean.getInstance().getSourceLongitude() == 0.0 ||
                    SourceLocationBean.getInstance().getDestinationLatitude() == 0.0 || SourceLocationBean.getInstance().getDestinationLongitude() == 0.0) {
                CommonMethods.getInstance().DisplayToast(mContext, "Please Reset your location");
                ((BaseActivity) mContext).hideNavigationButton();
                return;
            }
            String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
            if (userID == null || userID.equalsIgnoreCase("")) {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                ((BaseActivity) mContext).hideNavigationButton();
                return;
            }
            Map<String, String> param = new HashMap<>();
            param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
            param.put("user_id", userID);
            param.put("pickup_latitude", String.valueOf(SourceLocationBean.getInstance().getSourceLatitude()));
            param.put("pickup_longitude", String.valueOf(SourceLocationBean.getInstance().getSourceLongitude()));
            param.put("destination_latitude", String.valueOf(SourceLocationBean.getInstance().getDestinationLatitude()));
            param.put("destination_longitude", String.valueOf(SourceLocationBean.getInstance().getDestinationLongitude()));
            param.put("car_id", mSelectedCarID);
            CommonMethods.getInstance().e("PICKUP REQUEST PARAMS", "Param-> " + param.toString());
            APIHandler.getInstance(ConstantFile.APIURL).pickupRequest(param, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    Fetch User Info On start of this screen
     */
    private void fetchUserInfo() {
        try {
            String userID = SharedPreferencesHandler.getStringValues(mContext, mContext.getResources().getString(R.string.pref_userId));
            if (userID == null || userID.equalsIgnoreCase("")) {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
                ((BaseActivity) mContext).hideNavigationButton();
                return;
            }
            Map<String, String> param = new HashMap<>();
            param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
            param.put("user_id", userID);
            CommonMethods.getInstance().e("User Ride Info PARAMS", "Param-> " + param.toString());
            APIHandler.getInstance(ConstantFile.APIURL).getPassangerRideInfo(param, this);
        } catch (Exception e) {
            e.printStackTrace();
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
            destination_Address.setText(" Getting location ");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                geocoder = new Geocoder(mContext, Locale.ENGLISH);
                destinationAddress = geocoder.getFromLocation(x, y, 1);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    try {
                        android.location.Address returnAddress = destinationAddress.get(0);
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
                if (destinationAddress != null) {
                    CommonMethods.getInstance().e("", "Destination Address line 0 --> " + destinationAddress.get(0).getAddressLine(0));
                    CommonMethods.getInstance().e("", "Destination Address line 1 --> " + destinationAddress.get(0).getAddressLine(1));
                    setDestinationAddress(destinationAddress.get(0).getLatitude(), destinationAddress.get(0).getLongitude());
                } else {
                    CommonMethods.getInstance().DisplayToast(mContext, "Problem while getting Location");
                }

//                destination_Address.setText(destinationAddress.get(0).getAddressLine(0)
//                        + " " + destinationAddress.get(0).getAddressLine(1) + " ");
//                // set Destination Information to model class
//                setDestinationLocationInfo(destinationAddress.get(0));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

}
