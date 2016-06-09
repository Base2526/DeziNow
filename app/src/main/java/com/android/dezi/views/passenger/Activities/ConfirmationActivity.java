package com.android.dezi.views.passenger.Activities;
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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.views.passenger.Fragments.ConfirmFragment;

/**
 * Created by Mobilyte on 3/30/2016.
 */
public class ConfirmationActivity extends BaseActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ConfirmationActivity.this;
        ((BaseActivity)mContext).toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        ((BaseActivity)mContext).toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)mContext).oneStepBack();
            }
        });
        /*
        Receive the PickUp Address
         */
        String pickupAddress;
        try {
            pickupAddress = getIntent().getStringExtra("pickup_location");
            if(pickupAddress!=null){
                Bundle bundle = new Bundle();
                bundle.putString("pickup_location",pickupAddress);
                CommonMethods.getInstance().e("","PickUp Location-> " + pickupAddress);
                navigateToWithBundle(new ConfirmFragment(),bundle);
            }
            else{
                CommonMethods.getInstance().DisplayToast(mContext,"PickUp Location not Found, Try Again");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
//        navigateTo(new ConfirmFragment());
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ((BaseActivity)mContext).oneStepBack();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

        /*
        Model for isChangeDestination
         */
    public Boolean isChangeDestination;
    public Boolean getIsChangeDestination() {
        return isChangeDestination;
    }

    public void setIsChangeDestination(Boolean isChangeDestination) {
        this.isChangeDestination = isChangeDestination;
    }
}
