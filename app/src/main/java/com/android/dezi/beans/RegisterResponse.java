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

/**
 * Created by Mobilyte on 4/12/2016.
 */
public class RegisterResponse {

    /**
     * status : 1
     * message : Confirm Contact number
     * user_id : 9
     * data : {"email":"","contact_number":"9991098234","social_id":"1172554012754805","is_social":"1","device_token":"6424d6f90f0b1c06","updated_at":"2016-04-13 19:00:43","role":3}
     */

    private int status;
    private String message;
    private int user_id;
    /**
     * email :
     * contact_number : 9991098234
     * social_id : 1172554012754805
     * is_social : 1
     * device_token : 6424d6f90f0b1c06
     * updated_at : 2016-04-13 19:00:43
     * role : 3
     */

    private DataEntity data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String email;
        private String contact_number;
        private String social_id;
        private String is_social;
        private String device_token;
        private String updated_at;
        private int role;
        private String become_driver_request;
        private String is_driver_approved;

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

        public String getSocial_id() {
            return social_id;
        }

        public void setSocial_id(String social_id) {
            this.social_id = social_id;
        }

        public String getIs_social() {
            return is_social;
        }

        public void setIs_social(String is_social) {
            this.is_social = is_social;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getBecome_driver_request() {
            return become_driver_request;
        }

        public void setBecome_driver_request(String become_driver_request) {
            this.become_driver_request = become_driver_request;
        }

        public String getIs_driver_approved() {
            return is_driver_approved;
        }

        public void setIs_driver_approved(String is_driver_approved) {
            this.is_driver_approved = is_driver_approved;
        }

    }
}
