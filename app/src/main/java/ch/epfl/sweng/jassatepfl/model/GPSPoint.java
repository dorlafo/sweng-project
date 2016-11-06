package ch.epfl.sweng.jassatepfl.model;


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import static java.lang.Double.doubleToLongBits;

/**
 * Class representing a geographical position.
 */
public class GPSPoint {

    private double latitude;
    private double longitude;

    /**
     * Constructs a new GPSPoint with the given coordinates.
     *
     * @param latitude  The latitude of the point
     * @param longitude The longitude of the point
     */
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
        return doubleToLongBits(this.latitude) == doubleToLongBits(otherPoint.latitude)
                && doubleToLongBits(this.longitude) == doubleToLongBits(otherPoint.longitude);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    /**
     * Converts and returns the instance of GPSPoint to a
     * {@link com.google.android.gms.maps.model.LatLng LatLng}.
     *
     * @return The LatLng with the same coordinates as the GPSPoint
     */
    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

}
