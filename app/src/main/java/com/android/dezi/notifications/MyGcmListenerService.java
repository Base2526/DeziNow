/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dezi.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.android.dezi.MainActivity;
import com.android.dezi.R;
import com.android.dezi.beans.NotificationAcceptRideBean;
import com.android.dezi.beans.NotificationPickUpRequestBean;
import com.android.dezi.utility.CommonMethods;
import com.android.dezi.views.driver.Activities.DriverDashboard;
import com.android.dezi.views.passenger.Activities.PassangerDashboard;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Bundle userBundle = data.getBundle("notification");
        String title = userBundle.getString("title");
        String message = userBundle.getString("message");
        String notificationType = userBundle.getString("notification_type");
        String userData = userBundle.getString("data");

        System.out.println("DATA " + data.toString());
        System.out.println("USER DATA" + userData);
        System.out.println("TITLE " + title);
        System.out.println("NOTIFICATION TYPE " + notificationType);
        Gson gson = new Gson();

        if (notificationType.equalsIgnoreCase("1")) {
            // PickUp Request for Driver
            NotificationPickUpRequestBean beanObj = gson.fromJson(userData, NotificationPickUpRequestBean.class);
            beanObj.setTitle(title);
            beanObj.setNotification_type(notificationType);

            Intent intent = new Intent(this, DriverDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("driver_pickup", beanObj);
            startActivity(intent);
        } else if (notificationType.equalsIgnoreCase("2")) {
            //Pickup Accepted By Driver
            NotificationAcceptRideBean beanObj = gson.fromJson(userData, NotificationAcceptRideBean.class);
            beanObj.setTitle(title);
            beanObj.setNotification_type(notificationType);

            Intent intent = new Intent(this, PassangerDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("accept_ride", beanObj);
            startActivity(intent);
        } else {
            // Do not Show Notification
            hideNotification();
//            sendNotification(title);
        }

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
//
        // [END_EXCLUDE]
    }

    private void hideNotification() {

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param title GCM message received.
     */
    private void sendNotification1(String title) {
        CommonMethods.getInstance().e("GCM MESSAGE", "GCM MESSAGE-> " + title);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("DEZI")
                .setContentText(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}