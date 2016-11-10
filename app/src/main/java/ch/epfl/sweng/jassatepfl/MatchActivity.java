package ch.epfl.sweng.jassatepfl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;


public class MatchActivity extends BaseActivity {

    private Match match;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent startIntent = getIntent();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        /* Notification onClick handler.
         * Will display dialog Box depending on the notification received.
         */
        if(startIntent.hasExtra("notif")) {
            final String matchID = startIntent.getStringExtra("matchId");
            switch(startIntent.getStringExtra("notif")) {
                case "matchfull":
                    ref.child("matches").child(startIntent.getStringExtra("matchId"))
                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Match match = dataSnapshot.getValue(Match.class);
                                    new AlertDialog.Builder(MatchActivity.this)
                                            .setTitle(R.string.match_is_full)
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
                    ref.child("players")
                            .child(startIntent.getStringExtra("sciper"))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    player = dataSnapshot.getValue(Player.class);
                                    new AlertDialog.Builder(MatchActivity.this)
                                            .setTitle(R.string.player_joined)
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
                    ref.child("players")
                            .child(startIntent.getStringExtra("sciper"))
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    player = dataSnapshot.getValue(Player.class);
                                    new AlertDialog.Builder(MatchActivity.this)
                                            .setTitle(R.string.player_left)
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
                            .setTitle(R.string.join_match)
                            .setMessage(R.string.join_message)
                            .setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    ref.child("matches").child(matchID)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    match = dataSnapshot.getValue(Match.class);
                                                    DatabaseUtils.addPlayerToMatch(MatchActivity.this,
                                                            ref,
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
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

}
