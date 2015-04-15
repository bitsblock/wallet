package com.bitsblock.wallet.model;

import com.bitsblock.wallet.util.Constant;

import java.util.Date;

@SuppressWarnings("unused")
public class Transaction {
    private String contact;
    private Constant.Mode mode;
    private String phone;
    private double valueTransaction;
    private Date date;

    public Transaction(String contact, String phone, Constant.Mode mode, double value, Date date) {
        this.contact = "Test";
        this.phone = phone;
        this.mode = mode;
        this.valueTransaction = value;
        this.date = date;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Constant.Mode getMode() {
        return mode;
    }

    public void setMode(Constant.Mode mode) {
        this.mode = mode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getValueTransaction() {
        return valueTransaction;
    }

    public void setValueTransaction(double valueTransaction) {
        this.valueTransaction = valueTransaction;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
