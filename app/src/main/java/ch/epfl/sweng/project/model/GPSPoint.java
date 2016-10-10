package ch.epfl.sweng.project.model;

/**
 * Created by vinz on 10/10/16.
 */

public class GPSPoint {
    private double latitude;
    private double longitude;

    public GPSPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GPSPoint() { }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
