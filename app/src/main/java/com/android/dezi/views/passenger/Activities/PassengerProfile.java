package com.android.dezi.views.passenger.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.dezi.BaseActivity;
import com.android.dezi.views.passenger.Fragments.PassengerProfileFragment;

/**
 * Created by  on 11/5/16.
 */
public class PassengerProfile extends BaseActivity{
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigateTo(new PassengerProfileFragment());
        getSupportActionBar().setTitle("");
        mTitle.setText("PROFILE");
        mTitleImageview.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        oneStepBack();
    }
}