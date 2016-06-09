package com.android.dezi.views.passenger.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.dezi.BaseActivity;
import com.android.dezi.views.passenger.Fragments.AboutUsFragment;

public class AboutUsPassengerActivity extends BaseActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = AboutUsPassengerActivity.this;
        getSupportActionBar().setTitle("");
        mTitle.setText("ABOUT DEZI");
        mTitleImageview.setVisibility(View.GONE);

        ((BaseActivity) mContext).toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneStepBack();
            }
        });
        navigateTo(new AboutUsFragment());
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        oneStepBack();
    }
}