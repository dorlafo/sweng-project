package ch.epfl.sweng.project.res;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Match.MatchID;
import ch.epfl.sweng.project.model.Match.MatchRank;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.model.Player.PlayerID;

public class DummyMatchData {

    public static List<Match> dummyMatches() {
        LatLng rolexCoord = new LatLng(46.518299, 6.568323);
        LatLng BCCoord = new LatLng(46.518470, 6.561907);
        LatLng COCoord = new LatLng(46.520050, 6.564625);
        LatLng swissTechCoord = new LatLng(46.523103, 6.564649);

        Player amaury = new Player(new PlayerID(1), "Combes", "Amaury");
        Player vincenzo = new Player(new PlayerID(2), "Bazzucchi", "Vincenzo");
        Player dorian = new Player(new PlayerID(3), "Laforest", "Dorian");
        Player alexis = new Player(new PlayerID(4), "Montavon", "Alexis");
        Player random = new Player(new PlayerID(5), "Smith", "John");

        List<Player> match1Players = new ArrayList<>();
        match1Players.add(amaury);

        List<Player> match2Players = new ArrayList<>();
        match2Players.add(vincenzo);
        match2Players.add(dorian);

        List<Player> match3Players = new ArrayList<>();
        match3Players.add(alexis);

        List<Player> match4Players = new ArrayList<>();
        match4Players.add(random);

        Match match1 = new Match(new MatchID(1), match1Players, rolexCoord,
                "Rolex", new MatchRank(0), false, null, new Date());
        Match match2 = new Match(new MatchID(2), match2Players, BCCoord,
                "BC", new MatchRank(1), false, null, new Date(100, 12, 25));
        Match match3 = new Match(new MatchID(3), match3Players, COCoord,
                "CO", new MatchRank(2), false, null, new Date(120, 1, 1));
        Match match4 = new Match(new MatchID(4), match4Players, swissTechCoord,
                "SwissTech", new MatchRank(3), true, null, new Date(300, 31, 12));

        List<Match> matches = new ArrayList<>();
        matches.add(match1);
        matches.add(match2);
        matches.add(match3);
        matches.add(match4);

        return matches;
    }
}
