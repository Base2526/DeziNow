package com.android.dezi.views.passenger.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;

/**
 * Created by  on 11/5/16.
 */
public class PassengerProfileFragment extends Fragment {
    private View rootView;
    Button mLogoutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_passenger_profile, container, false);
        mLogoutBtn = (Button)rootView.findViewById(R.id.logout);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).logout();
            }
        });
        return rootView;
    }
}
