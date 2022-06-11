package com.example.ciclotm.Services;

import com.example.ciclotm.Constants;

public class DistanceCalculator {

    public double DistanceCalculation(double lat1, double lon1, double lat2, double lon2) {
        double Radius = Constants.EARTH_RADIUS;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }
}
