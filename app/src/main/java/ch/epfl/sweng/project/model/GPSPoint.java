package ch.epfl.sweng.project.model;


import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

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

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null || other.getClass() != this.getClass()) {
            return false;
        }

        GPSPoint otherPoint = (GPSPoint) other;
        return this.latitude == otherPoint.latitude
                && this.longitude == otherPoint.longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

}
