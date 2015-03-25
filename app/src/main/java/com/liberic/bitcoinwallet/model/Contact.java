package com.liberic.bitcoinwallet.model;


public class Contact {
    String uriImage;
    String name;
    String phone;

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

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

    public Contact(String name, String phone, String uriImage){
        this.name = name;
        this.phone = phone;
        this.uriImage = uriImage;
    }

}
