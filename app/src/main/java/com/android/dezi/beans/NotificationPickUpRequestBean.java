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
 * Created by Mobilyte on 5/2/2016.
 */
public class NotificationPickUpRequestBean implements Serializable{

    /**
     * first_name : anuj
     * profile_pic : uploads/c075c11d3d8d44bb6bef1dfbdd9c2e51.jpg
     * car_detail : {"name":"skoda","number":"PB11L 1883","transmission":"both","model":"2006"}
     * eta : [{"distance":{"text":"4.0 mi","value":6377},"duration":{"text":"10 mins","value":590},"status":"OK"}]
     * pickup_address : {"latitude":"30.707339","longitude":"76.860539"}
     * ride_id : 259
     * notification_type : 1
     * title : Passenger Request for ride
     */

    private String first_name;
    private String profile_pic;
    /**
     * name : skoda
     * number : PB11L 1883
     * transmission : both
     * model : 2006
     */

    private CarDetailEntity car_detail;
    /**
     * latitude : 30.707339
     * longitude : 76.860539
     */

    private PickupAddressEntity pickup_address;
    private int ride_id;
    private String notification_type;
    private String title;
    /**
     * distance : {"text":"4.0 mi","value":6377}
     * duration : {"text":"10 mins","value":590}
     * status : OK
     */

    private List<EtaEntity> eta;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public CarDetailEntity getCar_detail() {
        return car_detail;
    }

    public void setCar_detail(CarDetailEntity car_detail) {
        this.car_detail = car_detail;
    }

    public PickupAddressEntity getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(PickupAddressEntity pickup_address) {
        this.pickup_address = pickup_address;
    }

    public int getRide_id() {
        return ride_id;
    }

    public void setRide_id(int ride_id) {
        this.ride_id = ride_id;
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

    public static class CarDetailEntity implements Serializable{
        private String name;
        private String number;
        private String transmission;
        private String model;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTransmission() {
            return transmission;
        }

        public void setTransmission(String transmission) {
            this.transmission = transmission;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }

    public static class PickupAddressEntity implements Serializable{
        private String latitude;
        private String longitude;

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
    }

    public static class EtaEntity implements Serializable{
        /**
         * text : 4.0 mi
         * value : 6377
         */

        private DistanceEntity distance;
        /**
         * text : 10 mins
         * value : 590
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
