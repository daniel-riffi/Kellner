package com.example.kellner;

import at.orderlibrary.Order;

public class Backend {

    public double CalculatePriceOfOrder(Order order){
        return order.positions.stream().map(x -> x.product.offer.price).
    }
}
