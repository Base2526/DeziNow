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
import java.util.List;

/**
 * Created by Mobilyte on 4/28/2016.
 */
public class PassangerRideInfoResponse {

    /**
     * status : 1
     * message : Confirm Contact number
     * data : {"cars":[{"id":"1","user_id":"9","name":"Skoda","transmission":"automatic","number":"HR-04-08544","model":"2006","is_default":"0","added_at":"2016-04-25 17:57:59"},{"id":"2","user_id":"9","name":"Skoda","transmission":"manual","number":"HR-04-08540","model":"2008","is_default":"1","added_at":"2016-04-25 17:58:03"}],"payment_methods":[{"id":"2","user_id":"9","account_type":"card","card_token":"123qwerytry","payer_email":"","charge_id":"","created_at":"2016-04-26","updated_at":"2016-04-26 11:12:30"}]}
     */

    private int status;
    private String message;
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

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        /**
         * id : 1
         * user_id : 9
         * name : Skoda
         * transmission : automatic
         * number : HR-04-08544
         * model : 2006
         * is_default : 0
         * added_at : 2016-04-25 17:57:59
         */

        private List<CarsEntity> cars;
        /**
         * id : 2
         * user_id : 9
         * account_type : card
         * card_token : 123qwerytry
         * payer_email :
         * charge_id :
         * created_at : 2016-04-26
         * updated_at : 2016-04-26 11:12:30
         */

        private List<PaymentMethodsEntity> payment_methods;

        public List<CarsEntity> getCars() {
            return cars;
        }

        public void setCars(List<CarsEntity> cars) {
            this.cars = cars;
        }

        public List<PaymentMethodsEntity> getPayment_methods() {
            return payment_methods;
        }

        public void setPayment_methods(List<PaymentMethodsEntity> payment_methods) {
            this.payment_methods = payment_methods;
        }

        public static class CarsEntity {
            private String id;
            private String user_id;
            private String name;
            private String transmission;
            private String number;
            private String model;
            private String is_default;
            private String added_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTransmission() {
                return transmission;
            }

            public void setTransmission(String transmission) {
                this.transmission = transmission;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getModel() {
                return model;
            }

            public void setModel(String model) {
                this.model = model;
            }

            public String getIs_default() {
                return is_default;
            }

            public void setIs_default(String is_default) {
                this.is_default = is_default;
            }

            public String getAdded_at() {
                return added_at;
            }

            public void setAdded_at(String added_at) {
                this.added_at = added_at;
            }
        }

        public static class PaymentMethodsEntity {
            private String id;
            private String user_id;
            private String account_type;
            private String card_token;
            private String payer_email;
            private String charge_id;
            private String created_at;
            private String updated_at;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getAccount_type() {
                return account_type;
            }

            public void setAccount_type(String account_type) {
                this.account_type = account_type;
            }

            public String getCard_token() {
                return card_token;
            }

            public void setCard_token(String card_token) {
                this.card_token = card_token;
            }

            public String getPayer_email() {
                return payer_email;
            }

            public void setPayer_email(String payer_email) {
                this.payer_email = payer_email;
            }

            public String getCharge_id() {
                return charge_id;
            }

            public void setCharge_id(String charge_id) {
                this.charge_id = charge_id;
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
        }
    }
}
