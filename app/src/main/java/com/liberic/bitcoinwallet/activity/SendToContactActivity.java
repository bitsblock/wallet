package com.liberic.bitcoinwallet.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.liberic.bitcoinwallet.R;
import com.liberic.bitcoinwallet.util.Constant;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendToContactActivity extends ActionBarActivity {

    private TextView nameOfContactToSend;
    private CircleImageView imageOfContactToSend;
    private CircleImageView imageOfContact;
    private TextView nameOfContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_contact);

        Bundle extras = getIntent().getExtras();
        nameOfContactToSend = (TextView) findViewById(R.id.name_contact_to_send);
        nameOfContactToSend.setText(extras.getString(Constant.NAME));

        nameOfContact = (TextView) findViewById(R.id.name_contact);
        //nameOfContact.setText(extras.getString(Constant.NAME));

        imageOfContact = (CircleImageView) findViewById(R.id.image_contact);
        //imageOfContact.setText(extras.getString(Constant.NAME));

        imageOfContactToSend = (CircleImageView) findViewById(R.id.image_contact_to_send);
        Bitmap bmp = extras.getParcelable(Constant.IMAGE);
        imageOfContactToSend.setImageBitmap(bmp);
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
