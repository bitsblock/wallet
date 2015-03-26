package com.liberic.bitcoinwallet.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.provider.ContactsContract.Data;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.liberic.bitcoinwallet.R;
import com.liberic.bitcoinwallet.adapter.ContactsAdapter;
import com.liberic.bitcoinwallet.model.Contact;
import com.liberic.bitcoinwallet.util.Constant;
import com.liberic.bitcoinwallet.util.Interface;

import java.util.ArrayList;
import java.util.List;

public class SendActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private static SendActivity mApp;

    private static final String[] PHOTO_BITMAP_PROJECTION = new String[] { Photo.PHOTO };

    public static Context getContext() {
        return mApp.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_contacts);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        listQueryContact("");

        mApp = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        final SearchView mySearch = new SearchView(this);

        // Assumes current activity is the searchable activity
        mySearch.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Actions for search
        mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mySearch.clearFocus();
                listQueryContact(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listQueryContact(s);
                return false;
            }
        });

        menu.findItem(R.id.action_search).setActionView(mySearch);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_qr_code) {
            if(isCameraAvailable())
                openQrCamera();
            else
                Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_search) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void listQueryContact(String search){
        final List<Contact> data = new ArrayList<>();
        Cursor mCursor = getContentResolver().query(
                Data.CONTENT_URI,
                new String[]{Data._ID, Data.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE, Phone.PHOTO_THUMBNAIL_URI},
                Data.MIMETYPE + " = '" + Phone.CONTENT_ITEM_TYPE + "' AND " + Phone.NUMBER + " IS NOT NULL AND " + Data.DISPLAY_NAME + " LIKE '%" + search + "%'",
                null, Data.DISPLAY_NAME + " ASC"
        );

        if(mCursor.moveToFirst()) {
            do{
                data.add(new Contact(mCursor.getString(mCursor.getColumnIndex(Data.DISPLAY_NAME)), mCursor.getString(mCursor.getColumnIndex(Phone.NUMBER)), mCursor.getString(mCursor.getColumnIndex(Phone.PHOTO_THUMBNAIL_URI))));
            }while (mCursor.moveToNext());
        }
        mCursor.close();
        ContactsAdapter dataAdapter = new ContactsAdapter(data);
        dataAdapter.setClickListener(new Interface.ClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                sendToContact(data.get(position));
            }
        });
        mRecyclerView.swapAdapter(dataAdapter, false);
    }

    private void openQrCamera(){
        IntentIntegrator intent = new IntentIntegrator(this);
        intent.initiateScan(IntentIntegrator.QR_CODE_TYPES);

    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents=result.getContents();
            if (contents != null) {
                Toast toast = Toast.makeText(this, result.toString(), Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(this, "Fail at read", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void sendToContact(Contact contact) {
        Intent intent = new Intent(this, SendToContactActivity.class);

        Bundle extras = new Bundle();
        extras.putString(Constant.NAME,contact.getName());
        extras.putString(Constant.PHONE, contact.getPhone());
        extras.putString(Constant.IMAGE, contact.getUriImage());
        intent.putExtras(extras);

        startActivity(intent);
    }
}
