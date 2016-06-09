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
package com.android.dezi.api;

import com.android.dezi.beans.EstimatedFareResponse;
import com.android.dezi.beans.GetDriverLocationResponse;
import com.android.dezi.beans.GetProfileResponse;
import com.android.dezi.beans.LocationInfoBean;
import com.android.dezi.beans.LoginResponse;
import com.android.dezi.beans.OTPResponse;
import com.android.dezi.beans.PassangerRideInfoResponse;
import com.android.dezi.beans.PickupRequestResponse;
import com.android.dezi.beans.RegisterResponse;
import com.android.dezi.beans.UpdateDriverProfileResponse;
import com.android.dezi.beans.UpdatePositionResponse;
import com.android.dezi.utility.ConstantFile;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by Mobilyte on 2/17/2016.
 */
public interface APICallMethods {
    // Get LocationInfo


    @GET
    Call<LocationInfoBean> getLocationInfo(@Url String url);

    //OTP API

    @FormUrlEncoded
    @POST(ConstantFile.SEND_OTP)
    Call<OTPResponse> sendOTP(@FieldMap Map<String, String> options);

    // Register User

    @FormUrlEncoded
    @POST(ConstantFile.REGISTER_API)
    Call<RegisterResponse> registerUser(@FieldMap Map<String, String> options);

    // Login User

    @FormUrlEncoded
    @POST(ConstantFile.LOGIN_API)
    Call<LoginResponse> loginUser(@FieldMap Map<String, String> options);

    // Logout User

    @FormUrlEncoded
    @POST(ConstantFile.LOGOUT_API)
    Call<PickupRequestResponse> logoutUser(@FieldMap Map<String, String> options);

    //Get Profile

    @FormUrlEncoded
    @POST(ConstantFile.GET_PROFILE)
    Call<GetProfileResponse>getProfile(@FieldMap Map<String, String> options);

    //Update Driver Profile

    @Multipart
    @POST(ConstantFile.UPDATE_DRIVER_PROFILE)
    Call<UpdateDriverProfileResponse>updateDriverProfile(@PartMap Map<String, RequestBody> options, @Part MultipartBody.Part profile_pic);

    // Update Driver Profile without Image
    @FormUrlEncoded
    @POST(ConstantFile.UPDATE_DRIVER_PROFILE)
    Call<UpdateDriverProfileResponse>updateDriverProfileWithoutImage(@FieldMap Map<String, String> options);

    //update Driver position
    @FormUrlEncoded
    @POST(ConstantFile.UPDATE_DRIVER_POSITION)
    Call<UpdatePositionResponse>updateDriverPosition(@FieldMap Map<String, String> options);

    //Get Driver position
    @FormUrlEncoded
    @POST(ConstantFile.GET_DRIVER_POSITION)
    Call<GetDriverLocationResponse>getDriverPosition(@FieldMap Map<String, String> options);

    //Get Estimated Fare Amount
    @FormUrlEncoded
    @POST(ConstantFile.GET_ESTIMATED_FARE)
    Call<EstimatedFareResponse>getEstimatedFare(@FieldMap Map<String, String> options);

    //Pick Up Request
    @FormUrlEncoded
    @POST(ConstantFile.PICKUP_REQUEST)
    Call<PickupRequestResponse>pickupRequest(@FieldMap Map<String, String> options);

    //Update GCM Token
    @FormUrlEncoded
    @POST(ConstantFile.UPDATE_GCM_TOKEN)
    Call<PickupRequestResponse>updateGCMToken(@FieldMap Map<String, String> options);

    //Get Passanger Info
    @FormUrlEncoded
    @POST(ConstantFile.GET_PASSANGER_RIDE_INFO)
    Call<PassangerRideInfoResponse>getPassangerRideInfo(@FieldMap Map<String, String> options);

    // CHANGE MODE

    @FormUrlEncoded
    @POST(ConstantFile.CHANGE_MODE)
    Call<PickupRequestResponse> changeMode(@FieldMap Map<String, String> options);

    // TOGGLE DRIVER PROFILE

    @FormUrlEncoded
    @POST(ConstantFile.TOGGLE_PROFILE)
    Call<PickupRequestResponse> toogleProfile(@FieldMap Map<String, String> options);

    //  ACCEPT RIDE BY DRIVER

    @FormUrlEncoded
    @POST(ConstantFile.ACCEPT_RIDE)
    Call<PickupRequestResponse> acceptRide(@FieldMap Map<String, String> options);
}
