package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.PlayerListAdapterForMatch;

public class WaitingPlayersActivity extends BaseActivityWithNavDrawer {

    private String sciper;
    private Match match;
    private String matchId;
    private Player player;

    private ChildEventListener pendingMatchesListener;

    private TextView variant;
    private TextView description;
    private ListView listView;

    private int playersReady = 0;
    private int posInList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_waiting_players, drawer, false);
        drawer.addView(contentView, 0);
    }

    // TODO: implement pendingMatches deletion on server
    // TODO: on tap on player tile, show his stats
    @Override
    protected void onResume() {
        super.onResume();
        matchId = getIntent().getStringExtra("MATCH_ID");
        description = (TextView) findViewById(R.id.match_description);
        variant = (TextView) findViewById(R.id.match_variant);

        listView = (ListView) findViewById(android.R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));

        sciper = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        PlayerListAdapterForMatch pAdapter = new PlayerListAdapterForMatch(this, matchId);
        listView.setAdapter(pAdapter);
        dbRefWrapped.child("matches").child(matchId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                match = dataSnapshot.getValue(Match.class);
                description.setText(match.getDescription());
                variant.setText(match.getGameVariant().toString());

                List<Player> players = match.getPlayers();
                for (int i = 0; i < players.size(); ++i) {
                    if (players.get(i).getID().equals(new Player.PlayerID(sciper))) {
                        posInList = i;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        pendingMatchesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int pos = Integer.parseInt(dataSnapshot.getKey());
                boolean ready = dataSnapshot.getValue(Boolean.class);
                if (ready) {
                    listView.getChildAt(pos).setBackgroundColor(0xFF00FF00);
                    playersReady += 1;
                }
                if (playersReady == match.getMaxPlayerNumber()) {
                    Button game = (Button) findViewById(R.id.play);
                    game.setEnabled(true);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbRefWrapped.child("pendingMatches").child(matchId).addChildEventListener(pendingMatchesListener);

        Intent startIntent = getIntent();

        /* Notification onClick handler.
         * Will display dialog Box depending on the notification received.
         */
        if (startIntent.hasExtra("notif")) {
            final String matchID = startIntent.getStringExtra("matchId");
            switch (startIntent.getStringExtra("notif")) {
                case "matchfull":
                    dbRefWrapped.child("matches").child(startIntent.getStringExtra("matchId"))
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Match match = dataSnapshot.getValue(Match.class);
                                    new AlertDialog.Builder(WaitingPlayersActivity.this)
                                            .setTitle(R.string.error_match_full)
                                            .setMessage("Match: " + match.getDescription())
                                            .show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("ERROR-DATABASE", databaseError.toString());
                                }
                            });
                    startIntent.removeExtra("notif");
                    startIntent.removeExtra("matchId");
                    break;
                case "playerjoined":
                    dbRefWrapped.child("players")
                            .child(startIntent.getStringExtra("sciper"))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    player = dataSnapshot.getValue(Player.class);
                                    new AlertDialog.Builder(WaitingPlayersActivity.this)
                                            .setTitle(R.string.notification_player_joined)
                                            .setMessage(player.getFirstName() + " has join the match")
                                            .show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("ERROR-DATABASE", databaseError.toString());
                                }
                            });

                    startIntent.removeExtra("notif");
                    startIntent.removeExtra("matchId");
                    startIntent.removeExtra("sciper");
                    break;
                case "playerleft":
                    dbRefWrapped.child("players")
                            .child(startIntent.getStringExtra("sciper"))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    player = dataSnapshot.getValue(Player.class);
                                    new AlertDialog.Builder(WaitingPlayersActivity.this)
                                            .setTitle(R.string.notification_player_left)
                                            .setMessage(player.getFirstName() + " has left the match")
                                            .show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("ERROR-DATABASE", databaseError.toString());
                                }
                            });

                    startIntent.removeExtra("notif");
                    startIntent.removeExtra("matchId");
                    startIntent.removeExtra("sciper");
                    break;
                case "invite":
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.dialog_join_match)
                            .setMessage(R.string.dialog_join_message)
                            .setPositiveButton(R.string.dialog_join_confirmation, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    WaitingPlayersActivity.this.dbRefWrapped.child("matches").child(matchID)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    match = dataSnapshot.getValue(Match.class);
                                                    DatabaseUtils.addPlayerToMatch(WaitingPlayersActivity.this,
                                                            WaitingPlayersActivity.this.dbRefWrapped,
                                                            matchID,
                                                            fAuth.getCurrentUser().getDisplayName(),
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
                                    // Do nothing, goes back to ListMatchActivity
                                }
                            })
                            .show();
                    startIntent.removeExtra("notif");
                    startIntent.removeExtra("matchId");
                    startIntent.removeExtra("sciper");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * When button "LEAVE MATCH" is pressed, the current user is removed from the match player list
     * send him back to main menu.
     */
    public void leaveMatch(View view) {
        match.removePlayerById(new Player.PlayerID(sciper));
        dbRefWrapped.child("matches").child(matchId).setValue(match);
        dbRefWrapped.child("pendingMatches").child(matchId).child(Integer.toString(posInList)).removeValue();
        Intent backToList = new Intent(this, MatchListActivity.class);
        startActivity(backToList);
    }

    /**
     * When all user clicks on "start match" button it launches the game.
     */
    public void userIsReady(View view) {
        dbRefWrapped.child("pendingMatches").child(matchId).child(Integer.toString(posInList)).setValue(true);
        Button ready = (Button) findViewById(R.id.ready);
        ready.setEnabled(false);
    }

    public void goToMatch(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Feature missing")
                .setMessage("will move to new activity")
                .show();
        dbRefWrapped.child("pendingMatches").child(matchId).removeEventListener(pendingMatchesListener);
        // TODO: backToList.putExtra("MATCH_ID", matchId);
        // TODO: Create intent for next activity
        // TODO: In new activity, delete pendingMatches
    }

}
