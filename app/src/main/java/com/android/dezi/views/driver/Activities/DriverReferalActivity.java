package com.android.dezi.views.driver.Activities;

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
import com.android.dezi.views.driver.Fragments.DriverReferalFragment;

/**
 * Created by Mobilyte on 5/12/2016.
 */
public class DriverReferalActivity extends BaseActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = DriverReferalActivity.this;
        modifyTolbar();
        navigateTo(new DriverReferalFragment());
    }

    /*
Modify Appearance of Toolbar for Driver Mode
 */
    private void modifyTolbar() {
        /*
        Set Toolbar Color for Driver Side
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackground(ContextCompat.getDrawable(mContext,R.drawable.driver_toolbar_bg));
        }
        getSupportActionBar().setTitle("");
        mTitleImageview.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        oneStepBack();
    }

}