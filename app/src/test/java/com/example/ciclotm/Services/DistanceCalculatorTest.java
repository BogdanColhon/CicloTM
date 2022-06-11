package com.example.ciclotm.Services;

import static org.junit.Assert.*;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

public class DistanceCalculatorTest {

    @Test
    public void distanceCalculation() {
        LatLng first = new LatLng(45.706687592762556, 21.234532818161675);
        LatLng second = new LatLng(45.70660611408313, 21.234972700447383);
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        double result = distanceCalculator.DistanceCalculation(first.latitude,first.longitude,second.latitude, second.longitude);
        assertEquals(result,0.05,0.03);
    }
}