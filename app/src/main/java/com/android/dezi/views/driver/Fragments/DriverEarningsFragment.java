package com.android.dezi.views.driver.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.adapters.DriverEarningAdapter;
import com.android.dezi.beans.TripHistoryBean;
import com.android.dezi.utility.CommonMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anuj.sharma on 5/12/2016.
 */
public class DriverEarningsFragment extends Fragment {
    Context mContext;
    View rootView;

    RecyclerView mRecyclerView;
    DriverEarningAdapter mAdapter = null;

    LinearLayoutManager llm;
    private boolean loading = true;
    int count = 20;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_driver_earnings, container, false);
        mContext = getActivity();
        ((BaseActivity)mContext).mTitle.setText("EARNINGS");
        ((BaseActivity)mContext).mTitle.setTextColor(CommonMethods.getInstance().getColor(mContext,R.color.black));
        initViews();
        initRecyclerView();
        loadData();
        return rootView;
    }

    /*
    Initialize Views
     */
    private void initViews(){
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.list_driver_earning);
    }
    /*
    Initialize RecyclerView
     */
    private void initRecyclerView(){
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);
       /*
        Add Scrolling Listener in RecyclerView
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();
                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        CommonMethods.getInstance().e("...", "Last Item Wow !");
                        count = count + 20;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    /*
    Load Data
     */
    private void loadData(){
        List<TripHistoryBean> listdata = new ArrayList<>();
        for(int i=0;i<3;i++){
            TripHistoryBean obj = new TripHistoryBean();
            obj.setId(i + "");
            obj.setDate("03/15");
            obj.setTime("8:47 PM");
            obj.setMappic("");
            obj.setPrice("Rs 120");
            listdata.add(obj);
        }
        mAdapter = new DriverEarningAdapter(mContext,listdata, DriverEarningsFragment.this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*
    On Click history Items
     */
    public void onDriverEarningItemClick(){
        ((BaseActivity)mContext).navigateTo(new DriverEarningDetailFragment());
    }

}
