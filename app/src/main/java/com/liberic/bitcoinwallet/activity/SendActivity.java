package com.liberic.bitcoinwallet.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import com.google.zxing.client.android.Intents;
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
        if (id == R.id.action_qrcode) {
            openQrCamera();
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
                new String[] { Data._ID, Data.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE, Phone.PHOTO_THUMBNAIL_URI },
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
                sendToContact(view, data.get(position).getName(), data.get(position).getPhone(),data.get(position).getUriImage());
            }
        });
        mRecyclerView.swapAdapter(dataAdapter, false);
    }

    private void openQrCamera(){
        try {
            Intent intent = new Intent(Intents.Scan.ACTION);
            intent.putExtra("SCAN_MODE","QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(SendActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void sendToContact(View v, String name, String phone, String uriPhoto) {
        Intent intent = new Intent(this, SendToContactActivity.class);

        Bundle extras = new Bundle();
        extras.putString(Constant.NAME,name);
        extras.putString(Constant.PHONE, phone);
        extras.putString(Constant.IMAGE, uriPhoto);
        intent.putExtras(extras);

        startActivity(intent);
    }
}
