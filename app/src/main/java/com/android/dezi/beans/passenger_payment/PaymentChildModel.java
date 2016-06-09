package com.android.dezi.beans.passenger_payment;

/**
 * Created by anuj.sharma on 5/13/2016.
 */
public class PaymentChildModel {
    private String cardnumber="";
    private String cartType="";
    private boolean isDefault=false;


    public String getCartType() {
        return cartType;
    }

    public void setCartType(String cartType) {
        this.cartType = cartType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }


}
