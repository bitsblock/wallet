package com.bitsblock.wallet.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.bitsblock.wallet.R;
import com.bitsblock.wallet.util.Constant;
import com.bitsblock.wallet.util.Globals;
import com.bitsblock.wallet.util.Interface;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendToContactFromCodeActivity extends ActionBarActivity {
    private String address;
    private String message;
    private String label;
    private double amount;

    private SendToContactFromCodeActivity ctx;
    private TextView bitcoinText;
    private TextView currencyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout();
    }

    private void setLayout() {
        ctx = this;

        if ("Bitcoin".equals(getSharedPreferences(Constant.PREF_CURRENT_USER,MODE_PRIVATE).getString(Constant.TYPE_INPUT,null))) {
            setContentView(R.layout.activity_send_to_contact_from_code_bitcoin);
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
            setContentView(R.layout.activity_send_to_contact_from_code_currency);
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
        Bundle extras = getIntent().getExtras();
        address = extras.getString(Constant.ADDRESS);
        label = extras.getString(Constant.LABEL);
        message = extras.getString(Constant.MESSAGE);
        ((TextView)findViewById(R.id.message)).setText(message + "\n" + address);
        amount = extras.getDouble(Constant.AMOUNT);

        TextView nameOfContactToSend = (TextView) findViewById(R.id.name_contact_to_send);
        nameOfContactToSend.setText(label.replace('+','\n'));

        CircleImageView imageOfContactToSend = (CircleImageView) findViewById(R.id.image_contact_to_send);
        imageOfContactToSend.setImageResource(R.drawable.bender);

        setTitle("Send to " + nameOfContactToSend.getText());

        TextView nameOfContact = (TextView) findViewById(R.id.name_contact);
        nameOfContact.setText(Globals.user);
        CircleImageView imageOfContact = (CircleImageView) findViewById(R.id.image_contact);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(Globals.uriPhoto));
            imageOfContact.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            imageOfContact.setImageResource(R.drawable.homer);
        }

        TextView mCurrencyType = (TextView) findViewById(R.id.icon_currency);
        mCurrencyType.setText(getSharedPreferences(Constant.PREF_CURRENT_USER, MODE_PRIVATE).getString(Constant.CURRENCY_TYPE, null));

        bitcoinText.setText(Double.toString(amount));
        try {
            Double conversion = Interface.convertToCurrencyFromBitcoin(ctx, Double.valueOf(bitcoinText.getText().toString()));
            currencyText.setText(conversion.toString());
        } catch (NumberFormatException e) {
            currencyText.setText("0.0");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_to_contact_from_code, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
