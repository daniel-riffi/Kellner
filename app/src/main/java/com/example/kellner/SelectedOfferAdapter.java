package com.example.kellner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
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


    public SelectedOfferAdapter(Context ctx, int layoutId, List<Position> selectedPositions, Consumer addPC, Consumer delPC) {
        this.selectedPositions = selectedPositions;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.addPositionConsumer = addPC;
        this.deletePositionConsumer = delPC;
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
        ((TextView) listItem.findViewById(R.id.txtSelectedOfferName)).setText(position.product.offer.name);
        ((TextView) listItem.findViewById(R.id.txtSelectedOfferNumber)).setText(String.valueOf(position.amount));
        ((Button) listItem.findViewById(R.id.btnSelectOffer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPositionConsumer.accept(position);
            }
        });
        ((Button) listItem.findViewById(R.id.btnDeselectOffer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePositionConsumer.accept(position);
            }
        });
        return listItem;
    }
}
