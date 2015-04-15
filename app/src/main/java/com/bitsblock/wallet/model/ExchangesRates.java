package com.bitsblock.wallet.model;

import com.bitsblock.wallet.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExchangesRates {
    private Ticket USD;
    private Ticket JPY;
    private Ticket CNY;
    private Ticket SGD;
    private Ticket HKD;
    private Ticket CAD;
    private Ticket NZD;
    private Ticket AUD;
    private Ticket CLP;
    private Ticket GBP;
    private Ticket DKK;
    private Ticket SEK;
    private Ticket ISK;
    private Ticket CHF;
    private Ticket BRL;
    private Ticket EUR;
    private Ticket RUB;
    private Ticket PLN;
    private Ticket THB;
    private Ticket KRW;
    private Ticket TWD;
    private List<Ticket> list = new ArrayList<>();

    public ExchangesRates(JSONObject obj) throws JSONException {
        this.USD = new Ticket(Constant.Rates.USD.toString(), obj.getJSONObject(Constant.Rates.USD.toString()));
        this.JPY = new Ticket(Constant.Rates.JPY.toString(), obj.getJSONObject(Constant.Rates.JPY.toString()));
        this.CNY = new Ticket(Constant.Rates.CNY.toString(), obj.getJSONObject(Constant.Rates.CNY.toString()));
        this.SGD = new Ticket(Constant.Rates.SGD.toString(), obj.getJSONObject(Constant.Rates.SGD.toString()));
        this.HKD = new Ticket(Constant.Rates.HKD.toString(), obj.getJSONObject(Constant.Rates.HKD.toString()));
        this.CAD = new Ticket(Constant.Rates.CAD.toString(), obj.getJSONObject(Constant.Rates.CAD.toString()));
        this.NZD = new Ticket(Constant.Rates.NZD.toString(), obj.getJSONObject(Constant.Rates.NZD.toString()));
        this.AUD = new Ticket(Constant.Rates.AUD.toString(), obj.getJSONObject(Constant.Rates.AUD.toString()));
        this.CLP = new Ticket(Constant.Rates.CLP.toString(), obj.getJSONObject(Constant.Rates.CLP.toString()));
        this.GBP = new Ticket(Constant.Rates.GBP.toString(), obj.getJSONObject(Constant.Rates.GBP.toString()));
        this.DKK = new Ticket(Constant.Rates.DKK.toString(), obj.getJSONObject(Constant.Rates.DKK.toString()));
        this.SEK = new Ticket(Constant.Rates.SEK.toString(), obj.getJSONObject(Constant.Rates.SEK.toString()));
        this.ISK = new Ticket(Constant.Rates.ISK.toString(), obj.getJSONObject(Constant.Rates.ISK.toString()));
        this.CHF = new Ticket(Constant.Rates.CHF.toString(), obj.getJSONObject(Constant.Rates.CHF.toString()));
        this.BRL = new Ticket(Constant.Rates.BRL.toString(), obj.getJSONObject(Constant.Rates.BRL.toString()));
        this.EUR = new Ticket(Constant.Rates.EUR.toString(), obj.getJSONObject(Constant.Rates.EUR.toString()));
        this.RUB = new Ticket(Constant.Rates.RUB.toString(), obj.getJSONObject(Constant.Rates.RUB.toString()));
        this.PLN = new Ticket(Constant.Rates.PLN.toString(), obj.getJSONObject(Constant.Rates.PLN.toString()));
        this.THB = new Ticket(Constant.Rates.THB.toString(), obj.getJSONObject(Constant.Rates.THB.toString()));
        this.KRW = new Ticket(Constant.Rates.KRW.toString(), obj.getJSONObject(Constant.Rates.KRW.toString()));
        this.TWD = new Ticket(Constant.Rates.TWD.toString(), obj.getJSONObject(Constant.Rates.TWD.toString()));
        addToList();
    }

    private void addToList() {
        list.add(USD);
        list.add(JPY);
        list.add(CNY);
        list.add(SGD);
        list.add(HKD);
        list.add(CAD);
        list.add(NZD);
        list.add(AUD);
        list.add(CLP);
        list.add(GBP);
        list.add(DKK);
        list.add(SEK);
        list.add(ISK);
        list.add(CHF);
        list.add(BRL);
        list.add(EUR);
        list.add(RUB);
        list.add(PLN);
        list.add(THB);
        list.add(KRW);
        list.add(TWD);
    }

    public List<Ticket> getList() {
        return list;
    }
}
