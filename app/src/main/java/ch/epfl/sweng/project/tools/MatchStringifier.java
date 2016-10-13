package ch.epfl.sweng.project.tools;

import android.content.Context;
import android.content.res.Resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

import ch.epfl.sweng.project.R;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;

/**
 * Helper Class providing methods to "stringify" the different
 * fields of a {@link ch.epfl.sweng.project.model.Match Match}.
 *
 * @author Nicolas Phan Van
 */
public final class MatchStringifier {
    private final Context context;
    private Match match;

    public MatchStringifier(Context context) {
        this.context = context;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public String markerSnippet() {
        Resources res = context.getResources();
        String newLine = System.getProperty("line.separator");

        StringBuilder builder = new StringBuilder(
                res.getString(R.string.snippet_match_rank))
                .append(match.getRank().getRank())
                .append(newLine)
                .append(res.getString(R.string.snippet_player_list))
                .append(playersToString())
                .append(newLine)
                .append(res.getString(R.string.snippet_game_variant))
                //builder.append(match.getGameVariant().toString()) TODO: implement this
                .append(newLine)
                .append(res.getString(R.string.snippet_expiration_date))
                .append(dateToStringCustom());

        return builder.toString();
    }

    public String dateToStringCustom() {
        DateFormat dateFormat = new SimpleDateFormat(
                context.getResources().getString(R.string.date_format),
                Locale.FRENCH);
        return dateFormat.format(match.getExpirationTime());
    }

    public String playersToString() {
        StringBuilder builder = new StringBuilder();
        Iterator<Player> playerIterator = match.getPlayers().iterator();

        while (playerIterator.hasNext()) {
            builder.append(playerIterator.next().toString());
            if (playerIterator.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    public String rankToString() {
        return Integer.toString(match.getRank().getRank());
    }
}
