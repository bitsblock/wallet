package com.liberic.bitcoinwallet.util;

import android.content.Context;
import android.preference.Preference;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liberic.bitcoinwallet.R;

public class ToolbarPreference extends Preference {
    public ToolbarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        parent.setPadding(0, 0, 0, 0);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_prefrences, parent, false);

        Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle(getTitle());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PreferenceScreen prefScreen = (PreferenceScreen) getPreferenceManager().findPreference(getKey());
                //prefScreen.getDialog().dismiss();
            }
        });
        return layout;
    }
}
