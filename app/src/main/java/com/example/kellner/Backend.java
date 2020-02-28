package com.example.kellner;

import com.example.orderlibrary.Order;
import com.example.orderlibrary.Position;

import java.util.List;
import java.util.stream.Collectors;



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
