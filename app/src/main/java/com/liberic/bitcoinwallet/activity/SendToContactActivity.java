package com.liberic.bitcoinwallet.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.liberic.bitcoinwallet.R;
import com.liberic.bitcoinwallet.util.Constant;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendToContactActivity extends ActionBarActivity {

    private TextView nameOfContactToSend;
    private CircleImageView imageOfContactToSend;
    private CircleImageView imageOfContact;
    private TextView nameOfContact;
    private EditText bitcoinEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_contact);

        Bundle extras = getIntent().getExtras();
        nameOfContactToSend = (TextView) findViewById(R.id.name_contact_to_send);
        nameOfContactToSend.setText(extras.getString(Constant.NAME));

        setTitle("Send to " + nameOfContactToSend);

        nameOfContact = (TextView) findViewById(R.id.name_contact);
        //nameOfContact.setText(extras.getString(Constant.NAME));

        imageOfContact = (CircleImageView) findViewById(R.id.image_contact);
        //imageOfContact.setText(extras.getString(Constant.NAME));

        imageOfContactToSend = (CircleImageView) findViewById(R.id.image_contact_to_send);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(SendActivity.getContext().getContentResolver(), Uri.parse(extras.getString(Constant.IMAGE)));
            imageOfContactToSend.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            imageOfContactToSend.setImageResource(R.drawable.bender);
        }

        bitcoinEditText = (EditText) findViewById(R.id.edit_bitcoins);
        bitcoinEditText.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 8, afterDecimal = 8;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, @NonNull Spanned dest, int dstart, int dend) {
                        String temp = bitcoinEditText.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.equals("0")) {
                            //Don't allow beginning with a 0
                            return "";
                        } else if (!temp.contains(".")) {
                            //No decimal point placed yet
                            if(temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if(temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source,start,end,dest,dstart,dend);
                    }
                }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_to_contact, menu);
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
