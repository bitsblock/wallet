package com.bitsblock.wallet.util;

import com.bitsblock.wallet.model.ExchangesRates;
import com.bitsblock.wallet.model.Ticket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JSONToStringCollection {
    private final ExchangesRates exchangesRates;

    public JSONToStringCollection(JSONObject obj) throws JSONException {
        this.exchangesRates = new ExchangesRates(obj);
    }

    public CharSequence[] getEntries() {
        List<String> list = new ArrayList<>();
        for(Ticket t:exchangesRates.getList()) {
            list.add(t.getId());
        }
        return list.toArray(new CharSequence[list.size()]);
    }

    public CharSequence[] getEntryValues() {
        List<String> list = new ArrayList<>();
        for(Ticket t:exchangesRates.getList()) {
            list.add(String.valueOf(t.getLast()));
        }
        return list.toArray(new CharSequence[list.size()]);
    }

    public Map<String, Double> getMap() {
        Map<String, Double> map = new HashMap<>();
        for(Ticket t:exchangesRates.getList()) {
            map.put(t.getId(),t.getLast());
        }
        return map;
    }
}
