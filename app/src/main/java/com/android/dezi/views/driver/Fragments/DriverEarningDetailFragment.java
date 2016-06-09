package com.android.dezi.views.driver.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.views.common.Fragments.ReportIssueFragment;

/**
 * Created by anuj.sharma on 5/12/2016.
 */
public class DriverEarningDetailFragment extends Fragment implements View.OnClickListener{
    Context mContext;
    View rootView;

    TextView mReportIssueBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_driver_earning_detail,container,false);
        mContext = getActivity();
        ((BaseActivity)mContext).mTitle.setText("EARNING DETAIL");
        ((BaseActivity)mContext).mTitle.setTextColor(CommonMethods.getInstance().getColor(mContext, R.color.black));
        initView();
        return rootView;
    }

    /*
    Initialize Views
     */
    private void initView() {
        mReportIssueBtn = (TextView)rootView.findViewById(R.id.report_issue);

        mReportIssueBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.report_issue:
                ((BaseActivity)mContext).mTitle.setText("REPORT AN ISSUE");
                ((BaseActivity)mContext).navigateTo(new ReportIssueFragment());
                break;
        }
    }
}
