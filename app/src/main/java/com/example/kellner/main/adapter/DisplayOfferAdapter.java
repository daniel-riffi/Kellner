package com.example.kellner.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.kellner.R;

import at.orderlibrary.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;



public class DisplayOfferAdapter extends BaseAdapter {
    private List<Offer> offers = new ArrayList<>();
    private int layoutId;
    private LayoutInflater inflater;
    private Consumer consumer;
    private Consumer consumeDialog;

    public DisplayOfferAdapter(Context ctx, int layoutId, List<Offer> offers, Consumer consumer, Consumer consumeDialog){
        this.offers = offers;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.consumer = consumer;
        this.consumeDialog = consumeDialog;
    }

    @Override
    public int getCount() {
        return offers.size();
    }

    @Override
    public Offer getItem(int position) {
        return offers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Offer offer = offers.get(position);
        View listItem = (view == null) ? inflater.inflate(this.layoutId, null) : view;
        ((Button) listItem.findViewById(R.id.btnAdd)).setText(offer.name);
        //String tmp = getString(R.string.item);
        ((Button) listItem.findViewById(R.id.btnAdd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumer.accept(offer);
            }
        });

        ((Button) listItem.findViewById(R.id.btnAdd)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                consumeDialog.accept(offer);
                return true;
            }
        });

        return listItem;
    }
}
