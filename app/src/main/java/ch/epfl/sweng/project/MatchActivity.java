package ch.epfl.sweng.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.tools.PlayerListAdapterForMatch;

public class MatchActivity extends AppCompatActivity {

    private String matchId;
    private String sciper;
    private PlayerListAdapterForMatch pAdapter;
    private Player player;
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            matchId = extras.getString("MATCH_ID");
            Log.d("MATCH:", matchId);

            FirebaseDatabase.getInstance().getReference()
                    .child("matches")
                    .child(matchId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            match = dataSnapshot.getValue(Match.class);
                            Log.d("MATCH:", matchId);
                            TextView description = (TextView) findViewById(R.id.match_description);
                            TextView variant = (TextView) findViewById(R.id.match_variant);

                            description.setText(match.getDescription());
                            variant.setText(match.getGameVariant().toString());

                            ListView listView = (ListView) findViewById(android.R.id.list);
                            listView.setEmptyView(findViewById(android.R.id.empty));
                            pAdapter = new PlayerListAdapterForMatch(MatchActivity.this, matchId);
                            listView.setAdapter(pAdapter);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    /*
    When user click this button "leave match" it removes him from the match's player list and
    send him back to main menu.
     */

    public void leaveMatch(View view) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        sciper = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        FirebaseDatabase.getInstance().getReference()
                .child("players")
                .child(sciper)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        player = dataSnapshot.getValue(Player.class);
                        match.removePlayer(player);
                        Intent intent = new Intent(MatchActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /*
    When all user clicks on "start match" button it lanch the game.

    TODO : create the next activity and correct the name in the intent

     */

    public void startMatch(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.waiting_others)
                .setMessage(R.string.your_match_will_begin_soon)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, goes back to MatchActivity
            }
        })
                .show();

        /*
         Intent intent = new Intent(this, ***MatchIsStart***.class);
         startActivity(intent);
          */


    }

}
