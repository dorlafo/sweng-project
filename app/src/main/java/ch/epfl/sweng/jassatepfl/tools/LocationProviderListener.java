package ch.epfl.sweng.jassatepfl.tools;


import android.location.Location;

/**
 * Listener interface for a
 * {@link ch.epfl.sweng.jassatepfl.tools.LocationProvider LocationProvider}.
 */
public interface LocationProviderListener {

    /**
     * Method called by the LocationProvider whenever the location changes.
     * <br>
     * The parameter is the new location, given by the LocationProvider.
     *
     * @param location The new location
     */
    void onLocationChanged(Location location);

}
