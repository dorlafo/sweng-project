package ch.epfl.sweng.jassatepfl.tools;


import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Utility class that provides methods to access the current location.
 * <br>
 * Can be used in two ways: either implement the
 * {@link ch.epfl.sweng.jassatepfl.tools.LocationProviderListener LocationProviderListener}
 * interface to get real-time notifications when the location changes, or use the
 * {@link #getLastLocation()} method to get the current location.
 */
public final class LocationProvider implements ConnectionCallbacks, LocationListener {

    private final Activity callingActivity;
    private final PermissionHandler permissionHandler;
    private final boolean updatesRequested;

    private LocationProviderListener providerListener;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;

    /**
     * Constructs a new LocationProvider with the given activity.
     *
     * @param callingActivity  The activity using the provider
     * @param updatesRequested true if the location provider should update the location
     *                         in real time, false otherwise
     */
    public LocationProvider(Activity callingActivity, boolean updatesRequested) {
        this.callingActivity = callingActivity;
        this.updatesRequested = updatesRequested;

        permissionHandler = new PermissionHandler(callingActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionHandler.chamallowIsHere()) {
            permissionHandler.requestPermission();
        }

        buildGoogleApiClient();
        createLocationRequest();
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onConnected(@Nullable Bundle bundle) {
        if (permissionHandler.permissionIsGranted()) {
            if (updatesRequested) {
                startLocationUpdates();
            }
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                notifyListener(lastLocation);
            }
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
     * Checks whether the ACCESS_FINE_LOCATION permission has been granted.
     *
     * @return true if the current system version is lower than Marshmallow
     * (no runtime permission needed) or the runtime permission has been granted,
     * false otherwise.
     */
    public boolean locationPermissionIsGranted() {
        return permissionHandler.permissionIsGranted();
    }

    @SuppressWarnings({"MissingPermission"})
    public void setMockLocation(Location location) {
        LocationServices.FusedLocationApi.setMockMode(googleApiClient, true);
        LocationServices.FusedLocationApi.setMockLocation(googleApiClient, location);
    }

    /**
     * Builds a new googleApiClient to provide access to location services.
     */
    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(callingActivity)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create()
                .setInterval(15000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    /**
     * Starts the location updates, notifying the provider whenever
     * the location changes.
     */
    @SuppressWarnings({"MissingPermission"})
    private void startLocationUpdates() {
        if (permissionHandler.permissionIsGranted()) {
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
