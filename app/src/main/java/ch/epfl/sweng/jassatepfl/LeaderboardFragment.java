package ch.epfl.sweng.jassatepfl;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.PlayerListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Scoreboard Fragment for StatsActivity
 */
public class LeaderboardFragment extends Fragment {
    private DBReferenceWrapper dbRefWrapped;
    private PlayerListAdapter adapter;
    private ListView playerListView;
    private List<Player> playerList;
    private ChildEventListener playerListener;

    public LeaderboardFragment() {
    }

    public Fragment setReference(DBReferenceWrapper dbRefWrapped) {
        this.dbRefWrapped = dbRefWrapped;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_leaderboard, container, false);

        TextView emptyList = new TextView(getContext());
        emptyList.setText(R.string.loading_leaderboard);
        emptyList.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        emptyList.setTextColor(Color.BLACK);
        emptyList.setTextSize(20);

        playerList = new ArrayList<>();

        playerListView = (ListView) rootView.findViewById(R.id.leaderboard_list);
        ((ViewGroup) playerListView.getParent()).addView(emptyList);
        playerListView.setEmptyView(emptyList);
        contactFirebase();
        return rootView;
    }

    private void contactFirebase() {
        playerListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Player player = dataSnapshot.getValue(Player.class);
                playerList.add(0, player);
                modifyListAdapter();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Player player = dataSnapshot.getValue(Player.class);
                playerList.add(0, player);
                modifyListAdapter();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Player player = dataSnapshot.getValue(Player.class);
                playerList.add(0, player);
                modifyListAdapter();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Player player = dataSnapshot.getValue(Player.class);
                playerList.add(0, player);
                modifyListAdapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Do nothing
            }
        };
        dbRefWrapped.child("players")
                .orderByChild("quote")
                .limitToFirst(50)
                .addChildEventListener(playerListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbRefWrapped.child("players").removeEventListener(playerListener);
    }

    /**
     * Updates Match list adapter
     */
    private void modifyListAdapter() {
        adapter = new PlayerListAdapter(getContext(), R.layout.player_list_element, playerList);
        playerListView.setAdapter(adapter);
    }
}
