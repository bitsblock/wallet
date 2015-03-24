package com.liberic.bitcoinwallet.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liberic.bitcoinwallet.R;
import com.liberic.bitcoinwallet.model.Contact;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.RowHolder> {
    private final List<Contact> data;

    public ContactsAdapter(List<Contact> data) {
        this.data = data;
    }

    @Override
    public RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        RowHolder rh = new RowHolder(v);
        return rh;
    }

    @Override
    public void onBindViewHolder(RowHolder holder, int position) {
        holder.imageContact.setImageResource(R.drawable.bender);
        holder.nameContact.setText(data.get(position).getName());
        holder.phoneContact.setText(data.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        CircleImageView imageContact;
        TextView nameContact;
        TextView phoneContact;

        public RowHolder(View itemView) {
            super(itemView);
            imageContact = (CircleImageView) itemView.findViewById(R.id.image_contact);
            nameContact = (TextView) itemView.findViewById(R.id.name_contact);
            phoneContact = (TextView) itemView.findViewById(R.id.phone_contact);
        }
    }
}
