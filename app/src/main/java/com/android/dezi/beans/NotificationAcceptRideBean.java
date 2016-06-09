package com.android.dezi.beans;
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
import java.io.Serializable;
import java.util.List;

/**
 * Created by Mobilyte on 4/5/16.
 */
public class NotificationAcceptRideBean implements Serializable{


    /**
     * first_name : guri
     * email : gsm@gmail.com
     * contact_number : 9417484078
     * driver_id : 25
     * passenger_id : 12
     * profile_pic : uploads/a8efaebab24b552112de0e7a7b2beb4c.jpg
     * latitude : 30.6725054
     * longitude : 76.8555016
     * eta : [{"distance":{"text":"5.5 mi","value":8899},"duration":{"text":"17 mins","value":1007},"status":"OK"}]
     * notification_type : 1
     * title : Passenger Request for ride
     */

    private String first_name;
    private String email;
    private String contact_number;
    private String driver_id;
    private String passenger_id;
    private String profile_pic;
    private String latitude;
    private String longitude;
    private String notification_type;
    private String title;
    /**
     * distance : {"text":"5.5 mi","value":8899}
     * duration : {"text":"17 mins","value":1007}
     * status : OK
     */

    private List<EtaEntity> eta;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getPassenger_id() {
        return passenger_id;
    }

    public void setPassenger_id(String passenger_id) {
        this.passenger_id = passenger_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<EtaEntity> getEta() {
        return eta;
    }

    public void setEta(List<EtaEntity> eta) {
        this.eta = eta;
    }

    public static class EtaEntity implements Serializable{
        /**
         * text : 5.5 mi
         * value : 8899
         */

        private DistanceEntity distance;
        /**
         * text : 17 mins
         * value : 1007
         */

        private DurationEntity duration;
        private String status;

        public DistanceEntity getDistance() {
            return distance;
        }

        public void setDistance(DistanceEntity distance) {
            this.distance = distance;
        }

        public DurationEntity getDuration() {
            return duration;
        }

        public void setDuration(DurationEntity duration) {
            this.duration = duration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public static class DistanceEntity implements Serializable{
            private String text;
            private int value;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        public static class DurationEntity implements Serializable{
            private String text;
            private int value;

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }
}
