package com.greenmile.commons.util;

public class Haversine {
    private static final int EARTH_RADIUS = 6371;

    public static double distance(String startLat, String startLong,
                                  String endLat, String endLong) {

        double stLt = Double.parseDouble(startLat);
        double stLg = Double.parseDouble(startLong);
        double edLt = Double.parseDouble(endLat);
        double edLg = Double.parseDouble(endLong);

        double dLat  = Math.toRadians((edLt - stLt));
        double dLong = Math.toRadians((edLg - stLg));
        double startLt = Math.toRadians(stLt);
        double endLt   = Math.toRadians(edLt);

        double a = haversin(dLat) + Math.cos(startLt) * Math.cos(endLt) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
