package com.liberic.bitcoinwallet.model;

/**
 * Created by josrom on 24/03/15.
 */
public class Contact {
    String name;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String phone;

    public Contact(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

}
