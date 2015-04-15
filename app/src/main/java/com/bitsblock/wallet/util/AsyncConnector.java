package com.bitsblock.wallet.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

public class AsyncConnector extends AsyncTask<Void, Void, Void> {
	private JSONToStringCollection data;
	private String url;
	private ProgressDialog pd = null;
	private Context context;

	public AsyncConnector(Context context, String url, boolean pdOn) {
		this.url = url;
		this.context = context;
		if(pdOn) {
			pd = new ProgressDialog(context);
		}
	}

	@Override
	protected void onPreExecute() {
		if(pd != null) {
			pd.setIndeterminate(true);
			pd.setMessage("Message");
			pd.setTitle("Title");
			pd.show();
		} else {
			Toast.makeText(context, "Message", Toast.LENGTH_SHORT).show();
		}
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		ConnectorHttpJSON connector = new ConnectorHttpJSON(url);
		try {
			JSONObject obj = connector.execute();
			data = new JSONToStringCollection(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		Globals.entries = data.getEntries();
		Globals.values = data.getEntryValues();

		Globals.mapEntriesValues = data.getMap();

		if(pd != null) {
			pd.dismiss();
		} else {
			Toast.makeText(context, "Message", Toast.LENGTH_SHORT).show();
		}
		super.onPostExecute(result);
	}
}
