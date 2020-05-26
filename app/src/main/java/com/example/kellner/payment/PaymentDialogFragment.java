package com.example.kellner.payment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.kellner.R;
import com.example.kellner.main.MainActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class PaymentDialogFragment extends DialogFragment {

    private TextView txtTableNumberNumber;
    private TextView getTxtTableNumberLetter;
    private NumberPicker spnTableNumberNumber;
    private Spinner spnTableNumberLetter;
    private PaymentDialogFragment paymentDialogFragment;
    private PaymentSystemActivity paymentSystemActivity;
    private Consumer consumer;
    private List<String> spinnerArray = new ArrayList<String>();

    public PaymentDialogFragment(PaymentSystemActivity paymentSystemActivity, Consumer consumer){
        this.paymentSystemActivity = paymentSystemActivity;
        this.consumer = consumer;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.payment_dialog_layout, null);
        builder.setView(view);
        txtTableNumberNumber = view.findViewById(R.id.txtTableNumberNumber);
        getTxtTableNumberLetter = view.findViewById(R.id.txtTableNumberLetter);
        spnTableNumberNumber = view.findViewById(R.id.spnTableNumberNumber);
        spnTableNumberLetter = view.findViewById(R.id.spnTableNumberLetter);

        spnTableNumberNumber.setMaxValue(12);
        spnTableNumberNumber.setMinValue(1);
        spinnerArray.add("A");
        spinnerArray.add("B");
        spinnerArray.add("C");
        spinnerArray.add("D");
        spinnerArray.add("E");
        spinnerArray.add("F");
        spinnerArray.add("G");
        spinnerArray.add("H");
        spinnerArray.add("I");
        spinnerArray.add("J");
        spinnerArray.add("K");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(paymentSystemActivity, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTableNumberLetter.setAdapter(adapter);

        spnTableNumberNumber.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                txtTableNumberNumber.setText(String.valueOf(i1));
            }
        });

        spnTableNumberLetter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTxtTableNumberLetter.setText(spnTableNumberLetter.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txtTableNumberNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                spnTableNumberNumber.setValue(Integer.parseInt(txtTableNumberNumber.getText().toString()));
                return true;
            }
        });

        txtTableNumberNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(getTxtTableNumberLetter.getText().toString().equals("A")){
                    spnTableNumberLetter.setSelection(0);
                }else if(getTxtTableNumberLetter.getText().toString().equals("B")){
                    spnTableNumberLetter.setSelection(1);
                }else if(getTxtTableNumberLetter.getText().toString().equals("C")){
                    spnTableNumberLetter.setSelection(2);

                }
                return true;
            }
        });

        builder.setTitle(R.string.bez_finished_title);
        builder.setMessage(R.string.bez_finished_text);
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!txtTableNumberNumber.getText().toString().equals("") && !getTxtTableNumberLetter.getText().toString().equals("")){
                    //TODO: Überprüfung auf falsche Table eingaben (A1 - K12)


                    String txt = txtTableNumberNumber.getText().toString().concat(getTxtTableNumberLetter.getText().toString());
                    consumer.accept(txt);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.tbl_number_title);
                    builder.setMessage(R.string.tbl_number_text);
                    builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            paymentDialogFragment = new PaymentDialogFragment(paymentSystemActivity, consumer);
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
