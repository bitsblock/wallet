package com.liberic.bitcoinwallet.util;

import android.content.Context;
import android.view.View;

public class Interface {
    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    public static Double convertToCurrencyFromBitcoin(Context ctx, Double bitcoin) {
        Double currency;
        currency = bitcoin / ctx.getSharedPreferences(Constant.PREF_CURRENT_USER,Context.MODE_PRIVATE).getFloat(Constant.CURRENCY_VALUE,1.0f);
        return currency;
    }

    public static Double convertToBitcoinFromCurrency(Context ctx,Double currency) {
        Double bitcoin;
        bitcoin = currency * ctx.getSharedPreferences(Constant.PREF_CURRENT_USER,Context.MODE_PRIVATE).getFloat(Constant.CURRENCY_VALUE,1.0f);
        return bitcoin;
    }
}
