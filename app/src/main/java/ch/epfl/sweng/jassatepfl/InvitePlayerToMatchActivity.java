package ch.epfl.sweng.jassatepfl;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.PlayerListAdapter;

/**
 * Activity allowing user to add player while
 * creating match
 */
public class InvitePlayerToMatchActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,
        View.OnClickListener {

    private PlayerListAdapter adapter;
    private ListView playerListView;
    private List<Player> playerToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_player_to_match);
        TextView emptyList = new TextView(this);
        emptyList.setText(R.string.search_player_list);
        emptyList.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        emptyList.setTextColor(Color.BLACK);
        emptyList.setTextSize(20);
        playerListView = (ListView)findViewById(android.R.id.list);
        ((ViewGroup) playerListView.getParent()).addView(emptyList);
        playerListView.setEmptyView(emptyList);


        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView , View view , int position ,long arg3) {
                final Player player = adapter.getItem(position);
                new AlertDialog.Builder(InvitePlayerToMatchActivity.this)
                        .setTitle(R.string.invite_player_text)
                        .setMessage(" " + player.getFirstName() + " " + player.getLastName())
                        .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                playerToAdd.add(player);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing, goes back to InvitePlayerToMatchActivity
                            }
                        })
                        .show();
            }
        });
        playerToAdd = new ArrayList<>();
        Button inviteButton = (Button) findViewById(R.id.invite_button);
        inviteButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // When user click on search button.
        // Don't do anything, list updates on text changes.
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Modify query when user changes the search text
        DatabaseReference ref  =  FirebaseDatabase.getInstance().getReference().child("players");
        ref.orderByChild("firstName").startAt(newText).endAt(newText + "z").limitToFirst(50).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        List<Player> playerList = new ArrayList<Player>();
                        while(iterator.hasNext()){
                            playerList.add(iterator.next().getValue(Player.class));
                        }
                        adapter = new PlayerListAdapter(InvitePlayerToMatchActivity.this, R.layout.player_list_element, playerList);
                        playerListView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("DATABASE ERROR: ", databaseError.toString());
                    }
                }
        );
        return false;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.invite_button:
                Intent resultIntent = new Intent();
                if(!playerToAdd.isEmpty()) {
                    for(Player p : playerToAdd) {
                        resultIntent.putExtra("player" + playerToAdd.indexOf(p), p.getID().toString());
                    }
                    resultIntent.putExtra("players_added", playerToAdd.size());
                    setResult(RESULT_OK, resultIntent);
                } else {
                    setResult(RESULT_CANCELED, resultIntent);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
