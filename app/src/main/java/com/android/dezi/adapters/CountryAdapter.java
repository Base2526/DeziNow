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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dezi.R;
import com.android.dezi.beans.CountryBean;
import com.android.dezi.customClasses.DialingCode;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.views.common.Fragments.SignUpFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mobilyte on 4/13/2016.
 */
public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<CountryBean> mResponse;
    SignUpFragment mFragment;

    public CountryAdapter(Context ctx, List<CountryBean> response, SignUpFragment fragment) {
        this.mContext = ctx;
        this.mResponse = response;
        this.mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = null;
        RecyclerView.ViewHolder vh = null;
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_country_list, parent, false);
        vh = new CountryViewHolder(rowView);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CountryViewHolder vh = (CountryViewHolder) holder;
        final CountryBean obj = mResponse.get(position);
        vh.mCountryFlag.setImageResource(obj.getFlag());
        vh.mCountryname.setText(obj.getCountryName());
        vh.mCountryCode.setText(obj.getCountryCode());
        vh.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.setCountryCode(obj);
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
    public class CountryViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mParentLayout;
        public ImageView mCountryFlag;
        public TextView mCountryname, mCountryCode;

        public CountryViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (LinearLayout) itemView.findViewById(R.id.parent);
            mCountryFlag = (ImageView) itemView.findViewById(R.id.view_countryflag);
            mCountryname = (TextView) itemView.findViewById(R.id.view_countryname);
            mCountryCode = (TextView) itemView.findViewById(R.id.view_countrycode);
        }
    }

    public void updateList(List<CountryBean> list) {
        this.mResponse = list;
        notifyDataSetChanged();
    }
}