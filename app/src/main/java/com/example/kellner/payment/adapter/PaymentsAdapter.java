package com.example.kellner.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.kellner.R;
import com.example.kellner.payment.PaymentSystemActivity;

import at.orderlibrary.Position;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PaymentsAdapter extends BaseAdapter {
    private List<Position> payments = new ArrayList<>();
    private List<Position> selectedPayments;
    private int layoutId;
    private LayoutInflater inflater;
    private Consumer addPositionC;

    public PaymentsAdapter(Context ctx, int layoutId, List<Position> payments, Consumer addPositionC){
        this.payments = payments;
        this.selectedPayments = selectedPayments;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.addPositionC = addPositionC;
    }

    @Override
    public int getCount() {
        return payments.size();
    }

    @Override
    public Object getItem(int position) {
        return payments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        Position position = payments.get(pos);
        View listItem = (convertView == null) ? inflater.inflate(this.layoutId, null) : convertView;
        TextView txtPaymentName = listItem.findViewById(R.id.txtPaymentName);
        TextView txtPaymentAmount = listItem.findViewById(R.id.txtPaymentAmount);
        TextView txtPaymentPrice = listItem.findViewById(R.id.txtPaymentPrice);
        Button btnSelectPayment = listItem.findViewById(R.id.btnSelectPayment);

        if(position.product.specialWish != null){
            txtPaymentName.setText(position.product.offer.name + " " + position.product.specialWish);
        }else{
            txtPaymentName.setText(position.product.offer.name);
        }

        btnSelectPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPositionC.accept(position);
            }
        });



        txtPaymentAmount.setText(String.valueOf(position.amount));
        double totalPrice = position.calcPrice();
        NumberFormat formatter = new DecimalFormat("#0.00");
        txtPaymentPrice.setText(formatter.format(totalPrice) + " €");


        return listItem;
    }
}
