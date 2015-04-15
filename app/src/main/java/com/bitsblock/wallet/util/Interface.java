package com.bitsblock.wallet.util;

import android.content.Context;
import android.view.View;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Interface {
    public static void getRates(Context context, boolean activatePd) {
        new AsyncConnector(context, Constant.URL_RATES, activatePd).execute();
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    public static Double convertToCurrencyFromBitcoin(Context ctx, Double bitcoin) {
        Double currency = bitcoin * ctx.getSharedPreferences(Constant.PREF_CURRENT_USER,Context.MODE_PRIVATE).getFloat(Constant.CURRENCY_VALUE,1.0f);
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.valueOf(df.format(new BigDecimal(currency)).replace(',','.'));
    }

    public static Double convertToBitcoinFromCurrency(Context ctx,Double currency) {
        return currency / ctx.getSharedPreferences(Constant.PREF_CURRENT_USER,Context.MODE_PRIVATE).getFloat(Constant.CURRENCY_VALUE,1.0f);
    }
}
