package com.bitsblock.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.bitsblock.wallet.R;
import com.bitsblock.wallet.util.Constant;
import com.bitsblock.wallet.util.Globals;
import com.bitsblock.wallet.util.Interface;

import java.math.BigDecimal;

public class ReceiveActivity extends ActionBarActivity {
    private ImageView qrCodeImage;
    private EditText bitcoinEditText;
    //TODO The address must be request from server
    private String address = "1Q7RTAuPcxhxKYxyKppNLzAtP9T1MfkUut";
    private String amount = "0.0";
    private int width = 480;
    private int height = 480;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        qrCodeImage = (ImageView) findViewById(R.id.image_qr_code);

        bitcoinEditText = (EditText) findViewById(R.id.edit_bitcoins);
        final EditText currencyEditText = (EditText) findViewById(R.id.edit_currency);
        final Context ctx = this;

        /*bitcoinEditText.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    Pattern mPattern = Pattern.compile("[1-9][0-9]{0,7}(\\.[0-9]{0,7}[1-9])?|(0\\.[0-9]{0,7}[1-9])?");

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String formattedSource = source.subSequence(start, end).toString();
                        String destPrefix = dest.subSequence(0, dstart).toString();
                        String destSuffix = dest.subSequence(dend, dest.length()).toString();
                        String result = destPrefix + formattedSource + destSuffix;
                        result = result.replace(",", ".");
                        Matcher matcher = mPattern.matcher(result);
                        if (matcher.matches()) {
                            return null;
                        }
                        return "";
                    }
                }
        });*/

        bitcoinEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Double conversion = Interface.convertToCurrencyFromBitcoin(ctx, Double.valueOf(bitcoinEditText.getText().toString()));
                String conversionString = new BigDecimal(conversion).toPlainString();
                if(!conversionString.equals(currencyEditText.getText().toString()))
                    currencyEditText.setText(conversionString);

                amount = bitcoinEditText.getText().toString();
                try {
                    writeQrCode(parseDataToBitcoin());
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        /*currencyEditText.setFilters(new InputFilter[]{
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    Pattern mPattern = Pattern.compile("[1-9][0-9]*(\\.[0-9]?[1-9])?|(0\\.[0-9]?[1-9])?");

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        String formattedSource = source.subSequence(start, end).toString();
                        String destPrefix = dest.subSequence(0, dstart).toString();
                        String destSuffix = dest.subSequence(dend, dest.length()).toString();
                        String result = destPrefix + formattedSource + destSuffix;
                        result = result.replace(",", ".");
                        Matcher matcher = mPattern.matcher(result);
                        if (matcher.matches()) {
                            return null;
                        }
                        return "";
                    }
                }
        });*/
        currencyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double conversion = Interface.convertToBitcoinFromCurrency(ctx, Double.valueOf(currencyEditText.getText().toString()));
                String conversionString = new BigDecimal(conversion).toPlainString();
                if(!conversionString.equals(bitcoinEditText.getText().toString()))
                    bitcoinEditText.setText(conversionString);

                amount = bitcoinEditText.getText().toString();
                try {
                    writeQrCode(parseDataToBitcoin());
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView mCurrencyType = (TextView) findViewById(R.id.icon_currency);
        mCurrencyType.setText(getSharedPreferences(Constant.PREF_CURRENT_USER, MODE_PRIVATE).getString(Constant.CURRENCY_TYPE, null));

        try {
            writeQrCode(parseDataToBitcoin());
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String parseDataToBitcoin(){
        String uri = "bitcoin:" + address + "?amount=" + amount;
        String user = getSharedPreferences(Constant.PREF_GENERAL,MODE_PRIVATE).getString(Constant.USER, null);
        if(user == null)
            user = Globals.user;

        uri += "&label=" + user;
        TelephonyManager telemamanger = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String getSimSerialNumber = telemamanger.getLine1Number();
        if (getSimSerialNumber != null || getSimSerialNumber != "") {
            uri += "+" + getSimSerialNumber;
        } else {
            uri += "+" + Globals.phone;
        }

        return uri;
    }

    private void writeQrCode(String data) throws WriterException {
        com.google.zxing.MultiFormatWriter writer = new MultiFormatWriter();

        BitMatrix bm = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
        Bitmap imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for(int i=0; i < width; i++){
            for(int j=0;j < height;j++){
                imageBitmap.setPixel(i,j,bm.get(i,j) ? Color.BLACK: Color.WHITE);
            }
        }

        if (imageBitmap != null) {
            qrCodeImage.setImageBitmap(imageBitmap);
        } else {
            Toast.makeText(getApplicationContext(), "Error of input", Toast.LENGTH_SHORT).show();
        }
    }
}
