package ch.epfl.sweng.project.tools;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Utility class that provides methods to access the current location.
 * <br>
 * Can be used in two ways: either implement the
 * {@link ch.epfl.sweng.project.tools.LocationProviderListener LocationProviderListener}
 * interface to get real-time notifications when the location changes, or use the
 * {@link #getLastLocation()} method to get the current location.
 */
public final class LocationProvider implements ConnectionCallbacks, LocationListener {

    private LocationProviderListener providerListener;

    private final Context context;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;

    /**
     * Constructs a new LocationProvider with the given context.
     *
     * @param context The context using the provider
     */
    public LocationProvider(Context context) {
        this.context = context;
        buildGoogleApiClient();
        createLocationRequest();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                notifyListener(lastLocation);
            }
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        notifyListener(lastLocation);
    }

    /**
     * Sets a listener for this LocationProvider.
     *
     * @param providerListener The listener
     */
    public void setProviderListener(LocationProviderListener providerListener) {
        this.providerListener = providerListener;
    }

    /**
     * Manually connects the googleApiClient to enable access to location services.
     */
    public void connectGoogleApiClient() {
        googleApiClient.connect();
    }

    /**
     * Stops location updates and disconnects the googleApiClient.
     */
    public void stopLocationUpdates() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    /**
     * Returns the current location of the user.
     *
     * @return The current location
     */
    public Location getLastLocation() {
        return lastLocation;
    }

    /**
     * Builds a new googleApiClient to provide access to location services.
     */
    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    /**
     * Starts the location updates, notifying the provider whenever
     * the location changes.
     */
    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    /**
     * Call to the provider's listener to provide new location.
     *
     * @param location The updated current location
     */
    private void notifyListener(Location location) {
        if (providerListener != null) {
            providerListener.onLocationChanged(location);
        }
    }

}
