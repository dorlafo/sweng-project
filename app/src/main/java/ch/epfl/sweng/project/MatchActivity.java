package ch.epfl.sweng.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.project.error.ErrorHandlerUtils;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;

public class MatchActivity extends AppCompatActivity {

    private String matchID;
    private String sciper;
    private Player player;
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent startIntent = getIntent();
        String intentAction = startIntent.getAction();
        matchID = startIntent.getStringExtra("matchId");
        sciper = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if(intentAction != null) {
            switch(intentAction) {
                case "matchfull":
                    break;
                case "playerjoined":
                    break;
                case "playerleft":
                    break;
                case "reject":
                    break;
                case "invite":
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.join_match)
                            .setMessage(R.string.join_message)
                            .setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    sciper = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
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
                                    ref.child("players").child(sciper)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    player = dataSnapshot.getValue(Player.class);
                                                    try {
                                                        match.addPlayer(player);
                                                        ref.child("matches").child(matchID).setValue(match);
                                                        Intent moveToMatchActivity = new Intent(MatchActivity.this, MatchActivity.class);
                                                        getIntent().putExtra("MATCH_ID", matchID);
                                                        startActivity(moveToMatchActivity);
                                                    } catch (IllegalStateException e) {
                                                        ErrorHandlerUtils.sendErrorMessage(MatchActivity.this, R.string.match_is_full, "Sorry, desired match is full");
                                                    } catch (IllegalAccessException a) {
                                                        ErrorHandlerUtils.sendErrorMessage(MatchActivity.this, R.string.cannot_join_match, "You are already signed into that Match");
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
                    break;
                default:
                    break;
            }
        }
    }

}
