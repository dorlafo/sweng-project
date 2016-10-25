package ch.epfl.sweng.project.tools;


import android.location.Location;

public interface LocationProviderListener {
    void onLocationChanged(Location location);
}
