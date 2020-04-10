package com.example.kellner.main;



import at.orderlibrary.*;

import java.util.List;


public class Backend {

    public static double CalculatePriceOfOrder(Order order){
        return CalculatePriceOfPositions(order.positions);
    }

    public static double CalculatePriceOfPosition(Position position){
        return position.product.offer.price * position.amount;
    }

    public static double CalculatePriceOfPositions(List<Position> positionList){
        return positionList.stream()
                    .mapToDouble(Backend::CalculatePriceOfPosition)
                    .sum();
    }
}
