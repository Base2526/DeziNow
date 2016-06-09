package com.android.dezi.views.passenger.Activities;

import android.os.Bundle;
import android.view.View;

import com.android.dezi.BaseActivity;
import com.android.dezi.views.passenger.Fragments.ReportIssueFragment;

public class ReportIssueActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigateTo(new ReportIssueFragment());
        getSupportActionBar().setTitle("");
        mTitle.setText("REPORT AN ISSUE");
        mTitleImageview.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        oneStepBack();
    }
}
