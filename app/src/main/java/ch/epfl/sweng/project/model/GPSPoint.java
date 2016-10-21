package ch.epfl.sweng.project.model;


import com.google.android.gms.maps.model.LatLng;

/**
 * Class representing a geographical position.
 */
public class GPSPoint {
    private double latitude;
    private double longitude;

    public GPSPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Default constructor required for calls to DataSnapshot.getValue when using Firebase
     */
    public GPSPoint() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }
}
