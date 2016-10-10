package ch.epfl.sweng.project;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.Manifest;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sweng.project.model.Match;

// Used for test purposes
import ch.epfl.sweng.project.res.DummyMatchData;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap matchMap;
    private LatLng currentPositionCoord;

    private final int MY_LOCATION_REQUEST_CODE = 39;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        matchMap = googleMap;
        matchMap.getUiSettings().setZoomControlsEnabled(true);

        // TODO: change to use real matches
        displayNearbyMatches(DummyMatchData.dummyMatches());

        // Check if GPS permission has been granted, and request permission if not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            matchMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE:
                if (permissions.length == 1 &&
                        permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //matchMap.setMyLocationEnabled(true);
                }
        }
    }

    public void displayNearbyMatches(Iterable<Match> matches) {
        for (Match match : matches) {
            if (!match.isPrivateMatch()) {
                String matchID = Long.toString(match.getID().getID());
                matchMap.addMarker(new MarkerOptions().position(match.getLocation()).title(matchID)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        }
    }
}
