package com.android.dezi.views.driver.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.adapters.RefferalHistroyComplete;
import com.android.dezi.adapters.RefferalHistroyInComplete;
import com.android.dezi.beans.DriverReward.RewardChildModel;
import com.android.dezi.beans.DriverReward.RewardParentModel;
import com.android.dezi.utility.CommonMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anuj.sharma on 5/12/2016.
 */
public class DriverRewardFragment extends Fragment {
    private Context mContext;
    private View rootView, headerView;

    private TextView mTierLevel, mHour, mAcceptanceRate, mCancellationRate;



    ExpandableListView mExpndbleList;
    List<RewardParentModel> mList;
    RefferalHistroyComplete mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_driver_rewards, container, false);
        mContext = getActivity();
        ((BaseActivity)mContext).mTitle.setText("REWARD");
        ((BaseActivity)mContext).mTitle.setTextColor(CommonMethods.getInstance().getColor(mContext, R.color.black));
        headerView = inflater.inflate(R.layout.header_driver_reward,null);
        initView();
        addData();
        mExpndbleList.addHeaderView(headerView);
        mAdapter=new RefferalHistroyComplete(getActivity(),mList,mExpndbleList);
        mExpndbleList.setAdapter(mAdapter);
        return rootView;
    }

    /*
    Initialize Views
     */
    private void initView() {
        mExpndbleList = (ExpandableListView) rootView.findViewById(R.id.driver_expandableListRefferal);

        /*
        initialize header components
         */
        mTierLevel = (TextView)headerView.findViewById(R.id.reward_tier_level);
        mHour = (TextView)headerView.findViewById(R.id.reward_hour);
        mAcceptanceRate = (TextView)headerView.findViewById(R.id.reward_acceptance_rate);
        mCancellationRate = (TextView)headerView.findViewById(R.id.reward_cancellation_rate);
    }

    private void addData() {
        mList = new ArrayList<>();

        RewardParentModel parentObj;
        RewardChildModel childObj;
        List<RewardChildModel> child;

        //First item
        parentObj = new RewardParentModel();
        parentObj.setHeading("REFERRAL COMPLETED");

        child = new ArrayList<>();

        childObj = new RewardChildModel();
        childObj.setName("TOM CRUISE");
        childObj.setStatus("COMPLETED");
        child.add(childObj);

        childObj = new RewardChildModel();
        childObj.setName("JUSTIN HENDRICK");
        childObj.setStatus("COMPLETED");
        child.add(childObj);

        parentObj.setChildList(child);
        mList.add(parentObj);

        //Second item
        parentObj = new RewardParentModel();
        parentObj.setHeading("REFERRAL INCOMPLETED");

        child = new ArrayList<>();

        childObj = new RewardChildModel();
        childObj.setName("JOHN DOE");
        childObj.setStatus("INCOMPLETE");
        child.add(childObj);

        childObj = new RewardChildModel();
        childObj.setName("MISSY KIM");
        childObj.setStatus("INCOMPLETE");
        child.add(childObj);

        parentObj.setChildList(child);
        mList.add(parentObj);
    }
}
