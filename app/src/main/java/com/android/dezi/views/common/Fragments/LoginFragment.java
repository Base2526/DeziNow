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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.api.APIHandler;
import com.android.dezi.api.APIResponseInterface;
import com.android.dezi.beans.LoginResponse;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.ConstantFile;
import com.android.dezi.utility.MobileConnectivity;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.common.Activities.LoginActivity;
import com.android.dezi.views.passenger.Activities.PassangerDashboard;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Mobilyte on 3/22/2016.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, APIResponseInterface {
    Context mContext;
    View rootView = null;

    CoordinatorLayout mparent;

    EditText mEmail_et, mpassword_et;
    LinearLayout mFacebookBtn;
    TextView mSignUpBtn, mEmailError, mPasswordError;
    Button mLoginBtn;
    ProgressDialog mDialog;

    private String facebookID = null;
    private String DEVICEID;
    private boolean isPhone = false;

    CallbackManager callbackManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mContext = getActivity();
        initView();
        initTextWatcher();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(getActivity());
    }

    private void initView() {
        callbackManager = CallbackManager.Factory.create();

        mparent = (CoordinatorLayout) rootView.findViewById(R.id.parent);

        mEmail_et = (EditText) rootView.findViewById(R.id.login_email);
        mpassword_et = (EditText) rootView.findViewById(R.id.login_password);
        mEmailError = (TextView) rootView.findViewById(R.id.error_email);
        mPasswordError = (TextView) rootView.findViewById(R.id.error_password);
        mLoginBtn = (Button) rootView.findViewById(R.id.login_btn);
        mSignUpBtn = (TextView) rootView.findViewById(R.id.login_signupbtn);

        mFacebookBtn = (LinearLayout) rootView.findViewById(R.id.btn_facebook);
        mLoginBtn.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
        mFacebookBtn.setOnClickListener(this);
    }

    private void initTextWatcher() {
        mEmail_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email_phone = s.toString();
                if (email_phone != null && email_phone.length() > 0) {
                    boolean atleastOneAlpha = email_phone.matches(".*[a-zA-Z]+.*");
                    if (atleastOneAlpha) {
                        if (CommonMethods.getInstance().validateEmail(email_phone)) {
                            mEmailError.setVisibility(View.GONE);
                        } else {
                            mEmailError.setText(mContext.getResources().getString(R.string.validation_valid_email));
                            mEmailError.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //Entered text is phone number
                        if (CommonMethods.getInstance().isPhoneValid(email_phone)) {
                            mEmailError.setVisibility(View.GONE);
                        } else {
                            mEmailError.setText(mContext.getResources().getString(R.string.validation_valid_phone));
                            mEmailError.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    mEmailError.setText(mContext.getResources().getString(R.string.validation_email_phone));
                    mEmailError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mpassword_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                if (password != null) {
                    if (password.length() == 0) {
                        mPasswordError.setText(mContext.getResources().getString(R.string.validation_password));
                        mPasswordError.setVisibility(View.VISIBLE);
                    } else if (!CommonMethods.getInstance().isPasswordValid(password)) {
                        mPasswordError.setText(mContext.getResources().getString(R.string.validation_password_length));
                        mPasswordError.setVisibility(View.VISIBLE);
                    } else {
                        mPasswordError.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == mFacebookBtn) {
            //Initiate Facebook Login
            fbLogin();
        } else if (v == mLoginBtn) {
            checkLogin();
        } else if (v == mSignUpBtn) {
            ((LoginActivity) mContext).navigateTo(new SignUpFragment());
        }
    }

    /*
    Check Login
     */
    private void checkLogin() {
        String email, password;

        email = mEmail_et.getText().toString().trim();
        password = mpassword_et.getText().toString();

        boolean atleastOneAlpha = email.matches(".*[a-zA-Z]+.*");

        if (email.length() == 0) {
            mEmailError.setText(mContext.getResources().getString(R.string.validation_email_phone));
            mEmailError.setVisibility(View.VISIBLE);
        } else if (atleastOneAlpha && !CommonMethods.getInstance().validateEmail(email)) {
            // User trying to enter email
            mEmailError.setText(mContext.getResources().getString(R.string.validation_valid_email));
            mEmailError.setVisibility(View.VISIBLE);
        } else if (!atleastOneAlpha && !CommonMethods.getInstance().isPhoneValid(email)) {
            // user trying to enter phone
            mEmailError.setText(mContext.getResources().getString(R.string.validation_valid_phone));
            mEmailError.setVisibility(View.VISIBLE);
        } else if (password.length() == 0) {
            mPasswordError.setText(mContext.getResources().getString(R.string.validation_password));
            mPasswordError.setVisibility(View.VISIBLE);
            mEmailError.setVisibility(View.GONE);
        } else if (!CommonMethods.getInstance().isPasswordValid(password)) {
            mPasswordError.setText(mContext.getResources().getString(R.string.validation_password_length));
            mPasswordError.setVisibility(View.VISIBLE);
            mEmailError.setVisibility(View.GONE);
        } else {
            mEmailError.setVisibility(View.GONE);
            mPasswordError.setVisibility(View.GONE);

            if (atleastOneAlpha) isPhone = false; // user entered email
            else isPhone = true;    // User entered phone number

            loginRequest(); // Hit Login API

        }
    }

    /*
    Facebook Login
     */
    private void fbLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email", "user_about_me", "user_birthday"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Profile profile = Profile.getCurrentProfile();

//                CommonMethods.getInstance().DisplayToast(mContext, "Login Success " + loginResult.getAccessToken().getUserId());
                CommonMethods.getInstance().e("", "FACEBOOK" + loginResult.getRecentlyGrantedPermissions());
                CommonMethods.getInstance().e("", "FACEBOOK USER ID: " + loginResult.getAccessToken().getUserId());

                facebookID = loginResult.getAccessToken().getUserId();
                // App code
                /*
                Hit API
                 */
                loginRequest();
            }

            @Override
            public void onCancel() {
                // App code
                CommonMethods.getInstance().DisplayToast(mContext, "User cancelled the request");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                CommonMethods.getInstance().DisplayToast(mContext, "Error Occured while Facebook Login");
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
//        CommonMethods.getInstance().e("","On Activity Result-> " + data.toString());
    }

    /*
    API for Login
     */
    private void loginRequest() {
        if (MobileConnectivity.checkNetworkConnections(mContext).isIntenetConnectionactive()) {
            DEVICEID = CommonMethods.getInstance().getDeviceId(mContext);
            if (DEVICEID == null) {
                CommonMethods.getInstance().DisplayToast(mContext, mContext.getResources().getString(R.string.error_something));
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
            //check for facebook id first
            if (facebookID != null && !facebookID.equalsIgnoreCase("")) {
                // user login with facebook
                param.put("contact_number", "");
                param.put("email", "");
                param.put("password", "");
                param.put("is_social", "1");
                param.put("social_id", facebookID);
            } else {
                // user login with email/phone
                if (isPhone) {
                    param.put("contact_number", mEmail_et.getText().toString());
                    param.put("email", "");
                } else {
                    param.put("contact_number", "");
                    param.put("email", mEmail_et.getText().toString());
                }
                param.put("password", mpassword_et.getText().toString());
                param.put("is_social", "0");
                param.put("social_id", "");
            }

            CommonMethods.getInstance().e("LOGIN PARAMS", "Param-> " + param.toString());

            APIHandler.getInstance(ConstantFile.APIURL).loginUser(param, this);
        } else {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_internet), rootView);
        }
    }


    @Override
    public void onSuccess(Response response, Retrofit retrofit, String tag) {
        try {
            if (mDialog != null) {
                if (mDialog.isShowing()) mDialog.dismiss();
            }
            if (response.isSuccessful()) {
                LoginResponse obj = (LoginResponse) response.body();
                if (obj.getStatus() == 0) {
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", rootView);
                } else if (obj.getStatus() == 1) {
//                    CommonMethods.getInstance().displaySnackBar(mContext, "Login Successfull" + "", mparent);
                    // Store infor into SharedPrefrences and navigate
                    SharedPreferencesHandler.setStringValues(mContext,mContext.getResources().getString(R.string.pref_userId),obj.getUser_id());
                    SharedPreferencesHandler.setStringValues(mContext,mContext.getResources().getString(R.string.pref_email),obj.getData().getEmail());
//                    SharedPreferencesHandler.setStringValues(mContext,mContext.getResources().getString(R.string.pref_role),obj.getData().getRole());
                    SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_phone), obj.getData().getContact_number());
                    SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_isdriver_requested), obj.getData().getBecome_driver_request());
                    SharedPreferencesHandler.setStringValues(mContext, mContext.getResources().getString(R.string.pref_isdriver_approved), obj.getData().getIs_driver_approved());

                    Intent intent = new Intent(mContext, PassangerDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else if (obj.getStatus() == 2) {
                    CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", rootView);
                    /*
                    User logged in with facebook for first time, navigate to OTP screen
                     */
                    Bundle bundle = new Bundle();
                    if (facebookID != null) {
                        // Login request through Facebook
                        bundle.putString("is_social", "1");
                        bundle.putString("facebook_id", facebookID);
                        bundle.putString("email", "");      // no need but managed for previous code
                        bundle.putString("phone", "");      // no need but managed for previous code
                        bundle.putString("password", "");   // no need but managed for previous code
                    }
                    ((BaseActivity) mContext).navigateToWithBundle(new OTPFragment(), bundle);

                } else {
                    if (obj.getMessage() != null) {
                        CommonMethods.getInstance().displaySnackBar(mContext, obj.getMessage() + "", rootView);
                    }
                }
            } else {
                CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.error_api), rootView);
            }
        } catch (Exception e) {
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
