package com.example.kellner.payment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.kellner.R;
import com.example.kellner.main.MainActivity;
import com.example.kellner.main.OfferDialogFragment;
import com.example.kellner.payment.adapter.PaymentsAdapter;
import com.example.kellner.payment.adapter.SelectedPaymentsAdapter;

import at.orderlibrary.Offer;
import at.orderlibrary.Position;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PaymentSystemActivity extends AppCompatActivity{
    private ListView paymentsView;
    private ListView selectedPaymentsView;
    private List<Position> payments;
    private List<Position> selectedPayments = new ArrayList<>();
    private double totalPrice;
    private double curPrice;
    private PaymentsAdapter paymentsAdapter;
    private SelectedPaymentsAdapter selectedPaymentsAdapter;
    private TextView txtTotalPrice;
    private TextView txtTotalPriceName;
    private TextView txtToPay;
    private TextView txtToPayName;
    private TextView txtSelected;
    private TextView txtSelectedName;
    private Button btnFinishCurPayment;
    private Button btnFinishPayment;
    private PaymentDialogFragment paymentDialogFragment;
    private PaymentSystemActivity paymentSystemActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_system);
        Intent intent = getIntent();
        paymentSystemActivity = this;
        payments = (ArrayList<Position>)intent.getSerializableExtra("selectedPositions");

        paymentsView = findViewById(R.id.positionsView);
        selectedPaymentsView = findViewById(R.id.selectedPaymentsView);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtTotalPriceName = findViewById(R.id.txtTotalPriceName);
        txtToPay = findViewById(R.id.txtToPay);
        txtToPayName = findViewById(R.id.txtToPayName);
        txtSelected = findViewById(R.id.txtSelected);
        txtSelectedName = findViewById(R.id.txtSelectedName);
        btnFinishCurPayment = findViewById(R.id.btnFinishCurPayment);
        btnFinishPayment = findViewById(R.id.btnFinishPayment);

        getTotalPriceOfPaymentsList();
        getTotalPriceOfSelectedPaymentsList();
        bindAdapterToListView(paymentsView, selectedPaymentsView);
        initTotalPrice();
        initButtons(btnFinishCurPayment, btnFinishPayment);
    }

    private void initButtons(Button btnFinishCurPayment, Button btnFinishPayment) {
        getTotalPriceOfSelectedPaymentsList();
        btnFinishCurPayment.setOnClickListener(x -> {
            if(selectedPayments.size() != 0){
                totalPrice -= curPrice;
                updatePricePay();
            }
            paymentsAdapter.notifyDataSetChanged();
            selectedPayments.clear();
            updateSelected();
            selectedPaymentsAdapter.notifyDataSetChanged();
        });
        Intent intent = new Intent(this, MainActivity.class);
        btnFinishPayment.setOnClickListener(x -> {
            String t = txtToPay.getText().toString();
            String[] parts = t.split(" ");
            double d = Double.parseDouble(parts[0]);
            if(d != 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.bez_not_finished_title);
                builder.setMessage(R.string.bez_not_finished_text);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        paymentDialogFragment = new PaymentDialogFragment(intent, paymentSystemActivity);
                        paymentDialogFragment.show(getSupportFragmentManager(), "dialog");
                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }else{
                paymentDialogFragment = new PaymentDialogFragment(intent, paymentSystemActivity);
                paymentDialogFragment.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    private void bindAdapterToListView(ListView listViewPayments, ListView listViewSelectedPayments){
        Consumer<Position> addPositionConsumer = x -> handlePosition(x, true);
        paymentsAdapter = new PaymentsAdapter(this, R.layout.payments_layout, payments, addPositionConsumer);
        listViewPayments.setAdapter(paymentsAdapter);

        Consumer<Position> deletePositionConsumer = x -> handlePosition(x, false);
        selectedPaymentsAdapter = new SelectedPaymentsAdapter(this, R.layout.selected_payments_layout, selectedPayments, deletePositionConsumer);
        listViewSelectedPayments.setAdapter(selectedPaymentsAdapter);
    }

    private void handlePosition(Position position, boolean addel) {
        Position position2 = new Position(position);
        if(addel){
            if (position.amount == 1) {
                payments.remove(position);
            } else {
                for (Position pos :
                        payments) {
                    if (pos.equals(position)) {
                        pos.amount--;
                    }
                }
            }
            if (!selectedPayments.contains(position2)) {
                System.out.println("This Position is completely new");
                position2.amount = 1;
                selectedPayments.add(position2);
            }else{
                for (Position pos :
                        selectedPayments) {
                    if (pos.equals(position2)) {
                        System.out.println("Position equals a position in selectedPositions");
                        pos.amount++;
                    }
                }
            }
        }else{
            if(!payments.contains(position2)){
                position2.amount = 1;
                payments.add(position2);
            }else{
                for (Position pos :
                        payments) {
                    if (pos.equals(position2)) {
                        pos.amount++;
                    }
                }
            }

            if (position.amount == 1) {
                System.out.println("This Position is completely new");
                selectedPayments.remove(position);
            }else{
                for (Position pos :
                        selectedPayments) {
                    if (pos.equals(position)) {
                        System.out.println("Position equals a position in selectedPositions");
                        pos.amount--;
                    }
                }
            }

        }

        updateSelected();
        selectedPaymentsAdapter.notifyDataSetChanged();
        paymentsAdapter.notifyDataSetChanged();
    }


    private void getTotalPriceOfPaymentsList(){
        for (Position pos:
                payments) {
            totalPrice += pos.calcPrice();
        }
    }

    private void getTotalPriceOfSelectedPaymentsList(){
        curPrice = 0;
        for (Position pos:
                selectedPayments) {
            curPrice += pos.calcPrice();
        }
    }

    private void updatePricePay(){
        NumberFormat formatter = new DecimalFormat("#0.00");
        txtToPay.setText(formatter.format(totalPrice) + " €");
        txtToPayName.setText("Noch zu bezahlen: ");
    }

    private void initTotalPrice(){
        NumberFormat formatter = new DecimalFormat("#0.00");
        txtTotalPrice.setText(formatter.format(totalPrice) + " €");
        txtTotalPriceName.setText("Gesamtpreis: ");

        txtToPay.setText(formatter.format(totalPrice) + " €");
        txtToPayName.setText("Noch zu bezahlen: ");
    }

    private void updateSelected(){
        getTotalPriceOfSelectedPaymentsList();
        NumberFormat formatter = new DecimalFormat("#0.00");
        txtSelected.setText(formatter.format(curPrice) + " €");
        txtSelectedName.setText("Ausgewählt: ");
    }

}
 