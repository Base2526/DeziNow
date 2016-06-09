package com.android.dezi.beans.passenger_payment;

import java.util.List;

/**
 * Created by anuj.sharma on 5/13/2016.
 */
public class PaymentParentModel {
    private String paymentType="";
    private List<PaymentChildModel> childList;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public List<PaymentChildModel> getChildList() {
        return childList;
    }

    public void setChildList(List<PaymentChildModel> childList) {
        this.childList = childList;
    }


}
