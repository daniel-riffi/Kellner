package com.example.kellner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;



import static org.junit.Assert.*;

public class BackendTest {

    @Before
    public void setUp() throws Exception {
        //UnitTestVariables.ResetVariables();
    }

    @Test
    public void calculatePriceOfOrder() {
        //System.out.println("hallo");
        double expected = 97.97;
        //ouble actual = Backend.CalculatePriceOfOrder(UnitTestVariables.order1);
        //Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void calculatePriceOfPosition_1() {
        double expected = 29.97;
        //double actual = Backend.CalculatePriceOfPosition(UnitTestVariables.position1);
        //Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void calculatePriceOfPosition_2() {
        double expected = 45;
        //double actual = Backend.CalculatePriceOfPosition(UnitTestVariables.position2);
        //Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void calculatePriceOfPositions_1() {
        double expected = 97.97;
        //double actual = Backend.CalculatePriceOfPositions(UnitTestVariables.positions);
        //Assert.assertEquals(actual, expected, 0.001);
    }

    @Test
    public void calculatePriceOfPositions_2() {
        double expected = 74.97;
        //double actual = Backend.CalculatePriceOfPositions(Arrays.asList(UnitTestVariables.position1, UnitTestVariables.position2));
        //Assert.assertEquals(actual, expected, 0.001);
    }
}