package ch.epfl.sweng.jassatepfl.tools;


import android.content.Context;
import android.content.res.Resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

/**
 * Helper Class providing methods to "stringify" the different
 * fields of a {@link ch.epfl.sweng.jassatepfl.model.Match Match}.
 */
public final class MatchStringifier {

    private final Context context;
    private Match match;

    /**
     * Constructs a new Stringifier.
     * <br>
     * Do not forget to set a Match!
     *
     * @param context The context using the stringifier
     */
    public MatchStringifier(Context context) {
        this.context = context;
    }

    /**
     * Sets the Match to stringify.
     *
     * @param match The Match
     */
    public void setMatch(Match match) {
        this.match = match;
    }

    /**
     * Builds and returns a String representation of all useful
     * fields of a Match to be used in a googleMaps
     * {@link com.google.android.gms.maps.model.Marker Marker}.
     *
     * @return The Marker snippet
     */
    public String markerSnippet() {
        Resources res = context.getResources();
        String newLine = System.getProperty("line.separator");

        StringBuilder builder = new StringBuilder(
                res.getString(R.string.snippet_match_quote))
                .append(Integer.toString(match.getQuote()))
                .append(newLine)
                .append(res.getString(R.string.snippet_player_list))
                .append(playersToString())
                .append(newLine)
                .append(res.getString(R.string.snippet_game_variant))
                .append(match.getGameVariant().toString())
                .append(newLine)
                .append(res.getString(R.string.snippet_expiration_date))
                .append(dateToStringCustom());

        return builder.toString();
    }

    /**
     * Converts and returns the expiration date of a Match
     * to a human readable date and time.
     *
     * @return The expiration date, in the form dd/MM/yyyy HH:mm:ss
     */
    public String dateToStringCustom() {
        DateFormat dateFormat = new SimpleDateFormat(
                context.getResources().getString(R.string.date_format_match_display),
                Locale.FRENCH);
        return dateFormat.format(match.getTime());
    }

    /**
     * Returns the String representation of the list of players of the Match
     *
     * @return The list of players
     */
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

    public String quoteToString() {
        return Integer.toString(match.getQuote());
    }

    public String variantToString() {
        return match.getGameVariant().toString();
    }

}
