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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.notification.InvitePlayer;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.PlayerListAdapter;

public class WaitingPlayersActivity extends BaseActivityWithNavDrawer {

    private String sciper;
    private Match match;
    private String matchId;
    private Player player;

    private ChildEventListener pendingMatchesListener;
    private ChildEventListener childEventListener;
    private ValueEventListener valueEventListener;

    private TextView variant;
    private TextView description;
    private ListView listView;

    private int playersReady = 0;
    private int posInList;

    private static final int INVITE_CODE = 42;
    private List<Player> playerList;

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
        description = (TextView) findViewById(R.id.match_description);
        variant = (TextView) findViewById(R.id.match_variant);
        listView = (ListView) findViewById(android.R.id.list);
        listView.setEmptyView(findViewById(android.R.id.empty));
        sciper = getUserSciper();

        playerList = new ArrayList<>();
        matchId = getIntent().getStringExtra("match_Id");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                match = dataSnapshot.getValue(Match.class);
                if(match != null) {
                    description.setText(match.getDescription());
                    variant.setText(match.getGameVariant().toString());

                    List<Player> players = match.getPlayers();
                    for (int i = 0; i < players.size(); ++i) {
                        if (players.get(i).getID().equals(new Player.PlayerID(sciper))) {
                            posInList = i;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR-DATABASE", databaseError.toString());
            }
        };
        dbRefWrapped.child("matches").child(matchId).addValueEventListener(valueEventListener);
        contactFirebase();

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
            switch (startIntent.getStringExtra("notif")) {
                case "matchfull":
                    new AlertDialog.Builder(WaitingPlayersActivity.this)
                            .setTitle(R.string.error_match_full)
                            .setMessage("Match: " + match.getDescription())
                            .show();
                    startIntent.removeExtra("notif");
                    startIntent.removeExtra("match_Id");
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
                    startIntent.removeExtra("match_Id");
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
                    startIntent.removeExtra("match_Id");
                    startIntent.removeExtra("sciper");
                    break;
                case "invite":
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.dialog_join_match)
                            .setMessage(R.string.dialog_join_message)
                            .setPositiveButton(R.string.dialog_join_confirmation, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseUtils.addPlayerToMatch(WaitingPlayersActivity.this,
                                            WaitingPlayersActivity.this.dbRefWrapped,
                                            matchId,
                                            fAuth.getCurrentUser().getDisplayName(),
                                            match);
                                }
                            })
                            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing, goes back to ListMatchActivity
                                }
                            })
                            .show();
                    startIntent.removeExtra("notif");
                    startIntent.removeExtra("match_Id");
                    startIntent.removeExtra("sciper");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == INVITE_CODE) {
                int playerNum = data.getIntExtra("players_added", 0);
                for (int i = 0; i < playerNum; i++) {
                    String sciper = data.getStringExtra("player" + i);
                    dbRefWrapped.child("players")
                            .child(sciper)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Player player = dataSnapshot.getValue(Player.class);
                                    new InvitePlayer(player).execute(matchId);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("ERROR-DATABASE", databaseError.toString());
                                }
                            });
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbRefWrapped.removeEventListener(pendingMatchesListener);
        dbRefWrapped.removeEventListener(childEventListener);
    }

    /**
     * When button "LEAVE MATCH" is pressed, the current user is removed from the match player list
     * send him back to main menu.
     *
     * @param view General view
     */
    public void leaveMatch(View view) {
        dbRefWrapped.removeEventListener(valueEventListener);
        try {
            match.removePlayerById(new Player.PlayerID(sciper));
        } catch (IllegalStateException e) {
            Log.e("Illegal State Exception", e.getMessage());
            return;
        }
        dbRefWrapped.child("pendingMatches").child(matchId).child(Integer.toString(posInList)).removeValue();
        Intent backToMain = new Intent(this, MainActivity.class);
        startActivity(backToMain);
        if(match.getPlayers().size() == 0) {
            dbRefWrapped.child("matches").child(matchId).removeValue();
        } else {
            dbRefWrapped.child("matches").child(matchId).setValue(match);
        }
    }

    /**
     * When all user clicks on "start match" button it launches the game.
     *
     * @param view General view
     */
    public void userIsReady(View view) {
        dbRefWrapped.child("pendingMatches").child(matchId).child(Integer.toString(posInList)).setValue(true);
        Button ready = (Button) findViewById(R.id.ready);
        ready.setEnabled(false);
    }

    /**
     * Ridirects user to InvitePlayerToMatchActivity when
     * clicks on "Invite" button
     *
     * @param view General view
     */
    public void invitePlayers(View view) {
        startActivityForResult(new Intent(this, InvitePlayerToMatchActivity.class), INVITE_CODE);
    }

    public void goToMatch(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Feature missing")
                .setMessage("will move to new activity")
                .show();
        dbRefWrapped.child("pendingMatches").child(matchId).removeEventListener(pendingMatchesListener);
        // TODO: backToList.putExtra("match_Id", matchId);
        // TODO: Create intent for next activity
        // TODO: In new activity, delete pendingMatches
    }

    private void contactFirebase() {
        childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Player player = dataSnapshot.getValue(Player.class);
                        playerList.add(player);
                        modifyListAdapter();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Player player = dataSnapshot.getValue(Player.class);
                        playerList.remove(player);
                        modifyListAdapter();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
        dbRefWrapped.child("matches")
                .child(matchId).child("players")
                .addChildEventListener(childEventListener);
    }

    private void modifyListAdapter() {
        PlayerListAdapter adapter = new PlayerListAdapter(WaitingPlayersActivity.this, R.layout.player_list_element, playerList);
        listView.setAdapter(adapter);
    }

}
