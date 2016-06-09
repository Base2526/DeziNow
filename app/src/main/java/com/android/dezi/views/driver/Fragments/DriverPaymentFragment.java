package com.android.dezi.views.driver.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.utility.CommonMethods;

/**
 * Created by anuj.sharma on 5/12/2016.
 */
public class DriverPaymentFragment extends Fragment {
    Context mContext;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_driver_payment,container,false);
        mContext = getActivity();
        ((BaseActivity)mContext).mTitle.setText("PAYMENTS");
        ((BaseActivity)mContext).mTitle.setTextColor(CommonMethods.getInstance().getColor(mContext, R.color.black));
        return rootView;
    }
}
