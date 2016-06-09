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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.adapters.PlaceAutocompleteAdapter;
import com.android.dezi.adapters.PlaceAutocompleteAdapter.PlaceAutoCompleteInterface;
import com.android.dezi.adapters.PlaceSavedAdapter;
import com.android.dezi.adapters.PlaceSavedAdapter.SavedPlaceListener;
import com.android.dezi.beans.SavedAddress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mobilyte on 4/6/2016.
 */
public class SearchActivity extends BaseActivity implements PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,OnClickListener,SavedPlaceListener {
    Context mContext;
    GoogleApiClient mGoogleApiClient;
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3000;

    LinearLayout mParent;
    private RecyclerView mRecyclerView;
    LinearLayoutManager llm;
    PlaceAutocompleteAdapter mAdapter;
    List<SavedAddress> mSavedAddressList;
    PlaceSavedAdapter mSavedAdapter;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    EditText mSearchEdittext;
    ImageView mClear;

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mContext = SearchActivity.this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        initViews();

        if(mSavedAddressList==null) mSavedAddressList = new ArrayList<>();
        /*
        Save address for test purpose
         */
        mSavedAddressList.add(new SavedAddress("Home",	30.446484,77.033943 ));
        mSavedAddressList.add(new SavedAddress("Work", 30.695202, 76.854172));
        if(mSavedAddressList.size()>0){
            mSavedAdapter = new PlaceSavedAdapter(mContext,mSavedAddressList);
                mRecyclerView.setAdapter(mSavedAdapter);
        }
//        navigateTo(new SearchFragment());
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ((BaseActivity)mContext).oneStepBack();
    }

    /*
   Initialize Views
    */
    private void initViews(){
        mRecyclerView = (RecyclerView)findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);

        mSearchEdittext = (EditText)findViewById(R.id.search_et);
        mClear = (ImageView)findViewById(R.id.clear);
        mClear.setOnClickListener(this);

        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.view_placesearch,
                mGoogleApiClient, BOUNDS_INDIA, null);
        mRecyclerView.setAdapter(mAdapter);

        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mClear.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    mClear.setVisibility(View.GONE);
                    if (mSavedAdapter != null && mSavedAddressList.size() > 0) {
                        mRecyclerView.setAdapter(mSavedAdapter);
                    }
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v == mClear){
            mSearchEdittext.setText("");
            if(mAdapter!=null){
                mAdapter.clearList();
            }
           if(mSavedAdapter!=null && mSavedAddressList.size()>0){
               mRecyclerView.setAdapter(mSavedAdapter);
           }

        }
    }



    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, int position) {
        if(mResultList!=null){
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if(places.getCount()==1){
                            //Do the things here on Click.....
                            Intent data = new Intent();
                            data.putExtra("lat",String.valueOf(places.get(0).getLatLng().latitude));
                            data.putExtra("lng", String.valueOf(places.get(0).getLatLng().longitude));
                            setResult(SearchActivity.RESULT_OK, data);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (Exception e){

            }

        }
    }

    @Override
    public void onSavedPlaceClick(List<SavedAddress> mResponse, int position) {
        if(mResponse!=null){
            try {
                Intent data = new Intent();
                data.putExtra("lat",String.valueOf(mResponse.get(position).getLatitude()));
                data.putExtra("lng", String.valueOf(mResponse.get(position).getLongitude()));
                setResult(SearchActivity.RESULT_OK, data);
                finish();
            }
            catch (Exception e){

            }

        }
    }
}
