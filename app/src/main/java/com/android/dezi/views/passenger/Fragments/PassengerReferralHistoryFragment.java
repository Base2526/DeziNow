package com.android.dezi.views.passenger.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.android.dezi.adapters.RefferalHistroyComplete;
import com.android.dezi.R;
import com.android.dezi.adapters.RefferalHistroyInComplete;
import com.android.dezi.beans.DriverReward.RewardChildModel;
import com.android.dezi.beans.DriverReward.RewardParentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by  on 12/5/16.
 */
public class PassengerReferralHistoryFragment extends Fragment {
    Context mContext;
    View rootView;

    ExpandableListView mExpndbleList;
    List<RewardParentModel> mList;
    RefferalHistroyComplete mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_passenger_referral_history, container, false);
        mContext = getActivity();
        initView();
        addData();
        mAdapter=new RefferalHistroyComplete(getActivity(),mList,mExpndbleList);
        mExpndbleList.setAdapter(mAdapter);

        return rootView;

    }

    /*
    Initialize Views
     */
    private void initView() {
        mExpndbleList = (ExpandableListView) rootView.findViewById(R.id.passenger_refereal_expandableList);
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
