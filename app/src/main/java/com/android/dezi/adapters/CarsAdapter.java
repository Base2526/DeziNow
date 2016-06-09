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

package com.android.dezi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dezi.R;
import com.android.dezi.beans.TripHistoryBean;
import com.android.dezi.views.passenger.Fragments.PassengerCarsFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mobilyte Inc. on 5/11/2016.
 */
public class CarsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<TripHistoryBean> mResponse;
    PassengerCarsFragment mFragment;


    public CarsAdapter(Context ctx,List<TripHistoryBean> response, PassengerCarsFragment fragment){
        this.mContext = ctx;
        this.mResponse = response;
        this.mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = null;
        RecyclerView.ViewHolder vh = null;
        rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cars, parent, false);
        vh = new CarViewHolder(rowView);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CarViewHolder vh = (CarViewHolder)holder;

        vh.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mFragment.onTripHistoryItemClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResponse.size();
    }

    /*
    View Holder For Trip History
     */
    public class CarViewHolder extends RecyclerView.ViewHolder {
        //        CardView mCardView;
        public LinearLayout mParentLayout;
        public CircleImageView mCarImage;
        public TextView mTime, mCarname, mCarNumber;

        public CarViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (LinearLayout)itemView.findViewById(R.id.parent);
            mCarImage = (CircleImageView)itemView.findViewById(R.id.car_img);
            mCarname = (TextView)itemView.findViewById(R.id.car_name);
            mCarNumber = (TextView)itemView.findViewById(R.id.card_number);
        }

    }
}
