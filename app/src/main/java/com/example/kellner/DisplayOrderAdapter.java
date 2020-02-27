package com.example.kellner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import at.orderlibrary.Offer;

public class DisplayOrderAdapter extends BaseAdapter {

    private List<Offer> offers = new ArrayList<>();
    private int layoutId;
    private LayoutInflater inflater;
    private Consumer consumer;

    public DisplayOrderAdapter(Context ctx, int layoutId, List<Offer> offers, Consumer consumer){
        this.offers = offers;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.consumer = consumer;
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
        ((TextView) listItem.findViewById(R.id.txtOfferName)).setText(offer.name);
        ((Button) listItem.findViewById(R.id.btnAdd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumer.accept(offer);
            }
        });
        return listItem;
    }
}