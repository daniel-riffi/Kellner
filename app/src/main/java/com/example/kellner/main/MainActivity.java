package com.example.kellner.main;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.example.kellner.R;
import com.example.kellner.Server;
import com.example.kellner.SettingsActivity;
import com.example.kellner.main.adapter.DisplayOfferAdapter;
import com.example.kellner.main.adapter.SelectedOfferAdapter;
import com.example.kellner.payment.PaymentSystemActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import at.orderlibrary.Category;
import at.orderlibrary.Offer;
import at.orderlibrary.Position;
import at.orderlibrary.Product;

public class MainActivity extends AppCompatActivity {
    private List<Offer> allOffers;
    private List<Offer> offers;
    private List<Position> selectedPositions;
    private GridView gridView;
    private ListView listView;
    private DisplayOfferAdapter displayOfferAdapter;
    private SelectedOfferAdapter selectedOfferAdapter;
    private Category category;
    private OfferDialogFragment offerDialogFragment;
    private final int RQ_PREFERENCES=12345;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        Button little_food = findViewById(R.id.btn_little_food);
        Button large_food = findViewById(R.id.btn_large_food);
        Button alcoholic_drinks = findViewById(R.id.btn_alcoholic_drinks);
        Button nonalcoholic_drinks = findViewById(R.id.btn_nonalcoholic_drinks);
        Button desert = findViewById(R.id.btn_desert);
        Button payment = findViewById(R.id.btnPayment);

        category = Category.NON_ALCOHOLIC_DRINK;
        nonalcoholic_drinks.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonalcoholic_drinks_colored));

        fillItemsList();
        initButtons(little_food, large_food, alcoholic_drinks, nonalcoholic_drinks, desert, payment);

        gridView = findViewById(R.id.gridView);
        listView = findViewById(R.id.listView);
        bindAdapterToGridView(gridView);
        bindAdapterToListView(listView);

        perms();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String ipAddress = prefs.getString("ipaddress", "empty");
        String port = prefs.getString("port", "empty");
        if(ipAddress.equals("empty") || port.equals("empty") || !connectToServer(ipAddress, port)){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, RQ_PREFERENCES);
        }

    }


    private boolean connectToServer(String ip, String port) {
        Server server = Server.getInstance();
        server.setIpAddress(ip);
        try {
            server.setPort(Integer.parseInt(port));
        }
        catch(NumberFormatException e){
            return false;
        }
        return server.connect();
    }


    private void initButtons(Button little_food, Button large_food, Button alcoholic_drinks, Button nonalcoholic_drinks, Button desert, Button payment){

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

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(MainActivity.this, PaymentSystemActivity.class);
                paymentIntent.putExtra("selectedPositions", (ArrayList<Position>) selectedPositions);
                startActivity(paymentIntent);
            }
        });
    }

    private void fillItemsList() {
        allOffers = new ArrayList<>();
        offers = new ArrayList<>();
        selectedPositions = new ArrayList<>();
        Server server=Server.getInstance();

        server.onOpen((obj)-> {
            server.readOffersFromServer((o) -> {
                allOffers = o;
                runOnUiThread(()->filterList(category));
            });
        });

    }
    private void perms(){
        if(checkSelfPermission(Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.INTERNET},0);
        }
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
        Consumer<Offer> longClick = x -> longClick(x);
        displayOfferAdapter = new DisplayOfferAdapter(this, R.layout.display_offer_layout, offers, addOfferConsumer, longClick);
        gridView.setAdapter(displayOfferAdapter);
    }

    private void longClick(Offer offer) {
        Consumer<Position> dialogClick = x -> addPosition(x, false);
        offerDialogFragment = new OfferDialogFragment(dialogClick);
        offerDialogFragment.setPosition(offerToPosition(offer));
        offerDialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void bindAdapterToListView(ListView listView){
        Consumer<Position> addPositionConsumer = x -> addPosition(x, true);
        Consumer<Position> deletePositionConsumer = x -> deletePosition(x, false);
        Consumer<Position> deleteLongPositionConsumer = x -> deletePosition(x, true);
        selectedOfferAdapter = new SelectedOfferAdapter(this, R.layout.selected_position_layout, selectedPositions, addPositionConsumer, deletePositionConsumer, deleteLongPositionConsumer);
        listView.setAdapter(selectedOfferAdapter);
    }

    private void deletePosition(Position position, boolean wb) {
        if(wb){
            selectedPositions.remove(position);
        }else if(position.amount == 1){
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

    public void addPosition(Position position, boolean wb){
        System.out.println(position.product.offer.toString() + " " + position.product.specialWish + "" + position.amount);
        if(!selectedPositions.contains(position)){
            System.out.println("This Position is completely new");
            selectedPositions.add(position);
        }else{
            for (Position pos:
                    selectedPositions) {
                if(pos.equals(position)){
                    System.out.println("Position equals a position in selectedPositions");
                    if(wb){
                        pos.amount++;
                    }else{
                        pos.amount += position.amount;
                    }
                }
            }
        }
        selectedOfferAdapter.notifyDataSetChanged();
    }

    public void addOfferButtonClicked(Offer offer){
        addPosition(offerToPosition(offer), false);
    }

    public Position offerToPosition(Offer offer){
        Product product = new Product();
        product.offer = offer;
        Position position = new Position();
        position.product = product;
        position.amount = 1;
        position.product.specialWish = "";
        return position;
    }
}

