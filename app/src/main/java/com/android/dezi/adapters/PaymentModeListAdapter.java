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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dezi.R;
import com.android.dezi.beans.PassangerRideInfoResponse;
import com.android.dezi.views.passenger.Fragments.ConfirmFragment;

/**
 * Created by Mobilyte on 4/28/2016.
 */
public class PaymentModeListAdapter extends BaseAdapter {
    Context mContext;
    PassangerRideInfoResponse mResponse;
    ConfirmFragment mFragment;

    public PaymentModeListAdapter(Context ctx, PassangerRideInfoResponse response, ConfirmFragment fragment) {
        this.mContext = ctx;
        this.mResponse = response;
        this.mFragment = fragment;
    }

    @Override
    public int getCount() {
        return mResponse.getData().getPayment_methods().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PaymentModeViewHolder countryViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_carlist, parent, false);
            countryViewHolder = new PaymentModeViewHolder();
            countryViewHolder.mParentLayout = (LinearLayout) convertView.findViewById(R.id.parent);
            countryViewHolder.mCardname = (TextView) convertView.findViewById(R.id.car_name);
            countryViewHolder.mId = (TextView) convertView.findViewById(R.id.id);
            convertView.setTag(countryViewHolder);
        } else {
            countryViewHolder = (PaymentModeViewHolder) convertView.getTag();
        }
        countryViewHolder.mCardname.setText(mResponse.getData().getPayment_methods().get(position).getAccount_type() + "");
        countryViewHolder.mId.setText(mResponse.getData().getPayment_methods().get(position).getId() + "");
        return convertView;
    }

    /*
    View Holder For Trip History
     */
    class PaymentModeViewHolder {
        public LinearLayout mParentLayout;
        public TextView mCardname, mId;
    }
}