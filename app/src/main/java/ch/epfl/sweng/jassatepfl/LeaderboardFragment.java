package ch.epfl.sweng.jassatepfl;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.PlayerListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Scoreboard Fragment for StatsActivity
 */
public class LeaderboardFragment extends Fragment {
    @Inject
    public DBReferenceWrapper dbRefWrapped;
    @Inject
    public FirebaseAuth fAuth;

    private PlayerListAdapter adapter;
    private ListView playerListView;
    private List<Player> playerList;
    private ChildEventListener playerListener;

    public LeaderboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().graph().inject(this);
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

        adapter = new PlayerListAdapter(getContext(), R.layout.player_list_element, new ArrayList<Player>());
        playerListView.setAdapter(adapter);

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
                if(playerList.contains(player)) {
                    playerList.remove(player);
                }
                playerList.add(player);
                Collections.sort(playerList, new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return Integer.compare(o2.getQuote(), o1.getQuote());
                    }
                });
                modifyListAdapter();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Player player = dataSnapshot.getValue(Player.class);
                playerList.remove(player);
                modifyListAdapter();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //Nothing to do
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR-DATABASE", databaseError.toString());
            }
        };
        dbRefWrapped.child(DatabaseUtils.DATABASE_PLAYERS)
                .orderByChild(DatabaseUtils.DATABASE_PLAYERS_QUOTE)
                .limitToFirst(50)
                .addChildEventListener(playerListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(playerListener != null) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_PLAYERS)
                    .orderByChild(DatabaseUtils.DATABASE_PLAYERS_QUOTE)
                    .limitToFirst(50)
                    .removeEventListener(playerListener);
        }
    }

    /**
     * Updates Match list adapter
     */
    private void modifyListAdapter() {
        adapter.refreshData(playerList);
    }
}
