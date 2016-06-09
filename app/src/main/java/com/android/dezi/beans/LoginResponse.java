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
package com.android.dezi.beans;

/**
 * Created by Mobilyte on 4/12/2016.
 */
public class LoginResponse {

    /**
     * status : 1
     * message : Confirm Contact number
     * user_id : 25
     * data : {"id":"25","unique_code":"200025","first_name":"guri","last_name":"","dob":"1990-06-21","gender":"male","profile_pic":"uploads/002e5d2264859767887f9dcef6086244.jpg","anniversary":"1970-02-19","state":"haryana","city":"patiala","country_code":"","contact_number":"9417484078","is_social":"0","last_ip":"","last_login":"","remember_token":"","social_id":"","name":"","email":"gsm@gmail.com","device_token":"93a91dfcf885dc59","active":"1","become_driver_request":"0","is_driver_approved":"0","is_logged":"true","created_at":"2016-04-22 14:46:30","updated_at":"2016-04-22 17:16:30","role":"4"}
     */

    private int status;
    private String message;
    private String user_id;
    /**
     * id : 25
     * unique_code : 200025
     * first_name : guri
     * last_name :
     * dob : 1990-06-21
     * gender : male
     * profile_pic : uploads/002e5d2264859767887f9dcef6086244.jpg
     * anniversary : 1970-02-19
     * state : haryana
     * city : patiala
     * country_code :
     * contact_number : 9417484078
     * is_social : 0
     * last_ip :
     * last_login :
     * remember_token :
     * social_id :
     * name :
     * email : gsm@gmail.com
     * device_token : 93a91dfcf885dc59
     * active : 1
     * become_driver_request : 0
     * is_driver_approved : 0
     * is_logged : true
     * created_at : 2016-04-22 14:46:30
     * updated_at : 2016-04-22 17:16:30
     * role : 4
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private String id;
        private String unique_code;
        private String first_name;
        private String last_name;
        private String dob;
        private String gender;
        private String profile_pic;
        private String anniversary;
        private String state;
        private String city;
        private String country_code;
        private String contact_number;
        private String is_social;
        private String last_ip;
        private String last_login;
        private String remember_token;
        private String social_id;
        private String name;
        private String email;
        private String device_token;
        private String active;
        private String become_driver_request;
        private String is_driver_approved;
        private String is_logged;
        private String created_at;
        private String updated_at;
        private String role;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUnique_code() {
            return unique_code;
        }

        public void setUnique_code(String unique_code) {
            this.unique_code = unique_code;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getAnniversary() {
            return anniversary;
        }

        public void setAnniversary(String anniversary) {
            this.anniversary = anniversary;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public String getIs_social() {
            return is_social;
        }

        public void setIs_social(String is_social) {
            this.is_social = is_social;
        }

        public String getLast_ip() {
            return last_ip;
        }

        public void setLast_ip(String last_ip) {
            this.last_ip = last_ip;
        }

        public String getLast_login() {
            return last_login;
        }

        public void setLast_login(String last_login) {
            this.last_login = last_login;
        }

        public String getRemember_token() {
            return remember_token;
        }

        public void setRemember_token(String remember_token) {
            this.remember_token = remember_token;
        }

        public String getSocial_id() {
            return social_id;
        }

        public void setSocial_id(String social_id) {
            this.social_id = social_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
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

        public String getIs_logged() {
            return is_logged;
        }

        public void setIs_logged(String is_logged) {
            this.is_logged = is_logged;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
