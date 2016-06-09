package com.android.dezi.views.passenger.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.dezi.R;

/**
 * Created by  on 12/5/16.
 */
public class FavouritPlacesFragment extends Fragment {
    private View rootView;
    Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favourite_places, container, false);
        mContext = getActivity();

        return rootView;
    }
}
