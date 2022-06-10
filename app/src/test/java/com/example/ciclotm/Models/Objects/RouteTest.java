package com.example.ciclotm.Models.Objects;

import static org.junit.Assert.*;

import org.junit.Test;

public class RouteTest {
    Route route = new Route();
    @Test
    public void calculateAverageSpeed() {
        double speedSum = 340.8;
        int samples = 80;
        double result = route.calculateAverageSpeed(speedSum,samples);
        assertEquals(result,4.26,0.1);
    }
}