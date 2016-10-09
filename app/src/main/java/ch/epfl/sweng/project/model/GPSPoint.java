package ch.epfl.sweng.project.model;

/**
 * Created by Amaury on 07/10/2016.
 */

final class GPSPoint {
    private double latitude;
    private double longitude;

    public GPSPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GPSPoint() {
        // Default constructor required for calls to DataSnapshot.getValue(gpsCoordinate.class)
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
