/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dezi.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.dezi.R;
import com.android.dezi.api.APIHandler;
import com.android.dezi.api.APIResponseInterface;
import com.android.dezi.beans.PickupRequestResponse;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.utility.ConstantFile;
import com.android.dezi.utility.SharedPreferencesHandler;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistrationIntentService extends IntentService implements APIResponseInterface{

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
//    private static final String ProjectId = "dezi-1271";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);
            CommonMethods.getInstance().e("GCM TOKEN", "GCM TOKEN-> " + token);
            String GCM_TOKEN = SharedPreferencesHandler.getStringValues(this,this.getResources().getString(R.string.pref_gcm_token));
            if(GCM_TOKEN == null){
                SharedPreferencesHandler.setStringValues(this,this.getResources().getString(R.string.pref_gcm_token),token);
                updateGCM(token);
            }
            else{
                if(GCM_TOKEN.equals(token)){
                    // Token Didn't update
                }
                else{
                    // Token gets update, Update it on server and in sharedpref
                    SharedPreferencesHandler.setStringValues(this,this.getResources().getString(R.string.pref_gcm_token),token);
                    updateGCM(token);
                }
            }

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

    /*
    Update GCM TOKEN
     */
    private void updateGCM(String token){
        try {
            String userID = SharedPreferencesHandler.getStringValues(this,this.getResources().getString(R.string.pref_userId));
            if(userID==null || userID.equals("")){
                CommonMethods.getInstance().DisplayToast(this,this.getResources().getString(R.string.error_something));
                return;
            }

            Map<String,String> param = new HashMap<>();
            param.put("token", ConstantFile.DEF_TOKEN);
            param.put("user_id", userID);
            param.put("gcm_token", token);
            CommonMethods.getInstance().e("UPDATE GCM TOKEN","PARAM-> " + param.toString());
            APIHandler.getInstance(ConstantFile.APIURL).updateGCMToken(param,this);

        }
        catch (Exception e){

        }
    }

    @Override
    public void onSuccess(Response response, Retrofit retrofit, String tag) {
        try {
            if (response.isSuccessful()) {
                PickupRequestResponse obj = (PickupRequestResponse) response.body();
                if(obj.getStatus()==1){
                    // GCM Updated
                }
                else{
                    // SOME ERROR OCCURED
                }
            }
            else{
                CommonMethods.getInstance().DisplayToast(this,this.getResources().getString(R.string.error_api));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        try {
            CommonMethods.getInstance().DisplayToast(this,this.getResources().getString(R.string.error_api));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
