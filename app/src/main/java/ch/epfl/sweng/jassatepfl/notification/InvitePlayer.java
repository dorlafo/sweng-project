package ch.epfl.sweng.jassatepfl.notification;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.server.ServerInterface;

/**
 * Async class necessary to send invite message in background
 *
 * @author Alexis Montavon
 */
public class InvitePlayer extends AsyncTask<String, Void, String> {
    private List<Player> playerList;

    public InvitePlayer(List<Player> playerList) {
        this.playerList = new ArrayList<>(playerList);
    }

    public InvitePlayer(ArrayAdapter<Player> playerAdapter) {
        List<Player> playerList = new ArrayList<>();
        for(int i = 0; i < playerAdapter.getCount(); ++i) {
            playerList.add(playerAdapter.getItem(i));
        }
        this.playerList = playerList;
    }

    public InvitePlayer(Player p) {
        playerList = new ArrayList<>();
        playerList.add(p);
    }

    @Override
    protected String doInBackground(String... params) {
        // Send invite to added players
        for (int i = 0; i < playerList.size(); ++i) {
            ServerInterface.getInstance().invitePlayer(playerList.get(i).getID().toString(), params[0]);
        }
        return "";
    }
}
