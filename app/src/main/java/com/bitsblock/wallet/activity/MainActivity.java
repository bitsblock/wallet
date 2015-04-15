package com.bitsblock.wallet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitsblock.wallet.R;
import com.bitsblock.wallet.adapter.LastTransactionsAdapter;
import com.bitsblock.wallet.model.Transaction;
import com.bitsblock.wallet.util.Constant;
import com.bitsblock.wallet.util.Globals;
import com.bitsblock.wallet.util.Interface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends ActionBarActivity {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0.0F);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_last_transactions);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new LastTransactionsAdapter(getData()));

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshTransactions);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.swapAdapter(new LastTransactionsAdapter(getData()),false);
                mSwipeRefresh.setRefreshing(false);
            }
        });

        LinearLayout toolbar = (LinearLayout) findViewById(R.id.app_bar);
        Button btn = (Button) toolbar.findViewById(R.id.action_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SendActivity.class));
            }
        });

        btn = (Button) toolbar.findViewById(R.id.action_receive);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReceiveActivity.class));
            }
        });

        CircleImageView imageAccount = (CircleImageView) toolbar.findViewById(R.id.image_account);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(Globals.uriPhoto));
            imageAccount.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            imageAccount.setImageResource(R.drawable.homer);
        }

        TextView mBitcoinBalance = (TextView) findViewById(R.id.balance_bitcoins);

        TextView mCurrencyType = (TextView) toolbar.findViewById(R.id.icon_currency);
        mCurrencyType.setText(getSharedPreferences(Constant.PREF_CURRENT_USER,MODE_PRIVATE).getString(Constant.CURRENCY_TYPE,null));

        TextView mCurrencyBalance = (TextView) toolbar.findViewById(R.id.balance_currency);
        mCurrencyBalance.setText(Interface.convertToCurrencyFromBitcoin(this, Double.valueOf(mBitcoinBalance.getText().toString())).toString());
    }

    private List<Transaction> getData() {
        //TODO Connect to server
        List<Transaction> data = new ArrayList<>();
        data.add(new Transaction("Test","987123456", Constant.Mode.SEND, 0.0, new Date()));
        data.add(new Transaction("Test", "987176456", Constant.Mode.RECEIVE, 1.0, new Date()));
        return data;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_logout) {
            LoginActivity.logout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
