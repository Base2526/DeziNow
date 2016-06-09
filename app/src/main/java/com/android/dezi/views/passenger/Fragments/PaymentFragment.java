package com.android.dezi.views.passenger.Fragments;
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
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.dezi.R;
import com.android.dezi.adapters.SavedCardsAdapter;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mobilyte on 4/19/2016.
 */
public class PaymentFragment extends Fragment implements View.OnClickListener{
    Context mContext;
    View rootView = null;
    ProgressDialog mDialog = null;

    Button mAddCardBtn;
    EditText mCardNumber, mCardExpireMonth, mCardExpireYear,mCardCVV;
    Dialog dialog;

    // Stripe Related Variables
    List<Token> mStripeCardsList = new ArrayList<>();
    SavedCardsAdapter mAdapter=null;
    RecyclerView mRecyclerView;

    //Paypal Related Variables
    Button mPaypalBtn;
    String PAYPAL_CLIENT_TOKEN = "";
    int PAYPAL_REQUEST_CODE = 1001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_payment,container,false);
        mContext = getActivity();
        initView();
        initRecyclerView();
        return rootView;
    }

    /*
    Initialize Views
     */
    private void initView(){
        mCardNumber = (EditText)rootView.findViewById(R.id.card_number);
        mCardExpireMonth = (EditText)rootView.findViewById(R.id.expire_month);
        mCardExpireYear = (EditText)rootView.findViewById(R.id.expire_year);
        mCardCVV = (EditText)rootView.findViewById(R.id.cvv);
        mAddCardBtn = (Button)rootView.findViewById(R.id.add_card);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.list_cards);

        mPaypalBtn = (Button)rootView.findViewById(R.id.paypal_btn);

        mAddCardBtn.setOnClickListener(this);
        mPaypalBtn.setOnClickListener(this);
    }
    private void initRecyclerView(){
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
//        llm.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(llm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_card:
                try {
                    // Show Progress first
                    showDialog();
                    addNewCard();
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.paypal_btn:
//                ClientToken clientTokenRequest = new ClientTokenRequest()
//                        .customerId(aCustomerId);
//                String clientToken = gateway.clientToken().generate(clientTokenRequest);

//                PaymentRequest paymentRequest = new PaymentRequest()
//                        .clientToken(PAYPAL_CLIENT_TOKEN);
//                startActivityForResult(paymentRequest.getIntent(getActivity()), PAYPAL_REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    private void showDialog(){
        if(mDialog==null)mDialog = new ProgressDialog(mContext);
        mDialog.setMessage("Please wait..");
        mDialog.show();
    }
    private void dismissDialog(){
        if(mDialog!=null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    /*
    Add New Card
     */
    private void addNewCard() throws AuthenticationException {
       /* if (dialog == null) {
            dialog = new Dialog(mContext);
            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);   //Remove Title Area
            dialog.setContentView(R.layout.dialog_addcard);
        }
        // set values for custom dialog components - text, image and button
//        final EditText searchEt = (EditText) dialog.findViewById(R.id.search_country_et);
        dialog.show();*/

        String cardNumber = mCardNumber.getText().toString();
        String expireMonth = mCardExpireMonth.getText().toString();
        String expireYear = mCardExpireYear.getText().toString();
        String cvv = mCardCVV.getText().toString();

        Card card = new Card(cardNumber, Integer.parseInt(expireMonth), Integer.parseInt(expireYear), cvv);
        if(!card.validateNumber()){
            dismissDialog();
            CommonMethods.getInstance().displaySnackBar(mContext,"Card is not Valid",rootView);
        }
        else if(!card.validateExpiryDate()){
            dismissDialog();
            CommonMethods.getInstance().displaySnackBar(mContext,"Expire Date is not Valid",rootView);
        }
        else if(!card.validateCVC()){
            dismissDialog();
            CommonMethods.getInstance().displaySnackBar(mContext,"CVC is not Valid",rootView);
        }
        else{

            // Register and Generate Token for further payment
            // Enter Published Key here
            Stripe stripe = new Stripe(mContext.getResources().getString(R.string.stripe_published_key));
            stripe.createToken(
                    card,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            // Send token to your server
                            dismissDialog();
                            if(mStripeCardsList==null) mStripeCardsList = new ArrayList<Token>();
                            mStripeCardsList.add(token);

                            // Update List of Cards
                            if(mAdapter == null){
                                mAdapter = new SavedCardsAdapter(mContext,mStripeCardsList,PaymentFragment.this);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                            else{
                                mAdapter.updateList(mStripeCardsList);
                            }
                            CommonMethods.getInstance().displaySnackBar(mContext,"CARD HAS BEEN ADDED SUCCESSFULLY",rootView);
                        }
                        public void onError(Exception error) {
                            dismissDialog();
                            // Show localized error message
                            Toast.makeText(getContext(),
                                    error.getLocalizedMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );
        }
    }

    /*
    Charge payment
     */
    public void initiatePayment(Token token){
        // Create a Customer
        new finalpayment(token).execute();
    }

    class finalpayment extends AsyncTask<String, String, String>{
        Map<String, Object> chargeParams;
        Charge charge;
        Token token;
        public finalpayment(Token tok){
            this.token = tok;
        }

        @Override
        protected void onPreExecute() {
            showDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                if(token!=null){
                    // Set your secret key: remember to change this to your live secret key in production
                    // See your keys here https://dashboard.stripe.com/account/apikeys
                    com.stripe.Stripe.apiKey  = mContext.getResources().getString(R.string.stripe_secret_key);

                    String stripeCustomerID = SharedPreferencesHandler.getStringValues(mContext,mContext.getResources().getString(R.string.pref_stripe_customer_id));
                    if(stripeCustomerID!=null && !stripeCustomerID.equals("")){
                        try {
                            chargeParams = new HashMap<String, Object>();
                            chargeParams.put("amount", 1000); // amount in cents, again
                            chargeParams.put("currency", "usd");
//                        chargeParams.put("source", paymentToken);
                            chargeParams.put("customer", stripeCustomerID);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        Map<String, Object> customerParams = new HashMap<String, Object>();
                        customerParams.put("source", token.getId());
                        customerParams.put("description", "Example customer");
                        Customer customer = Customer.create(customerParams);

                        // Save the customer ID for future transactions
                        SharedPreferencesHandler.setStringValues(mContext,mContext.getResources().getString(R.string.pref_stripe_customer_id),customer.getId());
                        // Get the credit card details submitted by the form
                        // Create the charge on Stripe's servers - this will charge the user's card
                        try {
                            chargeParams = new HashMap<String, Object>();
                            chargeParams.put("amount", 1000); // amount in cents, again
                            chargeParams.put("currency", "usd");
//                        chargeParams.put("source", paymentToken);
                            chargeParams.put("customer", customer.getId());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    try {
                        charge = Charge.create(chargeParams);
                        return "success";
                    } catch (AuthenticationException e) {
//                        CommonMethods.getInstance().displaySnackBar(mContext,"Authentication Failed",rootView);
                        e.printStackTrace();
                    } catch (InvalidRequestException e) {
//                        CommonMethods.getInstance().displaySnackBar(mContext,"Invalid Request",rootView);
                        e.printStackTrace();
                    } catch (APIConnectionException e) {
//                        CommonMethods.getInstance().displaySnackBar(mContext,"API Connection Error",rootView);
                        e.printStackTrace();
                    } catch (CardException e) {
//                        CommonMethods.getInstance().displaySnackBar(mContext,"Card Exception",rootView);
                        e.printStackTrace();
                    } catch (APIException e) {
//                        CommonMethods.getInstance().displaySnackBar(mContext,"Stripe API Error",rootView);
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissDialog();
            if( s!=null && s.equalsIgnoreCase("success")){
                CommonMethods.getInstance().DisplayToast(mContext,"IsPAID "+charge.getPaid());
            }
            else{
                CommonMethods.getInstance().displaySnackBar(mContext,"Payment Failed due to some reasons",rootView);
            }

        }
    }


}
