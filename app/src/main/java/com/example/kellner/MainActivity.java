package com.example.kellner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import at.orderlibrary.Category;
import at.orderlibrary.Offer;

public class MainActivity extends AppCompatActivity {

    private List<Offer> offers;
    private GridView gridView;
    private DisplayOrderAdapter displayOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fillItemsList();
        gridView = findViewById(R.id.gridView);
        bindAdapterToGridView(gridView);
    }

    private void fillItemsList() {
        offers = new ArrayList<>();
        offers.add(new Offer(1, "Schnitzel deluxe premium", 8.6, Category.FOOD));
        offers.add(new Offer(2, "Hendl", 5.6, Category.FOOD));
        offers.add(new Offer(3, "Bier", 4.5, Category.ALCOHOLIC_DRINK));
        offers.add(new Offer(4, "Apfelsaft", 2.5, Category.NON_ALCOHOLIC_DRINK));
        offers.add(new Offer(2, "Würstel", 5.6, Category.FOOD));
    }

    private void bindAdapterToGridView(GridView gridView) {
        Consumer<Offer> addOfferConsumer = x -> addOfferButtonClicked(x);
        displayOrderAdapter = new DisplayOrderAdapter(this, R.layout.display_offer_layout, offers, addOfferConsumer);
        gridView.setAdapter(displayOrderAdapter);
    }

    public void addOfferButtonClicked(Offer offer){
        System.out.println(offer.name + "clicked!");
    }
}
