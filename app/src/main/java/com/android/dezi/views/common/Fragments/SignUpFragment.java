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
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.adapters.CountryAdapter;
import com.android.dezi.beans.CountryBean;
import com.android.dezi.customClasses.DialingCode;
import com.android.dezi.utility.CommonMethods;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mobilyte on 4/4/2016.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private View rootView = null;
    public static int isFacebookSignUp = 0;
    private CoordinatorLayout mparent;
    private TextView mEmailError, mPasswordError, mCnfrmPasswordError;
    ImageView mCountryFlag;
    private EditText mEmail_et, mpassword_et, mCnfrmPassword_et;
    private Button mSignUpBtn;
    private LinearLayout mFacebookBtn;
    private String facebookID = null;
    private String email = null;
    private String password;
    private CallbackManager callbackManager;

    Dialog dialog;
    List<CountryBean>mCountryList, searchedList;
    CountryAdapter mAdapter=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        mContext = getActivity();
        initView();
        initTextWatcher();
        initCountryList();
        // Get Country name
        TelephonyManager tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String Sim_ISO = tm.getSimCountryIso().toUpperCase();
//        String locale = mContext.getResources().getConfiguration().locale.getCountry();
        CommonMethods.getInstance().e("","Country-> " + Sim_ISO);
        return rootView;
    }
    /*
    Initialize Views
     */
    private void initView() {
        callbackManager = CallbackManager.Factory.create();
        mparent = (CoordinatorLayout) rootView.findViewById(R.id.parent);

        mEmail_et = (EditText) rootView.findViewById(R.id.signup_email);
        mpassword_et = (EditText) rootView.findViewById(R.id.signup_password);
        mCnfrmPassword_et = (EditText) rootView.findViewById(R.id.signup_confirmpassword);
        mEmailError = (TextView) rootView.findViewById(R.id.error_email);
        mPasswordError = (TextView) rootView.findViewById(R.id.error_password);
        mCnfrmPasswordError = (TextView) rootView.findViewById(R.id.error_cnfrmpassword);
        mSignUpBtn = (Button) rootView.findViewById(R.id.signup_btn);
        mFacebookBtn = (LinearLayout) rootView.findViewById(R.id.btn_facebook);

        mSignUpBtn.setOnClickListener(this);
        mFacebookBtn.setOnClickListener(this);

        /*
        Get Current Country name and set it by Default
         */
        String locale = mContext.getResources().getConfiguration().locale.getCountry();
    }

    private void initTextWatcher() {
        mEmail_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString();
                if (email != null) {
                    if (CommonMethods.getInstance().validateEmail(email)) {
                        mEmailError.setVisibility(View.GONE);
                    } else {
                        mEmailError.setText("Please Enter Valid Email");
                        mEmailError.setVisibility(View.VISIBLE);
                    }
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
                        mPasswordError.setText("Please enter Password");
                        mPasswordError.setVisibility(View.VISIBLE);
                    } else if (!CommonMethods.getInstance().isPasswordValid(password)) {
                        mPasswordError.setText(mContext.getResources().getString(R.string.validation_password_length));
                        mPasswordError.setVisibility(View.VISIBLE);
                    } else if (password.length() >= 4) {
                        mPasswordError.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /*
    Initialize Country List data
     */
    private void initCountryList(){
        if(mCountryList== null )mCountryList = new ArrayList<>();
        if(mCountryList.size()>0)mCountryList.clear();
        CountryBean obj;
        String[] l = Locale.getISOCountries();
        System.out.println(l.length);

        for (int i = 0; i < l.length; i++) {
            obj = new CountryBean();
            String localeCode = l[i];
            Locale ll = new Locale("", localeCode);
            String countryCode = DialingCode.getPhone(l[i]);
            String countryName = ll.getDisplayCountry();
            if (l[i].equalsIgnoreCase("do")) {
                l[i] = "do1";
            }
            int resID = mContext.getResources().getIdentifier(l[i].toLowerCase(),
                    "drawable", mContext.getPackageName());

            obj.setFlag(resID);
            obj.setCountryCode(countryCode);
            obj.setCountryName(countryName);

            mCountryList.add(obj);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mFacebookBtn) {
            //Initiate Facebook Login
            fbLogin();
        } else if (v == mSignUpBtn) {
            checkSignup();
        }
    }

    /*
    Show Countries Dialog
     */
    private void showCountriesDialog(){

        if(dialog==null){
            dialog = new Dialog(mContext);
            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);   //Remove Title Area
            dialog.setContentView(R.layout.dialog_country);

//            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//            lp.copyFrom(dialog.getWindow().getAttributes());
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setAttributes(lp);
        }

        // set values for custom dialog components - text, image and button
        final EditText searchEt = (EditText) dialog.findViewById(R.id.search_country_et);
        final RecyclerView mRecyclerView = (RecyclerView)dialog.findViewById(R.id.list_country);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);
        if(mAdapter==null){
            mAdapter = new CountryAdapter(mContext,mCountryList,this);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }

        dialog.show();

        /*
        Search Edittext
         */
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchedList = new ArrayList<CountryBean>();
                String countryName = s.toString();
                if(countryName!=null && countryName.length()>0){
                    for(CountryBean bean: mCountryList){
                        if(bean.getCountryName().toLowerCase(Locale.ENGLISH).contains(countryName.toLowerCase(Locale.ENGLISH))){
                            searchedList.add(bean);
                        }
                    }

                    if(mAdapter == null){
                        mAdapter = new CountryAdapter(mContext, searchedList, SignUpFragment.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        mAdapter.updateList(searchedList);
                    }
                }else{
                    if(mAdapter == null){
                        mAdapter = new CountryAdapter(mContext, mCountryList, SignUpFragment.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        mAdapter.updateList(mCountryList);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setCountryCode(CountryBean obj){
        if(obj!=null){
            mCountryFlag.setImageResource(obj.getFlag());
            if(dialog!=null)dialog.dismiss();
        }
    }

    /*
        Check Signup
     */
    private void checkSignup() {
        String confirmpassword;
        email = mEmail_et.getText().toString().trim();
        password = mpassword_et.getText().toString();
        confirmpassword = mCnfrmPassword_et.getText().toString();

        if (email.length() == 0) {
            mEmailError.setText(mContext.getResources().getString(R.string.validation_email));
            mEmailError.setVisibility(View.VISIBLE);
        } else if (!CommonMethods.getInstance().validateEmail(email)) {
            mEmailError.setText(mContext.getResources().getString(R.string.validation_valid_email));
            mEmailError.setVisibility(View.VISIBLE);
        }
        else if (password.length() == 0) {
            mPasswordError.setText(mContext.getResources().getString(R.string.validation_password));
            mPasswordError.setVisibility(View.VISIBLE);

            mEmailError.setVisibility(View.GONE);
        }
        else if (!CommonMethods.getInstance().isPasswordValid(password)) {
            mPasswordError.setText(mContext.getResources().getString(R.string.validation_password_length));
            mPasswordError.setVisibility(View.VISIBLE);

            mEmailError.setVisibility(View.GONE);
        } else if (confirmpassword.length() == 0) {
            mCnfrmPasswordError.setText(mContext.getResources().getString(R.string.validation_confirm_password));
            mCnfrmPasswordError.setVisibility(View.VISIBLE);

            mEmailError.setVisibility(View.GONE);
            mPasswordError.setVisibility(View.GONE);
        } else if (!password.equals(confirmpassword)) {
            CommonMethods.getInstance().displaySnackBar(mContext, mContext.getResources().getString(R.string.validation_match_password), rootView);

//            Snackbar.make(getView(), mContext.getResources().getString(R.string.validation_match_password), Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();

            mEmailError.setVisibility(View.GONE);
            mPasswordError.setVisibility(View.GONE);
            mCnfrmPasswordError.setVisibility(View.GONE);
        } else {
            mEmailError.setVisibility(View.GONE);
            mPasswordError.setVisibility(View.GONE);
            mCnfrmPasswordError.setVisibility(View.GONE);

            signUpRequest();    // Proceed SIGNUP Request
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
//                CommonMethods.getInstance().e("", "USER FIRSTNAME " + profile.getFirstName());
//                CommonMethods.getInstance().e("", "USER LASTNAME " + profile.getLastName());
//                CommonMethods.getInstance().e("", "USER PROFILEPIC " + profile.getProfilePictureUri(300,300).toString());

                facebookID = loginResult.getAccessToken().getUserId();
                /*
                Hit API
                 */
                signUpRequest();
            }

            @Override
            public void onCancel() {
                // App code
                CommonMethods.getInstance().DisplayToast(mContext, "User cancelled the request");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                CommonMethods.getInstance().DisplayToast(mContext, "Error Occured");
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
    HIT API for Signup
     */
    private void signUpRequest() {
        Bundle bundle = new Bundle();
        if (facebookID != null) {
            // Login request through Facebook
            bundle.putString("is_social", "1");
            bundle.putString("facebook_id", facebookID);
            bundle.putString("email", "");
            bundle.putString("password", "");
            isFacebookSignUp = 0;
        } else {
            // Login request through Email/password
            bundle.putString("is_social", "0");
            bundle.putString("facebook_id", "");
            bundle.putString("email", email);
            bundle.putString("password", password);
            isFacebookSignUp = 1;
        }
        ((BaseActivity) mContext).navigateToWithBundle(new OTPFragment(), bundle);
    }
}
