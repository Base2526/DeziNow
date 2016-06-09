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
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.views.common.Activities.LoginActivity;

/**
 * Created by Mobilyte on 4/8/2016.
 */
public class ChooseLoginFragment extends Fragment implements View.OnClickListener{
    Context mContext;
    View rootView = null;

    Button mLoginBtn, mSignUpBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login_choose, container, false);
        mContext = getActivity();
        initView();
        return rootView;
    }

    /*
    Initialize Views
     */
    private void initView(){
        mLoginBtn = (Button)rootView.findViewById(R.id.btn_login);
        mSignUpBtn = (Button)rootView.findViewById(R.id.btn_signup);
        // Click Listeners
        mLoginBtn.setOnClickListener(this);
        mSignUpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mLoginBtn){
            ((BaseActivity)mContext).navigateTo(new LoginFragment());
        }
        else if(v == mSignUpBtn){
            ((BaseActivity)mContext).navigateTo(new SignUpFragment());
        }
    }
}
