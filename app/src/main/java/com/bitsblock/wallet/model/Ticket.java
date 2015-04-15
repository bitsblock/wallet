package com.bitsblock.wallet.model;

import com.bitsblock.wallet.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

public class Ticket {
    private double fifteenMinutes;
    private double last;
    private double buy;
    private double sell;
    private String symbol;
    private String id;

    public Ticket(String id, JSONObject obj) throws JSONException {
        this.id = id;
        fifteenMinutes = obj.getDouble(Constant.Ticket.FIFTEEN);
        last = obj.getDouble(Constant.Ticket.LAST);
        buy = obj.getDouble(Constant.Ticket.BUY);
        sell = obj.getDouble(Constant.Ticket.SELL);
        symbol = obj.getString(Constant.Ticket.SYMBOL);
    }

    public double getFifteenMinutes() {
        return fifteenMinutes;
    }

    public double getLast() {
        return last;
    }

    public double getBuy() {
        return buy;
    }

    public double getSell() {
        return sell;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getId() {
        return id;
    }
}
