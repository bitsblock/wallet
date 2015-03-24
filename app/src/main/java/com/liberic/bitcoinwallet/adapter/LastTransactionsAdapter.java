package com.liberic.bitcoinwallet.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liberic.bitcoinwallet.R;
import com.liberic.bitcoinwallet.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class LastTransactionsAdapter extends RecyclerView.Adapter<LastTransactionsAdapter.RowHolder> {
    private final SimpleDateFormat sdfDate;
    private List<Transaction> data;
    public LastTransactionsAdapter(List<Transaction> data){
        this.data = data;
        sdfDate = new SimpleDateFormat("HH:mm:ss MM-dd-yyyy", Locale.ENGLISH);
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row, parent, false);
        RowHolder rh = new RowHolder(v);
        return rh;
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        holder.imageContact.setImageResource(R.drawable.bender);
        holder.nameContact.setText(data.get(position).getContact());
        holder.phoneContact.setText(data.get(position).getPhone());
        holder.modeTransaction.setText(data.get(position).getMode().toString());
        holder.valueTransaction.setText(Double.toString(data.get(position).getValueTransaction()));
        holder.dateTransaction.setText(sdfDate.format(data.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        CircleImageView imageContact;
        TextView nameContact;
        TextView phoneContact;
        TextView modeTransaction;
        TextView iconBitcoins;
        TextView valueTransaction;
        TextView dateTransaction;

        public RowHolder(View itemView) {
            super(itemView);
            imageContact = (CircleImageView) itemView.findViewById(R.id.image_contact);
            nameContact = (TextView) itemView.findViewById(R.id.name_contact);
            phoneContact = (TextView) itemView.findViewById(R.id.phone_contact);
            modeTransaction = (TextView) itemView.findViewById(R.id.mode_transaction);
            iconBitcoins = (TextView) itemView.findViewById(R.id.icon_bitcoins);
            valueTransaction = (TextView) itemView.findViewById(R.id.value_transaction);
            dateTransaction = (TextView) itemView.findViewById(R.id.date_transaction);
        }
    }
}
