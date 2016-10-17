package ch.epfl.sweng.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.project.model.GPSPoint;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.MatchProvider;
import ch.epfl.sweng.project.model.Player;
//TODO Remove when tests are done
import ch.epfl.sweng.project.model.Rank;

/**
 * Your app's main activity.
 */
public final class MainActivity extends AppCompatActivity {
    private MatchProvider mProvider; //TODO convention de nommage pour les providers ? "m" pour match puis "Provider". A décider.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProvider = new MatchProvider();
    }

    public void createMatch(View view) {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        mProvider.stop();
        super.onStop();
    }
}