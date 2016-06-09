package com.android.dezi.views.passenger.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.android.dezi.BaseActivity;
import com.android.dezi.R;
import com.android.dezi.adapters.PaymentExpandableAdapter;
import com.android.dezi.beans.passenger_payment.PaymentChildModel;
import com.android.dezi.beans.passenger_payment.PaymentParentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 12/5/16.
 */
public class PassangerPaymentFragment extends Fragment implements View.OnClickListener{
    Context mcontext;
    Button mAddCard;
    private View rootView, footerView;

    ExpandableListView mExpandableList;
    List<PaymentParentModel> mList;
    PaymentExpandableAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_passenger_payment, container, false);
        mcontext = getActivity();
        ((BaseActivity)mcontext).mTitle.setText("PAYMENT");
        footerView = inflater.inflate(R.layout.footer_passenger_payment,null);
        initView();
        addData();
        mExpandableList.addFooterView(footerView);
        mAdapter=new PaymentExpandableAdapter(getActivity(),mList);
        mExpandableList.setAdapter(mAdapter);
        return rootView;
    }

    private void initView() {
        mExpandableList = (ExpandableListView) rootView.findViewById(R.id.credit_expandablelist);
        // Initialize Footer Components
        mAddCard= (Button) footerView.findViewById(R.id.add_card);
        mAddCard.setOnClickListener(this);
    }

    private void addData() {
        mList = new ArrayList<>();

        PaymentParentModel parentObj;
        PaymentChildModel childObj;
        List<PaymentChildModel> child;

        //First item
        parentObj = new PaymentParentModel();
        parentObj.setPaymentType("PAYPAL");

        child = new ArrayList<>();

        childObj = new PaymentChildModel();
        childObj.setCardnumber("PERSONAL ****1964");
        childObj.setCartType("VISA");
        child.add(childObj);

        childObj = new PaymentChildModel();
        childObj.setCardnumber("PERSONAL ****8534");
        childObj.setCartType("VISA");
        child.add(childObj);

        parentObj.setChildList(child);
        mList.add(parentObj);

        //Second item
        parentObj = new PaymentParentModel();
        parentObj.setPaymentType("CREDIT CARDS");

        child = new ArrayList<>();

        childObj = new PaymentChildModel();
        childObj.setCardnumber("PERSONAL ****1234");
        childObj.setCartType("VISA");
        child.add(childObj);

        childObj = new PaymentChildModel();
        childObj.setCardnumber("PERSONAL ****5434");
        childObj.setCartType("VISA");
        child.add(childObj);

        parentObj.setChildList(child);
        mList.add(parentObj);
    }

    @Override
    public void onClick(View v) {
        if (v== mAddCard){
            ((BaseActivity)mcontext).navigateTo(new AddNewCardFragment());
            ((BaseActivity)mcontext).mTitle.setText("ADD PAYMENT");
            ((BaseActivity)mcontext). mTitleImageview.setVisibility(View.GONE);
        }

    }
}