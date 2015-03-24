package com.liberic.bitcoinwallet.model;

import com.liberic.bitcoinwallet.util.Mode;

import java.util.Date;

public class Transaction {
    String contact;
    Mode mode;
    String phone;
    double valueTransaction;
    Date date;

    public Transaction(String contact, String phone, Mode mode, double value, Date date) {
        this.contact = contact;
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

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
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
