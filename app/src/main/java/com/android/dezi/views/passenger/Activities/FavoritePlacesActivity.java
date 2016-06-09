package com.android.dezi.views.passenger.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.dezi.BaseActivity;
import com.android.dezi.views.passenger.Fragments.FavouritPlacesFragment;

public class FavoritePlacesActivity extends BaseActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigateTo(new FavouritPlacesFragment());
        getSupportActionBar().setTitle("");
        mTitle.setText("FARORITE PLACES");
        mTitleImageview.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        oneStepBack();
    }
}
