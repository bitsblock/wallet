package com.bitsblock.wallet.model;


@SuppressWarnings("unused")
public class Contact {
    private String uriImage;
    private String name;
    private String phone;

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
