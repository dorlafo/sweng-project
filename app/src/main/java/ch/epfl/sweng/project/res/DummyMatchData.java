package ch.epfl.sweng.project.res;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import ch.epfl.sweng.project.model.Match;

public class DummyMatchData {
    private static Match match1 = new Match(1, null, new LatLng(46.518299, 6.568323), "Rolex", null, false, null, null);
    private static Match match2 = new Match(2, null, new LatLng(46.518470, 6.561907), "BC", null, false, null, null);
    private static Match match3 = new Match(3, null, new LatLng(46.520050, 6.564625), "CO", null, false, null, null);

    public static ArrayList<Match> dummyMatches() {
        ArrayList<Match> matches = new ArrayList<>();
        matches.add(match1);
        matches.add(match2);
        matches.add(match3);
        return matches;
    }
}
