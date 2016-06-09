package com.android.dezi.views.common.Fragments;
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
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.dezi.Permissions.PermissionsAndroid;
import com.android.dezi.R;
import com.android.dezi.api.APIHandler;
import com.android.dezi.api.APIResponseInterface;
import com.android.dezi.beans.OTPResponse;
import com.android.dezi.beans.RegisterResponse;
import com.android.dezi.customClasses.GPSTracker;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.ConstantFile;
import com.android.dezi.utility.MobileConnectivity;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.passenger.Activities.PassangerDashboard;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mobilyte on 4/8/2016.
 */
public class OTPFragment extends Fragment implements View.OnClickListener, APIResponseInterface {
    Context mContext;
    View rootView = null;

    RelativeLayout mparent;
    ProgressDialog mDialog;
    ProgressBar mProgress;
    EditText mPhone_et, mOtp_et, mOtp_et1, mOtp_et2, mOtp_et3;
    Button mRegisterBtn, mGetOTPBtn;
    private boolean OTPSEND = false;
    String DEVICEID = null;
    String latitide=null, longitude=null;

    private boolean isOTOPSent = false;

    private String facebookID, email, phone, password, isSocial;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_otp, container, false);
        mContext = getActivity();
        initView();
        try {
            // GET DEVICE ID
            DEVICEID = Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = getArguments();
        try {
            isSocial = bundle.getString("is_social");
            facebookID = bundle.getString("facebook_id");
            email = bundle.getString("email");
            password = bundle.getString("password");

            CommonMethods.getInstance().e("", "facebookID-> " + facebookID);
            CommonMethods.getInstance().e("", "email-> " + email);
            CommonMethods.getInstance().e("", "password-> " + password);

            mPhone_et.setFocusable(true);
            mPhone_et.setFocusableInTouchMode(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Get Location and find Lat, Lng
        try {
            if (!PermissionsAndroid.getInstance().checkLocationPermission((Activity) mContext)) {
                PermissionsAndroid.getInstance().requestForLocationPermission((Activity) mContext);
            } else {
                        /*
                        Check GPS is Enable or not
                        */
                GPSTracker gpsTracker = new GPSTracker(getActivity());
                if(gpsTracker.getIsGPSTrackingEnabled()){
                    latitide = String.valueOf(gpsTracker.getLatitude());
                    longitude = String.valueOf(gpsTracker.getLongitude());
                }
                else{
                    // Ask user to enable GPS/network in settings
                    gpsTracker.showSettingsAlert();
                }
               /* if (CommonMethods.getInstance().checkLocationService(mContext)) {
                    // check if GPS enabled
                    GPSTracker gpsTracker = new GPSTracker(this);
                }*/
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return rootView;
    }

    /*
    Initialize views
     */
    private void initView() {
        mparent = (RelativeLayout) rootView.findViewById(R.id.parent);
        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mPhone_et = (EditText) rootView.findViewById(R.id.phone_et);
        mOtp_et = (EditText) rootView.findViewById(R.id.otp_et);
        mOtp_et1 = (EditText) rootView.findViewById(R.id.otp_et1);
        mOtp_et2 = (EditText) rootView.findViewById(R.id.otp_et2);
        mOtp_et3 = (EditText) rootView.findViewById(R.id.otp_et3);
        mGetOTPBtn = (Button) rootView.findViewById(R.id.btn_getotp);
        mRegisterBtn = (Button) rootView.findViewById(R.id.btn_confirm);

        mGetOTPBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mGetOTPBtn) {
            if(MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive())
            getOTP();
            else{
                CommonMethods.getInstance().displaySnackBar(mContext,mContext.getResources().getString(R.string.error_internet),rootView);
            }
        } else if (v == mRegisterBtn) {
            beginRegisteration();
        }
    }

    /*
    Get OTP
     */
    private void getOTP() {
        phone = mPhone_et.getText().toString().trim();
        if (phone.length() == 0) {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.validation_phone), rootView);
            return;
        }

        if (DEVICEID == null) {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_something), rootView);
            return;
        }
        if (mDialog == null) mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait..");
        mDialog.setCancelable(false);
        mDialog.show();
        // HIT API TO GET OTP
        Map<String, String> param = new HashMap<>();
        param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
        param.put("device_token", DEVICEID); //Device ID
        param.put("contact_number", phone);
        param.put("email",email);
        param.put("is_social",isSocial);
        CommonMethods.getInstance().e("OTP PARAMS", "Param-> " + param.toString());
        APIHandler.getInstance(ConstantFile.APIURL).sendOTP(param, this);
    }

    /*
    Register
     */
    private void beginRegisteration() {
        String otp = mOtp_et.getText().toString().trim() + mOtp_et1.getText().toString().trim() + mOtp_et2.getText().toString().trim() + mOtp_et3.getText().toString().trim();
        if (!isOTOPSent) {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.message_request_otp), rootView);
        } else if (otp.length() == 0) {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.validation_otp), rootView);
        } else {
            //Register User
            if (MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive())
                registerUser(otp);
            else {
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_internet), rootView);
            }
        }
    }

    private void registerUser(String otp) {
        try {
            GPSTracker gpsTracker = new GPSTracker(getActivity());
            if (DEVICEID == null) {
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_something), rootView);
                return;
            }
            if(gpsTracker.getIsGPSTrackingEnabled()){
                latitide = String.valueOf(gpsTracker.getLatitude());
                longitude = String.valueOf(gpsTracker.getLongitude());
            }
            if(latitide==null || longitude==null){
                if (!PermissionsAndroid.getInstance().checkLocationPermission((Activity) mContext)) {
                    PermissionsAndroid.getInstance().requestForLocationPermission((Activity) mContext);
                }
                else{
                    gpsTracker.showSettingsAlert();
                }
                CommonMethods.getInstance().displaySnackBar(mContext,"Unable to find User Position, Please Enable Location first",rootView);
                return;
            }

            if (mDialog == null) mDialog = new ProgressDialog(mContext);
            mDialog.setMessage("Please wait..");
            mDialog.setCancelable(false);
            mDialog.show();
            // HIT API TO GET OTP
            Map<String, String> param = new HashMap<>();
            param.put("token", ConstantFile.DEF_TOKEN);  // DEFAULT TOKEN
            param.put("otp", otp);  // OTP
            param.put("device_token", DEVICEID); //Device ID
            param.put("email", email); //Email ID
            param.put("contact_number", phone);
            param.put("is_social", isSocial); // If user came from facebook, then 1 else 0
            param.put("social_id", facebookID);
            param.put("password", password);
            param.put("latitude", latitide);
            param.put("longitude", longitude);

            CommonMethods.getInstance().e("REGISTER PARAMS", "Param-> " + param.toString());
            APIHandler.getInstance(ConstantFile.APIURL).registerUser(param, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Response response, Retrofit retrofit, String tag) {
        try {
            if (mDialog != null) {
                if (mDialog.isShowing()) mDialog.dismiss();
            }
            if (tag.equalsIgnoreCase("register")) {
                if (response.isSuccessful()) {
                    RegisterResponse obj = (RegisterResponse) response.body();
                    if (obj.getStatus() == 0) {
                        CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", rootView);
                    } else if (obj.getStatus() == 1) {
                        // Registeration Successfull, navigate to Profile Screen
                        // Store infor into SharedPrefrences and navigate
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_userId), obj.getUser_id() + "");
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_email), obj.getData().getEmail());
//                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_role), "3");
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_phone), obj.getData().getContact_number());
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_isdriver_requested), obj.getData().getBecome_driver_request());
                        SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_isdriver_approved), obj.getData().getIs_driver_approved());

                        Intent intent = new Intent(mContext, PassangerDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        /*if(obj.getData().getRole().equalsIgnoreCase("3")){
                            // user is Passanger, Move him to Driver Profile Page
                            Intent intent = new Intent(mContext, DriverProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else if(obj.getData().getRole().equalsIgnoreCase("4"))
                        {
                            // User already driver, move him to passanger Home Screen
                        }*/
                    }
                }
            } else {
                if (response.isSuccessful()) {
                    OTPResponse obj = (OTPResponse) response.body();
                    if (obj.getStatus() == 0) {
                        CommonMethods.getInstance().DisplayToast(mContext, obj.getMessage() + "");
//                        CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", mparent);
                    } else {
                        CommonMethods.getInstance().DisplayToast(mContext, obj.getMessage() + "");
                        try {
                            mOtp_et.setText(obj.getOtp().charAt(0) + "");
                            mOtp_et1.setText(obj.getOtp().charAt(1) + "");
                            mOtp_et2.setText(obj.getOtp().charAt(2) + "");
                            mOtp_et3.setText(obj.getOtp().charAt(3) + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        isOTOPSent = true;
                        // Enable Resend OTP Button
                        //Disable SEND OTP BUTTON

                        mGetOTPBtn.setAlpha((float) 0.5);
                        mGetOTPBtn.setText(R.string.resend_verification_code);
                    }
                } else {
                    CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(Throwable t) {
        CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_api));
        if (mDialog != null) {
            if (mDialog.isShowing()) mDialog.dismiss();
        }
    }
}
