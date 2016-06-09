package com.android.dezi.adapters;
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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.dezi.R;
import com.android.dezi.beans.SavedAddress;

import java.util.List;

/**
 * Created by Mobilyte on 4/7/2016.
 */
public class PlaceSavedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface SavedPlaceListener {
        public void onSavedPlaceClick(List<SavedAddress> mResponse, int position);
    }

    Context mContext;
    List<SavedAddress> mResponse;
    SavedPlaceListener mListener;

    public PlaceSavedAdapter(Context ctx, List<SavedAddress> response) {
        this.mContext = ctx;
        this.mResponse = response;
        this.mListener = (SavedPlaceListener) mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = null;
        RecyclerView.ViewHolder vh = null;
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_savedaddress, parent, false);
        vh = new HistoryViewHolder(rowView);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        HistoryViewHolder vh = (HistoryViewHolder) holder;
        vh.mAddress.setText(mResponse.get(position).getName());
        vh.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSavedPlaceClick(mResponse, position);
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
    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        //        CardView mCardView;
        public RelativeLayout mParentLayout;
        public TextView mAddress;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (RelativeLayout) itemView.findViewById(R.id.parent);
            mAddress = (TextView) itemView.findViewById(R.id.address);
        }
    }
}