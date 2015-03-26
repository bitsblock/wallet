package com.liberic.bitcoinwallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.liberic.bitcoinwallet.R;
import com.liberic.bitcoinwallet.adapter.LastTransactionsAdapter;
import com.liberic.bitcoinwallet.model.Transaction;
import com.liberic.bitcoinwallet.util.Mode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private LastTransactionsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(null);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        getSupportActionBar().setElevation(0.0F);
        getSupportActionBar().setTitle("");
        //setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.list_last_transactions);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        List<Transaction> data = new ArrayList<>();
        data.add(new Transaction("Test","987123456", Mode.SEND, 0.0, new Date()));
        data.add(new Transaction("Test","987176456", Mode.RECEIVE, 1.0, new Date()));
        mAdapter = new LastTransactionsAdapter(data);
        mRecyclerView.setAdapter(mAdapter);

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
        imageAccount.setImageResource(R.drawable.homer);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "TODO Activity settings is coming", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_logout) {
            LoginActivity.logout(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
