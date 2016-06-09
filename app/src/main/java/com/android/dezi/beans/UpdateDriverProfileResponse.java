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
 * Created by Mobilyte on 4/15/2016.
 */
public class UpdateDriverProfileResponse {


    /**
     * status : 1
     * message : Confirm Contact number
     * user_id : 14
     * data : {"user_id":"14","role":4,"transmission":"automatic","navigation_system":"google_map","terms_conditions":"1","modified_at":"2016-04-19 14:18:59","email":"test@gmail.com","first_name":"raj","dob":"1990-12-08","gender":"male","anniversary":"2010-10-08","profile_pic":""}
     */

    private int status;
    private String message;
    private String user_id;
    /**
     * user_id : 14
     * role : 4
     * transmission : automatic
     * navigation_system : google_map
     * terms_conditions : 1
     * modified_at : 2016-04-19 14:18:59
     * email : test@gmail.com
     * first_name : raj
     * dob : 1990-12-08
     * gender : male
     * anniversary : 2010-10-08
     * profile_pic :
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
        private String user_id;
        private int role;
        private String transmission;
        private String navigation_system;
        private String terms_conditions;
        private String modified_at;
        private String email;
        private String first_name;
        private String dob;
        private String gender;
        private String anniversary;
        private String profile_pic;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String getTransmission() {
            return transmission;
        }

        public void setTransmission(String transmission) {
            this.transmission = transmission;
        }

        public String getNavigation_system() {
            return navigation_system;
        }

        public void setNavigation_system(String navigation_system) {
            this.navigation_system = navigation_system;
        }

        public String getTerms_conditions() {
            return terms_conditions;
        }

        public void setTerms_conditions(String terms_conditions) {
            this.terms_conditions = terms_conditions;
        }

        public String getModified_at() {
            return modified_at;
        }

        public void setModified_at(String modified_at) {
            this.modified_at = modified_at;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
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

        public String getAnniversary() {
            return anniversary;
        }

        public void setAnniversary(String anniversary) {
            this.anniversary = anniversary;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }
    }
}
