package com.android.dezi.views.common.Activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;

public class AboutUs extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about_us);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView)toolbar.findViewById(R.id.title);

        mTitle.setText("ABOUT DEZI");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }
}
