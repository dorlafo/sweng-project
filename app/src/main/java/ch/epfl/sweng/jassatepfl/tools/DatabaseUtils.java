package ch.epfl.sweng.jassatepfl.tools;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.jassatepfl.WaitingPlayersActivity;
import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;
import ch.epfl.sweng.jassatepfl.error.ErrorHandlerUtils;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

/**
 * Utils fonctions to retrieve/add from/to Database
 *
 * @author Alexis Montavon
 */
public class DatabaseUtils {
    public static final String DATABASE_PLAYERS = "players2";
    public static final String DATABASE_MATCHES = "matches2";
    public static final String DATABASE_ACTIVE_MATCHES = "activeMatches2";
    public static final String DATABASE_PENDING_MATCHES = "pendingMatches2";
    public static final String DATABASE_STATS = "stats";
    public static final String DATABASE_STATS_BUFFER = "buffer";
    public static final String DATABASE_STATS_USER = "user";

    /**
     * Add player to match on the database.
     *
     * @param context The context where the method is called.
     * @param ref     Database reference
     * @param matchID The matchID to add player to.
     * @param sciper  The player sciper
     * @param match   The match to add player to.
     */
    public static void addPlayerToMatch(final Context context,
                                        final DBReferenceWrapper ref,
                                        final String matchID,
                                        final String sciper,
                                        final Match match) {
        ref.child(DatabaseUtils.DATABASE_PLAYERS)
                .child(sciper)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Player player = dataSnapshot.getValue(Player.class);
                        try {
                            match.addPlayer(player);
                            ref.child(DatabaseUtils.DATABASE_MATCHES).child(matchID).setValue(match);
                            Intent moveToMatchActivity = new Intent(context, WaitingPlayersActivity.class);
                            moveToMatchActivity.putExtra("match_Id", matchID);
                            context.startActivity(moveToMatchActivity);
                        } catch (IllegalStateException e) {
                            ErrorHandlerUtils.sendErrorMessage(context, R.string.error_cannot_join, R.string.error_match_full);
                        } catch (IllegalAccessException a) {
                            ErrorHandlerUtils.sendErrorMessage(context, R.string.error_cannot_join, R.string.error_already_in_match);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("ERROR-DATABASE", databaseError.toString());
                    }
                });
    }

}
