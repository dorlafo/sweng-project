package ch.epfl.sweng.jassatepfl;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.jassatepfl.error.ErrorHandlerUtils;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;
import ch.epfl.sweng.jassatepfl.tools.PlayerListAdapter;

/**
 * Activity allowing user to add player while
 * creating match
 */
public class InvitePlayerToMatchActivity extends BaseAppCompatActivity implements
        SearchView.OnQueryTextListener,
        View.OnClickListener {

    private static final String TAG = InvitePlayerToMatchActivity.class.getSimpleName();

    private String currentUserSciper;
    private PlayerListAdapter adapter;
    private ListView playerListView;
    private Set<String> inviteScipers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fAuth.getCurrentUser() == null) {
            //Log.d(TAG, "showLogin:getCurrentUser:null");
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            //Log.d(TAG, "showLogin:getCurrentUser:notNull");
            setContentView(R.layout.activity_invite_player_to_match);

            // TODO: maybe have a field in base activity with sciper of current user, with error management
            currentUserSciper = getUserSciper();
            inviteScipers = new HashSet<>();

            TextView emptyList = new TextView(this);
            emptyList.setText(R.string.invite_welcome_text);
            emptyList.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            emptyList.setTextColor(Color.BLACK);
            emptyList.setTextSize(20);

            playerListView = (ListView) findViewById(R.id.invite_list);
            ((ViewGroup) playerListView.getParent()).addView(emptyList);
            playerListView.setEmptyView(emptyList);
            playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                    final Player player = adapter.getItem(position);
                    new AlertDialog.Builder(InvitePlayerToMatchActivity.this)
                            .setTitle(R.string.dialog_add_player)
                            .setMessage(player.toString())
                            .setPositiveButton(R.string.dialog_add_confirmation, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String sciper = player.getID().toString();
                                    if (sciper.equals(currentUserSciper)) {
                                        ErrorHandlerUtils.sendErrorMessage(InvitePlayerToMatchActivity.this,
                                                R.string.toast_invite_yourself,
                                                R.string.error_already_in_match);
                                    } else {
                                        inviteScipers.add(player.getID().toString());
                                    }
                                }
                            })
                            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing, goes back to InvitePlayerToMatchActivity
                                }
                            })
                            .show();
                }
            });
            Button inviteButton = (Button) findViewById(R.id.invite_button);
            inviteButton.setOnClickListener(this);
        }
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
        dbRefWrapped.child(DatabaseUtils.DATABASE_PLAYERS).orderByChild("firstName")
                .startAt(newText).endAt(newText + "z").limitToFirst(50).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Player> playerList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            playerList.add(snapshot.getValue(Player.class));
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invite_button:
                Intent resultIntent = new Intent();
                if (!inviteScipers.isEmpty()) {
                    int playerIndex = 0;
                    for (String sciper : inviteScipers) {
                        resultIntent.putExtra("player" + playerIndex, sciper);
                        ++playerIndex;
                    }
                    resultIntent.putExtra("players_added", playerIndex);
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
