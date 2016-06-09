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

import com.android.dezi.utility.ConstantFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mobilyte on 2/17/2016.
 */
public class APIHandler {
    private static APIHandler instance;
    OkHttpClient client;
    HttpLoggingInterceptor interceptor;
    Retrofit retrofit;
    Call call = null;

    APICallMethods handler; // Interface where all API methods are geting called

    /*
    Private Constructor in case of Single Instance Class
     */
    private APIHandler(String baseUrl) {
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        handler = retrofit.create(APICallMethods.class);
    }

    public static APIHandler getInstance(String baseUrl) {
        if (instance == null) {
            instance = new APIHandler(baseUrl);
        }
        return instance;
    }

    /*
    Get Location Info On basis of Latitude and Longitude
     */
    public void getLocationInfo(String lat, String lng, final APIResponseInterface listener){
        //Complete URL will be like
        // http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=true
        String url = "maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=true";
        call = handler.getLocationInfo(url);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "getlocation");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Method to Call API
     */
    public void sendOTP(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.sendOTP(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Register User
     */
    public void registerUser(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.registerUser(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "register");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Login User
     */
    public void loginUser(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.loginUser(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "login");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Logout user
     */
    public void logoutUser(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.logoutUser(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "logout");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Get Profile
     */
    public void getProfile(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.getProfile(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "get_profile");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Update Driver Profile
     */
    public void updateDriverProfile(Map<String, String> params, String profile_path, final APIResponseInterface listener) {

        if (profile_path.equalsIgnoreCase("")) {
            params.put("profile_pic", "");
            call = handler.updateDriverProfileWithoutImage(params);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(profile_path));
            MultipartBody.Part body = MultipartBody.Part.createFormData("profile_pic", "user_profile.jpg", requestBody);

            HashMap<String, RequestBody> bodyParam = new HashMap<>();
            for (Map.Entry<String, String> obj : params.entrySet()) {
                bodyParam.put(obj.getKey(), RequestBody.create(MediaType.parse("multipart/form-data"), obj.getValue()));
            }
            call = handler.updateDriverProfile(bodyParam, body);
        }

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "update_driver_profile");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    update driver Position
     */
    public void updateDriverPosition(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.updateDriverPosition(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "update_position");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
   Get driver Position
    */
    public void getDriverPosition(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.getDriverPosition(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "get_position");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Get Estimated Fare Amount
     */
    public void getEstimatedFare(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.getEstimatedFare(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "estimate_fare");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    PICKUP Request
     */
    public void pickupRequest(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.pickupRequest(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "pickup_request");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
   UPDATE GCM TOKEN
    */
    public void updateGCMToken(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.updateGCMToken(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "pickup_request");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Get User Info i.e Cars and saved Payment methods
     */
    public void getPassangerRideInfo(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.getPassangerRideInfo(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "passanger_ride_info");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    CHANGE Mode
     */
    public void changeMode(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.changeMode(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "mode");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Toggle Profile
   */
    public void toggle_Profile(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.toogleProfile(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "toggle_Profile");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    /*
    Toggle Profile
   */
    public void acceptRide(Map<String, String> params, final APIResponseInterface listener) {
        call = handler.acceptRide(params);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                listener.onSuccess(response, retrofit, "accept_Ride");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

}
