package com.bitsblock.wallet.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsblock.wallet.R;
import com.bitsblock.wallet.util.Constant;
import com.bitsblock.wallet.util.Globals;
import com.bitsblock.wallet.util.Interface;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiveActivity extends ActionBarActivity {
    private ImageView qrCodeImage;
    //TODO The address must be request from server
    private String address = "1Q7RTAuPcxhxKYxyKppNLzAtP9T1MfkUut";
    private String amount = "0.0";
    private int width = 480;
    private int height = 480;
    private String msg = null;
    private ReceiveActivity ctx;
    private TextView bitcoinText;
    private TextView currencyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLayout();
    }

    private void setLayout() {
        ctx = this;

        if ("Bitcoin".equals(getSharedPreferences(Constant.PREF_CURRENT_USER,MODE_PRIVATE).getString(Constant.TYPE_INPUT,null))) {
            setContentView(R.layout.activity_receive_bitcoin);
            bitcoinText = (EditText) findViewById(R.id.edit_bitcoins);
            currencyText = (TextView) findViewById(R.id.edit_currency);
            bitcoinText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        Double conversion = Interface.convertToCurrencyFromBitcoin(ctx, Double.valueOf(bitcoinText.getText().toString()));
                        currencyText.setText(conversion.toString());
                    } catch (NumberFormatException e) {
                        currencyText.setText("0.0");
                    }
                }
            });
            bitcoinText.setFilters(new InputFilter[]{
                    new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                        Pattern mPattern = Pattern.compile("[0-9]{0,8}(\\.[0-9]{0,8})?|(0\\.[0-9]{0,8})?");

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
            });
            loadViews();
        } else if ("Currency".equals(getSharedPreferences(Constant.PREF_CURRENT_USER,MODE_PRIVATE).getString(Constant.TYPE_INPUT,null))) {
            setContentView(R.layout.activity_receive_currency);
            currencyText = (EditText) findViewById(R.id.edit_currency);
            bitcoinText = (TextView) findViewById(R.id.edit_bitcoins);
            currencyText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Double conversion = Interface.convertToBitcoinFromCurrency(ctx, Double.valueOf(currencyText.getText().toString()));
                    String conversionString = new BigDecimal(conversion).toPlainString();
                    bitcoinText.setText(conversionString);
                }
            });

            loadViews();
        }
    }

    private void loadViews() {
        qrCodeImage = (ImageView) findViewById(R.id.image_qr_code);
        TextView mCurrencyType = (TextView) findViewById(R.id.icon_currency);
        mCurrencyType.setText(getSharedPreferences(Constant.PREF_CURRENT_USER, MODE_PRIVATE).getString(Constant.CURRENCY_TYPE, null));
        Button buttonAccept = (Button) findViewById(R.id.button_accept);
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = ((EditText) findViewById(R.id.content)).getText().toString();
                amount = bitcoinText.getText().toString();
                bitcoinText.setEnabled(false);
                currencyText.setEnabled(false);
                findViewById(R.id.content).setEnabled(false);
                findViewById(R.id.msg).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.msg)).setText(msg);
                try {
                    writeQrCode(parseDataToBitcoin());
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        Button buttonCancel = (Button) findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitcoinText.setEnabled(true);
                bitcoinText.setEnabled(true);
                findViewById(R.id.content).setEnabled(true);
                findViewById(R.id.msg).setVisibility(View.INVISIBLE);
                qrCodeImage.setImageResource(R.drawable.ic_photo_black);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        if (getSimSerialNumber != null && !getSimSerialNumber.equals("")) {
            uri += "+" + getSimSerialNumber;
        } else {
            uri += "+" + Globals.phone;
        }

        if (msg != null && !msg.equals("")) {
            uri += "&message=" + msg;
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
