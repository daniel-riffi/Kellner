package com.example.kellner.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kellner.R;

import at.orderlibrary.Position;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectedOfferAdapter extends BaseAdapter {
    private List<Position> selectedPositions = new ArrayList<>();
    private int layoutId;
    private LayoutInflater inflater;
    private Consumer addPositionConsumer;
    private Consumer deletePositionConsumer;
    private Consumer deleteLongPositionConsumer;


    public SelectedOfferAdapter(Context ctx, int layoutId, List<Position> selectedPositions, Consumer addPC, Consumer delPC, Consumer delLongPC) {
        this.selectedPositions = selectedPositions;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.addPositionConsumer = addPC;
        this.deletePositionConsumer = delPC;
        this.deleteLongPositionConsumer = delLongPC;
    }

    @Override
    public int getCount() {
        return selectedPositions.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedPositions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        Position position = selectedPositions.get(pos);
        View listItem = (convertView == null) ? inflater.inflate(this.layoutId, null) : convertView;
        TextView txtSelectedOfferName = listItem.findViewById(R.id.txtSelectedOfferName);
        if(position.product.specialWish != null){
            txtSelectedOfferName.setText(position.product.offer.name + " " + position.product.specialWish);
        }else{
            txtSelectedOfferName.setText(position.product.offer.name);
        }

        ((TextView) listItem.findViewById(R.id.txtSelectedOfferNumber)).setText(String.valueOf(position.amount));
        Button btnSelectOffer = listItem.findViewById(R.id.btnSelectOffer);
        btnSelectOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPositionConsumer.accept(position);
            }
        });
        Button btnDeselectOffer = listItem.findViewById(R.id.btnDeselectOffer);
        btnDeselectOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePositionConsumer.accept(position);
            }
        });
        btnDeselectOffer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteLongPositionConsumer.accept(position);
                return true;
            }
        });
        return listItem;
    }
}
