package ch.epfl.sweng.jassatepfl;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.LocationProvider;
import ch.epfl.sweng.jassatepfl.tools.LocationProviderListener;
import ch.epfl.sweng.jassatepfl.tools.MatchStringifier;

/**
 * Activity displaying matches as markers on a Google Maps Fragment.
 * <br>
 * Clicking on a marker displays the match information and clicking on
 * the information window prompts the user to join the match.
 */
public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, LocationProviderListener {

    private GoogleMap matchMap;
    private LocationProvider locationProvider;
    private Match match;
    private LatLng userLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        createMap();

        locationProvider = new LocationProvider(this);
        locationProvider.setProviderListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationProvider.connectGoogleApiClient();
        locationProvider.startLocationUpdates();

        if (userLastLocation != null) {
            matchMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(userLastLocation, 15f, 0f, 0f)));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationProvider.stopLocationUpdates();
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapReady(GoogleMap googleMap) {
        matchMap = googleMap;
        matchMap.getUiSettings().setZoomControlsEnabled(true);
        if (locationProvider.locationPermissionIsGranted()) {
            matchMap.setMyLocationEnabled(true);
        }

        matchMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Context context = MapsActivity.this;

                LinearLayout infoWindow = new LinearLayout(context);
                infoWindow.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                infoWindow.addView(title);
                infoWindow.addView(snippet);

                return infoWindow;
            }
        });

        /**
         * When click on one game, opens AlertDialog
         * Provides oppotunity to join match or cancel.
         */
        matchMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle(R.string.join_match)
                        .setMessage(R.string.join_message)
                        .setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                final String matchID = marker.getTag().toString();
                                ref.child("matches").child(matchID)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                match = dataSnapshot.getValue(Match.class);
                                                DatabaseUtils.addPlayerToMatch(MapsActivity.this,
                                                        ref,
                                                        matchID,
                                                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                                        match);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.e("ERROR-DATABASE", databaseError.toString());
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing, goes back to MapsActivity
                            }
                        })
                        .show();
            }
        });

        displayNearbyMatches();
    }

    @Override
    public void onLocationChanged(Location location) {
        userLastLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void switchToList(View view) {
        Intent intent = new Intent(this, MatchListActivity.class);
        startActivity(intent);
    }

    private void createMap() {
        if (matchMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private void displayNearbyMatches() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("matches");

        ref.addChildEventListener(new ChildEventListener() {
            private final Map<String, Marker> markers = new HashMap<>();
            private final MatchStringifier stringifier = new MatchStringifier(MapsActivity.this);

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Match m = dataSnapshot.getValue(Match.class);
                markers.put(dataSnapshot.getKey(), createMarker(m));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                markers.get(dataSnapshot.getKey()).remove();
                markers.put(dataSnapshot.getKey(), createMarker(dataSnapshot.getValue(Match.class)));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                markers.get(dataSnapshot.getKey()).remove();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                markers.get(dataSnapshot.getKey()).remove();
                markers.put(dataSnapshot.getKey(), createMarker(dataSnapshot.getValue(Match.class)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            private Marker createMarker(Match match) {
                stringifier.setMatch(match);
                Marker marker = matchMap.addMarker(new MarkerOptions()
                        .position(match.getLocation().toLatLng())
                        .title(match.getDescription())
                        .snippet(stringifier.markerSnippet())
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                marker.setTag(match.getMatchID());
                return marker;
            }
        });
    }

}
