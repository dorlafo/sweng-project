package ch.epfl.sweng.jassatepfl;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.LocationProvider;
import ch.epfl.sweng.jassatepfl.tools.LocationProviderListener;
import ch.epfl.sweng.jassatepfl.tools.MatchStringifier;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;

/**
 * Activity displaying matches as markers on a Google Maps Fragment.
 * Clicking on a marker displays the match information and clicking on
 * the information window prompts the user to join the match.
 */
public class MapsActivity extends BaseActivityWithNavDrawer implements
        OnMapReadyCallback, LocationProviderListener {

    private GoogleMap matchMap;
    private LocationProvider locationProvider;
    private Match match;
    private LatLng userLastLocation;
    private ChildEventListener childEventListener;
    private static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            Log.d(TAG, "showLogin:getCurrentUser:null");
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            Log.d(TAG, "showLogin:getCurrentUser:NOTnull");
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_maps, drawer, false);
            drawer.addView(contentView, 0);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                getSupportActionBar().hide();
            }

            ImageButton menuButton = (ImageButton) findViewById(R.id.maps_menu_button);
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
                }
            });

            createMap();

            locationProvider = new LocationProvider(this, true);
            locationProvider.setProviderListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationProvider.connectGoogleApiClient();
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
         * Provides opportunity to join match or cancel.
         */
        matchMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle(R.string.dialog_join_match)
                        .setMessage(R.string.dialog_join_message)
                        .setPositiveButton(R.string.dialog_join_confirmation, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final String matchID = marker.getTag().toString();
                                dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES).child(matchID)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                match = dataSnapshot.getValue(Match.class);
                                                DatabaseUtils.addPlayerToMatch(MapsActivity.this,
                                                        dbRefWrapped,
                                                        matchID,
                                                        getUserSciper(),
                                                        match);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.e("ERROR-DATABASE", databaseError.toString());
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
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
        if (userLastLocation == null) {
            matchMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(new LatLng(location.getLatitude(),
                            location.getLongitude()), 15f, 0f, 0f)));
        }
        userLastLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(childEventListener != null) {
            dbRefWrapped.removeEventListener(childEventListener);
        }
    }

    private void createMap() {
        if (matchMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private void displayNearbyMatches() {
        childEventListener = new ChildEventListener() {
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
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                match.hasParticipantWithID(getUserSciper()) ? HUE_ORANGE : HUE_BLUE)));
                marker.setTag(match.getMatchID());
                return marker;
            }
        };
        dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES)
                .orderByChild("privateMatch").equalTo(false)
                .addChildEventListener(childEventListener);
    }

}
