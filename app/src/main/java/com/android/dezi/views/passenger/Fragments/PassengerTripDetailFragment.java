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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.views.passenger.Activities.TripHistoryActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mobilyte Inc. on 5/11/2016.
 */
public class PassengerTripDetailFragment extends Fragment implements View.OnClickListener{
    Context mContext;
    View rootView;

    CircleImageView mProfile;
    TextView mReportIssueBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_trip_detail,container,false);
        mContext = getActivity();
        ((TripHistoryActivity) mContext).mTitle.setText("TRIP DETAIL");
        initView();
        return rootView;
    }

    /*
    Initialize Views
     */
    private void initView(){
        mProfile = (CircleImageView)rootView.findViewById(R.id.profile);
        mReportIssueBtn = (TextView)rootView.findViewById(R.id.report_issue);

        mReportIssueBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.report_issue:
                ((BaseActivity)mContext).navigateTo(new ReportIssueFragment());
                break;
        }
    }
}
