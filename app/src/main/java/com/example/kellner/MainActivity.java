package com.example.kellner;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import androidx.core.content.ContextCompat;
import at.orderlibrary.*;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    private List<Offer> allOffers;
    private List<Offer> offers;
    private List<Position> selectedPositions;
    private GridView gridView;
    private ListView listView;
    private DisplayOfferAdapter displayOfferAdapter;
    private SelectedOfferAdapter selectedOfferAdapter;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button little_food = findViewById(R.id.btn_little_food);
        Button large_food = findViewById(R.id.btn_large_food);
        Button alcoholic_drinks = findViewById(R.id.btn_alcoholic_drinks);
        Button nonalcoholic_drinks = findViewById(R.id.btn_nonalcoholic_drinks);
        Button desert = findViewById(R.id.btn_desert);

        category = Category.NON_ALCOHOLIC_DRINK;
        nonalcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonalcoholic_drinks_colored));

        fillItemsList();
        initButtons(little_food, large_food, alcoholic_drinks, nonalcoholic_drinks, desert);

        gridView = findViewById(R.id.gridView);
        listView = findViewById(R.id.listView);
        bindAdapterToGridView(gridView);
        bindAdapterToListView(listView);

    }

    private void initButtons(Button little_food, Button large_food, Button alcoholic_drinks, Button nonalcoholic_drinks, Button desert){

        little_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = Category.SMALL_FOOD;
                System.out.println("Category changed to " + category.name());
                little_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.little_food_colored));
                large_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.large_food));
                alcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.alcoholic_drinks));
                nonalcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonalcoholic_drinks));
                desert.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.desert));
                filterList(category);
            }
        });

        large_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = Category.FOOD;
                System.out.println("Category changed to " + category.name());
                large_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.large_food_colored));
                little_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.little_food));
                alcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.alcoholic_drinks));
                nonalcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonalcoholic_drinks));
                desert.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.desert));
                filterList(category);
            }
        });

        alcoholic_drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = Category.ALCOHOLIC_DRINK;
                System.out.println("Category changed to " + category.name());
                alcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.alcoholic_drinks_colored));
                little_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.little_food));
                large_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.large_food));
                nonalcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonalcoholic_drinks));
                desert.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.desert));
                filterList(category);
            }
        });

        nonalcoholic_drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = Category.NON_ALCOHOLIC_DRINK;
                System.out.println("Category changed to " + category.name());
                nonalcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonalcoholic_drinks_colored));
                little_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.little_food));
                large_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.large_food));
                alcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.alcoholic_drinks));
                desert.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.desert));
                filterList(category);
            }
        });

        desert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = Category.DESERT;
                System.out.println("Category changed to " + category.name());
                desert.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.desert_colored));
                little_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.little_food));
                large_food.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.large_food));
                alcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.alcoholic_drinks));
                nonalcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonalcoholic_drinks));
                filterList(category);
            }
        });
    }

    private void fillItemsList() {
        allOffers = new ArrayList<>();
        offers = new ArrayList<>();
        selectedPositions = new ArrayList<>();
        allOffers.add(new Offer(1, "Schnitzel", 8.6, Category.FOOD));
        allOffers.add(new Offer(2, "Hendl", 5.6, Category.FOOD));
        allOffers.add(new Offer(3, "Würstel", 5.6, Category.FOOD));

        allOffers.add(new Offer(4, "Pommes", 5.6, Category.SMALL_FOOD));

        allOffers.add(new Offer(5, "Bier 1/2", 4.5, Category.ALCOHOLIC_DRINK));
        allOffers.add(new Offer(6, "Bier 1/3", 4.5, Category.ALCOHOLIC_DRINK));
        allOffers.add(new Offer(7, "Wein 1/8", 4.5, Category.ALCOHOLIC_DRINK));

        allOffers.add(new Offer(8, "Cola", 2.5, Category.NON_ALCOHOLIC_DRINK));
        allOffers.add(new Offer(9, "Fanta", 2.5, Category.NON_ALCOHOLIC_DRINK));
        allOffers.add(new Offer(10, "Apfelsaft", 2.5, Category.NON_ALCOHOLIC_DRINK));

        allOffers.add(new Offer(12, "Kuchen", 5.6, Category.DESERT));

        filterList(category);
    }

    private void filterList(Category category){
        offers.clear();
        for (Offer offer:
                allOffers) {
            if(offer.category.equals(category)){
                offers.add(offer);
            }
        }

        try{
            displayOfferAdapter.notifyDataSetChanged();
        }catch(Exception ex){
            System.out.println("Not intialized yet");
            ex.printStackTrace();
        }
    }

    private void bindAdapterToGridView(GridView gridView) {
        Consumer<Offer> addOfferConsumer = x -> addOfferButtonClicked(x);
        displayOfferAdapter = new DisplayOfferAdapter(this, R.layout.display_offer_layout, offers, addOfferConsumer);
        gridView.setAdapter(displayOfferAdapter);
    }

    private void bindAdapterToListView(ListView listView){
        Consumer<Position> addPositionConsumer = x -> addPosition(x);
        Consumer<Position> deletePositionConsumer = x -> deletePosition(x);
        selectedOfferAdapter = new SelectedOfferAdapter(this, R.layout.selected_position_layout, selectedPositions, addPositionConsumer, deletePositionConsumer);
        listView.setAdapter(selectedOfferAdapter);
    }

    private void deletePosition(Position position) {
        if(position.amount == 1){
            selectedPositions.remove(position);
        }else{
            for (Position pos:
                 selectedPositions) {
               if(pos.equals(position)){
                   pos.amount--;
               }
            }
        }
        selectedOfferAdapter.notifyDataSetChanged();
    }

    public void addPosition(Position position){
        if(!selectedPositions.contains(position)){
            selectedPositions.add(position);
            System.out.println("it's a new item");
        }else{
            System.out.println("not a new item");
            for (Position pos:
                    selectedPositions) {
                if(pos.equals(position)){
                    pos.amount++;
                }
            }
        }
        selectedOfferAdapter.notifyDataSetChanged();
    }

    public void addOfferButtonClicked(Offer offer){
        System.out.println(offer.name + " clicked!");
        Product product = new Product();
        product.offer = offer;
        Position position = new Position();
        position.product = product;
        System.out.println("cur:");
        System.out.println(position);
        System.out.println("--");
        position.amount = 1;
        addPosition(position);
    }
}
