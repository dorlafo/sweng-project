package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.notification.InvitePlayer;
import ch.epfl.sweng.jassatepfl.stats.MatchStats;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.PlayerListAdapter;

public class WaitingPlayersActivity extends BaseActivityWithNavDrawer implements OnItemClickListener {

    private static final String TAG = WaitingPlayersActivity.class.getSimpleName();

    private PlayerListAdapter adapter;
    private Match match;
    private String matchId;
    private Player player;

    private ValueEventListener matchListener;
    private ChildEventListener pendingMatchListener;

    private int posInList;
    private int teamSelected;

    private boolean creator = false;

    private Map<String, Boolean> playersReady;

    private Button gameBtn;
    private Button readyBtn;
    private Button inviteBtn;

    private static final int INVITE_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            Log.d(TAG, "showLogin:getCurrentUser:null");
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            Log.d(TAG, "showLogin:getCurrentUser:notNull");

            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.activity_waiting_players, drawer, false);
            drawer.addView(contentView, 0);

            matchId = getIntent().getStringExtra("match_Id");
            match = Match.sentinelMatch();
            playersReady = new HashMap<>();

            gameBtn = (Button) findViewById(R.id.play);
            gameBtn.setVisibility(View.GONE);

            readyBtn = (Button) findViewById(R.id.ready_button);
            inviteBtn = (Button) findViewById(R.id.invite_button);

            adapter = new PlayerListAdapter(WaitingPlayersActivity.this,
                    R.layout.player_list_element, new ArrayList<Player>());

            ListView listView = (ListView) findViewById(android.R.id.list);
            listView.setOnItemClickListener(this);
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactFirebase();
        Intent startIntent = getIntent();

        /* Notification onClick handler.
         * Will display dialog Box depending on the notification received.
         */
        if (startIntent.hasExtra("notif")) {
            matchId = getIntent().getStringExtra("match_Id");
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
                    dbRefWrapped.child(DatabaseUtils.DATABASE_PLAYERS)
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
                    dbRefWrapped.child(DatabaseUtils.DATABASE_PLAYERS)
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
    public void onPause() {
        super.onPause();
        removeListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == INVITE_CODE) {
                int playerNum = data.getIntExtra("players_added", 0);
                for (int i = 0; i < playerNum; i++) {
                    String sciper = data.getStringExtra("player" + i);
                    dbRefWrapped.child(DatabaseUtils.DATABASE_PLAYERS)
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
        removeListener();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (creator) {
            final Player p = adapter.getItem(position);
            if (p != null) {
                teamSelected = match.teamNbForPlayer(p);
                int nbTeam = match.getGameVariant().getNumberOfTeam();
                CharSequence[] teams = new CharSequence[nbTeam];
                for (int i = 0; i < nbTeam; ++i) {
                    teams[i] = "Team " + Integer.toString(i + 1);
                }
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_select_team) + p.toString() + " :")
                        .setSingleChoiceItems(teams, teamSelected, new DialogInterface.OnClickListener() {

                            /**
                             * This method will be invoked when a button in the dialog is clicked.
                             *
                             * @param dialog The dialog that received the click.
                             * @param which  The button that was clicked (e.g.
                             *               {@link DialogInterface#BUTTON1}) or the position
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                teamSelected = which;
                            }
                        })
                        .setPositiveButton(R.string.dialog_team_selection_confirmation,
                                new DialogInterface.OnClickListener() {
                                    /**
                                     * This method will be invoked when a button in the dialog is clicked.
                                     *
                                     * @param dialog The dialog that received the click.
                                     * @param which  The button that was clicked (e.g.
                                     *               {@link DialogInterface#BUTTON1}) or the position
                                     */
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (teamSelected != (match.teamNbForPlayer(p))) {
                                            match.setTeam(teamSelected, p.getID());
                                            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES)
                                                    .child(matchId).setValue(match);
                                        }
                                    }
                                })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            /**
                             * This method will be invoked when a button in the dialog is clicked.
                             *
                             * @param dialog The dialog that received the click.
                             * @param which  The button that was clicked (e.g.
                             *               {@link DialogInterface#BUTTON1}) or the position
                             */
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    /**
     * When button "LEAVE MATCH" is pressed, the current user is removed from the match player list
     * send him back to main menu.
     *
     * @param view General view
     */
    public void leaveMatch(View view) {
        match.removePlayerById(new Player.PlayerID(getUserSciper()));

        dbRefWrapped.child(DatabaseUtils.DATABASE_PENDING_MATCHES)
                .child(matchId).child(getUserSciper()).removeValue();

        if (match.getPlayers().size() == 0) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES).child(matchId).removeValue();
        } else {
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES).child(matchId).setValue(match);
        }
        Intent backToMain = new Intent(this, MainActivity.class);
        startActivity(backToMain);
        finish();
    }

    /**
     * @param view General view
     */
    public void userIsReady(View view) {
        dbRefWrapped.child(DatabaseUtils.DATABASE_PENDING_MATCHES)
                .child(matchId).child(getUserSciper()).setValue(true);
    }

    /**
     * Redirects user to InvitePlayerToMatchActivity when
     * clicks on "Invite" button
     *
     * @param view General view
     */
    public void invitePlayers(View view) {
        startActivityForResult(new Intent(this, InvitePlayerToMatchActivity.class), INVITE_CODE);
    }

    public void goToMatch(View view) {
        removeListener();

        match.setStatus(Match.MatchStatus.ACTIVE);
        dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES).child(matchId).setValue(match);
        dbRefWrapped.child(DatabaseUtils.DATABASE_PENDING_MATCHES).child(matchId).removeValue();

        MatchStats matchStats = new MatchStats(match);
        dbRefWrapped.child(DatabaseUtils.DATABASE_MATCH_STATS).child(matchId).setValue(matchStats);

        goToGameActivity();
    }

    private void contactFirebase() {
        matchListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                match = dataSnapshot.getValue(Match.class);
                Log.d(TAG, "matchListener:onDataChange:dataSnapshot:" + dataSnapshot.toString());
                if (match != null) {
                    if (match.getMatchStatus().equals(Match.MatchStatus.ACTIVE)) {
                        goToGameActivity();
                    }
                    if (match.createdBy().getID().toString().equals(getUserSciper())) {
                        creator = true;
                        gameBtn.setVisibility(View.VISIBLE);
                    } else {
                        creator = false;
                        gameBtn.setVisibility(View.GONE);
                    }

                    TextView description = (TextView) findViewById(R.id.match_description);
                    description.setText(match.getDescription());

                    TextView variant = (TextView) findViewById(R.id.match_variant);
                    String gameVariant = String.format(getString(R.string.wait_field_match_variant),
                            match.getGameVariant().toString());
                    variant.setText(gameVariant);

                    posInList = match.getPlayerIndex(new Player.PlayerID(getUserSciper()));
                    if (posInList != -1) {
                        if (match.matchFull()) {
                            if (!match.teamAssignmentIsCorrect() && creator) {
                                Toast.makeText(WaitingPlayersActivity.this, R.string.toast_team_assignment_incorrect, Toast.LENGTH_SHORT)
                                        .show();
                            }
                            inviteBtn.setEnabled(false);
                        } else {
                            inviteBtn.setEnabled(true);
                        }

                        if (playersReady.size() == match.getMaxPlayerNumber() && match.teamAssignmentIsCorrect()) {
                            gameBtn.setEnabled(true);
                        } else {
                            gameBtn.setEnabled(false);
                        }
                    }
                    modifyListAdapter();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR-DATABASE", databaseError.toString());
            }
        };
        dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES)
                .child(matchId)
                .addValueEventListener(matchListener);

        pendingMatchListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "pendingMatchListener:onChildAdded:dataSnapshot:" + dataSnapshot.toString());
                updateButtonStatus(dataSnapshot.getKey(), (boolean) dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "pendingMatchListener:onChildChanged:dataSnapshot:" + dataSnapshot.toString());
                updateButtonStatus(dataSnapshot.getKey(), (boolean) dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "pendingMatchListener:onChildRemoved:dataSnapshot:" + dataSnapshot.toString());
                playersReady.remove(dataSnapshot.getKey());
                modifyListAdapter();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //TODO: check if this is indeed not used
                Log.d(TAG, "pendingMatchListener:onChildMoved:dataSnapshot:" + dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR-DATABASE", databaseError.toString());
            }
        };
        dbRefWrapped.child(DatabaseUtils.DATABASE_PENDING_MATCHES)
                .child(matchId)
                .addChildEventListener(pendingMatchListener);
    }

    private void removeListener() {
        if (matchListener != null) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_MATCHES)
                    .child(matchId)
                    .removeEventListener(matchListener);
        }
        if (pendingMatchListener != null) {
            dbRefWrapped.child(DatabaseUtils.DATABASE_PENDING_MATCHES)
                    .child(matchId)
                    .removeEventListener(pendingMatchListener);
        }
    }

    private void modifyListAdapter() {
        adapter.refreshData(match.getPlayers(), match, playersReady);
    }

    private void updateButtonStatus(String key, boolean value) {
        playersReady.put(key, value);
        if (key.equals(getUserSciper())) {
            readyBtn.setEnabled(!value);
        }
        gameBtn.setEnabled(allPlayersReady(playersReady) &&
                match.teamAssignmentIsCorrect());
        modifyListAdapter();
    }

    private boolean allPlayersReady(Map<String, Boolean> playersR) {
        if(playersR.size() == match.getMaxPlayerNumber()) {
            for(String k : playersR.keySet()) {
                if(!playersR.get(k)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void goToGameActivity() {
        Intent goToGameActivity = new Intent(this, GameActivity.class);
        goToGameActivity.putExtra("match_Id", matchId);
        goToGameActivity.putExtra("mode", "online");
        startActivity(goToGameActivity);
        finish();
    }

}
