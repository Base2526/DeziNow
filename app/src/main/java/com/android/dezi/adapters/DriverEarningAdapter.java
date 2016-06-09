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
import com.android.dezi.views.driver.Fragments.DriverEarningsFragment;

import java.util.List;

/**
 * Created by anuj.sharma on 5/12/2016.
 */
public class DriverEarningAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<TripHistoryBean> mResponse;
    DriverEarningsFragment mFragment;


    public DriverEarningAdapter(Context ctx,List<TripHistoryBean> response, DriverEarningsFragment fragment){
        this.mContext = ctx;
        this.mResponse = response;
        this.mFragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = null;
        RecyclerView.ViewHolder vh = null;
        rowView= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_driver_earnings, parent, false);
        vh = new DriverEarningViewHolder(rowView);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DriverEarningViewHolder vh = (DriverEarningViewHolder)holder;

        vh.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.onDriverEarningItemClick();
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
    public class DriverEarningViewHolder extends RecyclerView.ViewHolder {
        //        CardView mCardView;
        public LinearLayout mParentLayout;
        public TextView mOrderNo, mPrice, mStatus, mTime, mPassengername;

        public DriverEarningViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (LinearLayout)itemView.findViewById(R.id.parent);
            mOrderNo = (TextView)itemView.findViewById(R.id.order_number);
            mPrice = (TextView)itemView.findViewById(R.id.price);
            mStatus = (TextView)itemView.findViewById(R.id.status);
            mTime = (TextView)itemView.findViewById(R.id.time);
            mPassengername = (TextView)itemView.findViewById(R.id.passanger_name);
        }

    }
}
