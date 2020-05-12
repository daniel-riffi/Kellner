package com.example.kellner.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kellner.R;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import at.orderlibrary.Position;

public class SelectedPaymentsAdapter extends BaseAdapter {
    private List<Position> selectedPayments = new ArrayList<>();
    private int layoutId;
    private LayoutInflater inflater;
    private Consumer deletePositionC;

    public SelectedPaymentsAdapter(Context ctx, int layoutId, List<Position> selectedPayments, Consumer deletePositionC){
        this.selectedPayments = selectedPayments;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.deletePositionC = deletePositionC;
    }

    @Override
    public int getCount() {
        return selectedPayments.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedPayments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        Position position = selectedPayments.get(pos);
        View listItem = (convertView == null) ? inflater.inflate(this.layoutId, null) : convertView;
        TextView txtPaymentName = listItem.findViewById(R.id.txtPaymentName);
        TextView txtPaymentAmount = listItem.findViewById(R.id.txtPaymentAmount);
        TextView txtPaymentPrice = listItem.findViewById(R.id.txtPaymentPrice);
        Button btnDeSelectPayment = listItem.findViewById(R.id.btnDeSelectPayment);
        if(position.product.specialWish != null){
            txtPaymentName.setText(position.product.offer.name + " " + position.product.specialWish);
        }else{
            txtPaymentName.setText(position.product.offer.name);
        }
        txtPaymentAmount.setText(String.valueOf(position.amount));
        double totalPrice = position.calcPrice();
        NumberFormat formatter = new DecimalFormat("#0.00");
        txtPaymentPrice.setText(formatter.format(totalPrice) + " €");

        btnDeSelectPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePositionC.accept(position);
            }
        });


        return listItem;
    }
}
