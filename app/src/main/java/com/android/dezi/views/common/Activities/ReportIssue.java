package com.android.dezi.views.common.Activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.android.dezi.views.common.Fragments.ReportIssueFragment;

/**
 * Created by anuj.sharma on 5/12/2016.
 */
public class ReportIssue extends BaseActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ReportIssue.this;
        getSupportActionBar().setTitle("");
        mTitle.setText("REPORT AN ISSUE");
        mTitleImageview.setVisibility(View.GONE);
        /*
        Check User is Driver or Passanger and change Toolbar color
         */
        boolean isDriver = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriver));
        if (isDriver) {
            mTitle.setTextColor(CommonMethods.getInstance().getColor(mContext,R.color.black));
            modifyTolbar();
        }
        navigateTo(new ReportIssueFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDriverMode = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriver));
        boolean isDriverActive = SharedPreferencesHandler.getBooleanValues(mContext, mContext.getResources().getString(R.string.pref_isdriverActive));

        mShareMenuItem = menu.findItem(R.id.action_share);
        mToggleMenuItem = menu.findItem(R.id.action_toggle);
        if (isDriverMode) {
            //Driver mode
            mToggleMenuItem.setVisible(true);
            mShareMenuItem.setVisible(false);
            // Check if Driver is Active or not
            if (isDriverActive) {
                // Change Toggle icon green
                mToggleMenuItem.setIcon(ContextCompat.getDrawable(mContext, R.drawable.toggle_green));
            } else {
                //Change Toggle icon gray
                mToggleMenuItem.setIcon(ContextCompat.getDrawable(mContext, R.drawable.toggle_white));
            }
        } else {
            //Passanger Mode
            mToggleMenuItem.setVisible(false);
            mShareMenuItem.setVisible(true);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            //Shaer Action perform
        } else if (item.getItemId() == R.id.action_toggle) {
            toggleDriverProfile();
        }
        return true;

    }

    /*
   Modify Appearance of Toolbar for Driver Mode
    */
    private void modifyTolbar() {
        /*
        Set Toolbar Color for Driver Side
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackground(mContext.getResources().getDrawable(R.drawable.driver_toolbar_bg));
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        oneStepBack();
    }
}
