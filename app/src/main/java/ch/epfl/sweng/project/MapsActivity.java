package ch.epfl.sweng.project;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import ch.epfl.sweng.project.error.ErrorHandlerUtils;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.tools.LocationProvider;
import ch.epfl.sweng.project.tools.LocationProviderListener;
import ch.epfl.sweng.project.tools.MatchStringifier;

/**
 * Activity displaying matches as markers on a Google Maps Fragment.
 * <p>
 * Clicking on a marker displays the match information.
 */
public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, LocationProviderListener {

    private static final int MY_LOCATION_REQUEST_CODE = 39;

    private GoogleMap matchMap;
    private LocationProvider locationProvider;
    private String sciper;
    private Player player;
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        createMap();

        locationProvider = new LocationProvider(this);
        locationProvider.setProviderListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createMap();
        locationProvider.connectGoogleApiClient();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationProvider.stopLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        matchMap = googleMap;
        matchMap.getUiSettings().setZoomControlsEnabled(true);
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
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.e("ERROR-DATABASE", databaseError.toString());
                                            }
                                        });
                                sciper = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                                FirebaseDatabase.getInstance().getReference()
                                        .child("players")
                                        .child(sciper)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                player = dataSnapshot.getValue(Player.class);
                                                try {
                                                    match.addPlayer(player);
                                                    ref.child("matches").child(matchID).setValue(match);
                                                    Intent moveToMatchActivity = new Intent(MapsActivity.this, MatchActivity.class);
                                                    getIntent().putExtra("MATCH_ID", matchID);
                                                    startActivity(moveToMatchActivity);
                                                } catch (IllegalStateException e) {
                                                    ErrorHandlerUtils.sendErrorMessage(MapsActivity.this, R.string.match_is_full, "Sorry, desired match is full");
                                                } catch (IllegalAccessException a) {
                                                    ErrorHandlerUtils.sendErrorMessage(MapsActivity.this, R.string.cannot_join_match, "You are already signed into that Match");
                                                }
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
                                // Do nothing, goes back to ListMatchActivity
                            }
                        })
                        .show();
            }
        });

        //displayNearbyMatches(DummyMatchData.dummyMatches()); Do not touch
        displayNearbyMatches();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                matchMap.setMyLocationEnabled(true);
            }
        } else {
            matchMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE:
                if (permissions.length == 1 &&
                        permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        matchMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng userCoordinates = new LatLng(location.getLatitude(),
                location.getLongitude());

        matchMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition(userCoordinates, 15f, 0f, 0f)));
    }

    public void switchToList(View view) {
        Intent intent = new Intent(this, MatchListActivity.class);
        startActivity(intent);
    }

    private void createMap() {
        if (matchMap == null) {
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, R.string.request_rationale, Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }
        }
    }

    private void displayNearbyMatches(Iterable<Match> matches) {
        MatchStringifier stringifier = new MatchStringifier(MapsActivity.this);
        for (Match match : matches) {
            if (!match.isPrivateMatch()) {
                stringifier.setMatch(match);
                Marker marker = matchMap.addMarker(new MarkerOptions()
                        .position(match.getLocation().toLatLng())
                        .title(match.getDescription())
                        .snippet(stringifier.markerSnippet())
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                marker.setTag(match.getMatchID());
            }
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

            private Marker createMarker(Match m) {
                stringifier.setMatch(m);
                Marker marker = matchMap.addMarker(new MarkerOptions()
                        .position(m.getLocation().toLatLng())
                        .title(m.getDescription())
                        .snippet(stringifier.markerSnippet())
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                marker.setTag(m.getMatchID());
                return marker;
            }
        });
    }

}
