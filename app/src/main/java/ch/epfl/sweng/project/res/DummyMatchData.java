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
        LatLng CECoord = new LatLng(46.520525, 6.569554);
        LatLng flonCoord = new LatLng(46.520858, 6.629570);
        LatLng lemanCoord = new LatLng(46.453986, 6.553145);

        Player amaury = new Player(new PlayerID(1), "Combes", "Amaury");
        Player vincenzo = new Player(new PlayerID(2), "Bazzucchi", "Vincenzo");
        Player dorian = new Player(new PlayerID(3), "Laforest", "Dorian");
        Player alexis = new Player(new PlayerID(4), "Montavon", "Alexis");
        Player random = new Player(new PlayerID(5), "Smith", "John");
        Player colin = new Player(new PlayerID(6), "Branca", "Colin");
        Player marco = new Player(new PlayerID(7), "Ballerini", "Marco");
        Player nicolas = new Player(new PlayerID(8), "Phan Van", "Nicolas");

        List<Player> match1Players = new ArrayList<>();
        match1Players.add(amaury);

        List<Player> match2Players = new ArrayList<>();
        match2Players.add(vincenzo);
        match2Players.add(dorian);

        List<Player> match3Players = new ArrayList<>();
        match3Players.add(alexis);

        List<Player> match4Players = new ArrayList<>();
        match4Players.add(random);

        List<Player> match5Players = new ArrayList<>();
        match5Players.add(colin);
        match5Players.add(nicolas);
        match5Players.add(alexis);
        match5Players.add(vincenzo);

        List<Player> match6Players = new ArrayList<>();
        match6Players.add(marco);

        List<Player> match7Players = new ArrayList<>();

        Match match1 = new Match(new MatchID(1), match1Players, rolexCoord,
                "Rolex", new MatchRank(0), false, null, new Date());
        Match match2 = new Match(new MatchID(2), match2Players, BCCoord,
                "BC", new MatchRank(1), false, null, new Date(100, 12, 25));
        Match match3 = new Match(new MatchID(3), match3Players, COCoord,
                "CO", new MatchRank(2), false, null, new Date(120, 1, 1));
        Match match4 = new Match(new MatchID(4), match4Players, swissTechCoord,
                "SwissTech", new MatchRank(3), true, null, new Date(300, 31, 12));
        Match match5 = new Match(new MatchID(5), match5Players, CECoord,
                "CE", new MatchRank(4), false, null, new Date(300, 31, 12));
        Match match6 = new Match(new MatchID(6), match6Players, flonCoord,
                "Flon", new MatchRank(5), false, null, new Date(300, 31, 12));
        Match match7 = new Match(new MatchID(7), match7Players, lemanCoord,
                "Cette String est beaucoup trop longue, je me demande si l'affichage va foirer???",
                new MatchRank(6), false, null, new Date(300, 31, 12));

        List<Match> matches = new ArrayList<>();
        matches.add(match1);
        matches.add(match2);
        matches.add(match3);
        matches.add(match4);
        matches.add(match5);
        matches.add(match6);
        matches.add(match7);

        return matches;
    }
}
