package com.example.kellner.payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.kellner.R;
import com.example.kellner.main.MainActivity;

public class PaymentDialogFragment extends DialogFragment {
    private EditText txtTableNumber;
    private Intent intent;
    private PaymentDialogFragment paymentDialogFragment;
    private PaymentSystemActivity paymentSystemActivity;

    public PaymentDialogFragment(Intent intent, PaymentSystemActivity paymentSystemActivity){
        this.intent = intent;
        this.paymentSystemActivity = paymentSystemActivity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.payment_dialog_layout, null);
        builder.setView(view);
        txtTableNumber = view.findViewById(R.id.txtTableNumber);

        builder.setTitle("Bestellung wird gesendet");
        builder.setMessage("Die Bestellung wird nun an den Koch übermittelt. Bitte Tischnummer eingeben:");
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!txtTableNumber.getText().toString().equals("")){
                    startActivity(intent);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Tischnummer!");
                    builder.setMessage("Bitte vorher Tischnummer eingeben!");
                    builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            paymentDialogFragment = new PaymentDialogFragment(intent, paymentSystemActivity);
                            paymentDialogFragment.show(paymentSystemActivity.getSupportFragmentManager(), "dialog");
                        }
                    });
                    builder.show();
                }

            }
        });
        return builder.create();
    }
}
