package ch.epfl.sweng.project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.res.DummyMatchData; // TODO: delete when real data is used

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap matchMap;
    // private LatLng currentPositionCoord;

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
            public void onInfoWindowClick(Marker marker) {
                // TODO: make clicking on the info window join the match
            }
        });

        // TODO: change to use real matches
        displayNearbyMatches(DummyMatchData.dummyMatches());

        // Check if GPS permission has been granted, and request permission if not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            matchMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE:
                if (permissions.length == 1
                        && permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //matchMap.setMyLocationEnabled(true);
                }
        }
    }

    private void displayNearbyMatches(Iterable<Match> matches) {
        for (Match match : matches) {
            if (!match.isPrivateMatch()) {
                matchMap.addMarker(new MarkerOptions()
                        .position(match.getLocation())
                        .title(match.getDescription())
                        .snippet(markerSnippet(match))
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        }
    }

    private String markerSnippet(Match match) {
        Resources res = getResources();
        String newLine = System.getProperty("line.separator");

        StringBuilder builder = new StringBuilder(
                res.getString(R.string.snippet_match_rank))
                .append(match.getRank().getRank())
                .append(newLine)
                .append(res.getString(R.string.snippet_player_list))
                .append(listToString(match.getPlayers()))
                .append(newLine)
                .append(res.getString(R.string.snippet_game_variant))
                //builder.append(match.getGameVariant().toString()) TODO: implement this
                .append(newLine)
                .append(res.getString(R.string.snippet_expiration_date))
                .append(dateToStringCustom(match.getExpirationTime()));

        return builder.toString();
    }

    private String dateToStringCustom(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(
                getResources().getString(R.string.date_format),
                Locale.FRENCH);
        return dateFormat.format(date);
    }

    private String listToString(List<Player> players) {
        StringBuilder builder = new StringBuilder();
        Iterator<Player> playerIterator = players.iterator();

        while (playerIterator.hasNext()) {
            builder.append(playerIterator.next().toString());
            if (playerIterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
