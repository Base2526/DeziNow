package com.android.dezi.views.passenger.Activities;

import android.os.Bundle;
import android.view.View;

import com.android.dezi.BaseActivity;
import com.android.dezi.views.passenger.Fragments.PassengerReferralHistoryFragment;

public class ReferralHistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigateTo(new PassengerReferralHistoryFragment());
        getSupportActionBar().setTitle("");
        mTitle.setText("REFERRAL HISTORY");
        mTitleImageview.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        oneStepBack();
    }
}
