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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dezi.R;
import com.android.dezi.beans.CountryBean;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.views.common.Fragments.SignUpFragment;
import com.android.dezi.views.passenger.Fragments.PaymentFragment;
import com.stripe.android.model.Token;

import java.util.List;

/**
 * Created by Mobilyte on 4/20/2016.
 */
public class SavedCardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<Token> mResponse;
    PaymentFragment mFragment;

    public SavedCardsAdapter(Context ctx, List<Token> response, PaymentFragment fragment) {
        this.mContext = ctx;
        this.mResponse = response;
        this.mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = null;
        RecyclerView.ViewHolder vh = null;
        rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_saved_cards, parent, false);
        vh = new CountryViewHolder(rowView);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CountryViewHolder vh = (CountryViewHolder) holder;
        vh.mCardNumber.setText("XXXXXXXXXXXX" + mResponse.get(position).getCard().getLast4());
        String cardType = mResponse.get(position).getCard().getType();
        CommonMethods.getInstance().e("CARD", "CARD TYPE-> " + cardType);
        vh.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.initiatePayment(mResponse.get(position));
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
        //        CardView mCardView;
        public LinearLayout mParentLayout;
        public ImageView mCardType;
        public TextView mCardNumber;

        public CountryViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (LinearLayout) itemView.findViewById(R.id.parent);
            mCardType = (ImageView) itemView.findViewById(R.id.view_cardtype);
            mCardNumber = (TextView) itemView.findViewById(R.id.view_card_number);
        }
    }

    public void updateList(List<Token> list) {
        this.mResponse = list;
        notifyDataSetChanged();
    }
}