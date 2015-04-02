package com.liberic.bitcoinwallet.util;

public class Constant {
    /************ PREFERENCES *********************/
    public static final String PREF_GENERAL = "credentials_preferences";
    public static final String PREF_CURRENT_USER = Globals.user +"_preferences";

    public static final String USER = "USERNAME";
    public static final String PASS = "PASSWORD";
    public static final String CURRENCY_TYPE = "CURRENCY_TYPE";
    public static final String CURRENCY_VALUE = "CURRENCY_VALUE";

    public static final String NAME = "nameOfContact";
    public static final String PHONE = "phoneOfContact";
    public static final String IMAGE = "imageOfContact";
    public static final String MODE = "mode";
    public static final String ADDRESS = "address";
    public static final String LABEL = "label";
    public static final String MESSAGE = "message";
    public static final String AMOUNT = "amount";

    /************ MYSQL **************************/
    public static final String IP_MYSQL = "172.17.0.17";
    public static final String PORT_MYSQL = "3306";
    public static final String TABLE_USER_MYSQL = "user";
    public static final String USER_MYSQL = "root";
    public static final String PASS_MYSQL = "root";
    public static final String USER_TABLE_MYSQL = "username";
    public static final String PASS_TABLE_MYSQL = "password";
    public static final String URL_RATES = "https://blockchain.info/es/ticker";

    public enum Mode {
        SEND, RECEIVE
    }

    public enum Rates {
        USD, JPY, CNY, SGD, HKD, CAD, NZD, AUD, CLP, GBP, DKK, SEK, ISK, CHF, BRL, EUR, RUB, PLN, THB, KRW, TWD
    }

    public class Ticket {
        public static final String FIFTEEN = "15m";
        public static final String LAST = "last";
        public static final String BUY = "buy";
        public static final String SELL = "sell";
        public static final String SYMBOL = "symbol";
    }
}
