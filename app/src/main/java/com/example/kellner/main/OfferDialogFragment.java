package com.example.kellner.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

import com.example.kellner.R;

import at.orderlibrary.Offer;
import at.orderlibrary.Position;
import org.w3c.dom.Text;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OfferDialogFragment extends DialogFragment {
    private Position position;
    private TextView txtAmount;
    private TextView txtSpecialWishes;
    private Consumer consumer;

    public OfferDialogFragment(Consumer consumer){
        this.consumer = consumer;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(view);
        txtAmount = view.findViewById(R.id.txtAmount);
        txtSpecialWishes = view.findViewById(R.id.txtSpecialWishes);


        builder.setMessage(position.product.offer.name);

        builder.setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            position.amount = Integer.parseInt(txtAmount.getText().toString());
                            position.product.specialWish = txtSpecialWishes.getText().toString();
                            consumer.accept(position);
                    }
                });

        return builder.create();
    }


    public void setPosition(Position p){
        position = new Position();
        position = p;
    }

    public Position getPosition() {
        return position;
    }
}
