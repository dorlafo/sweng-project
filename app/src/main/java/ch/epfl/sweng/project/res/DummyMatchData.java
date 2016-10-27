package ch.epfl.sweng.project.res;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.project.model.GPSPoint;
import ch.epfl.sweng.project.model.Match;
import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.model.Player.PlayerID;

import static ch.epfl.sweng.project.model.Match.GameVariant.CLASSIC;

public class DummyMatchData {

    public static List<Match> dummyMatches() {
        GPSPoint rolexCoord = new GPSPoint(46.518299, 6.568323);
        GPSPoint BCCoord = new GPSPoint(46.518470, 6.561907);
        GPSPoint COCoord = new GPSPoint(46.520050, 6.564625);
        GPSPoint swissTechCoord = new GPSPoint(46.523103, 6.564649);
        GPSPoint CECoord = new GPSPoint(46.520525, 6.569554);
        GPSPoint flonCoord = new GPSPoint(46.520858, 6.629570);
        GPSPoint lemanCoord = new GPSPoint(46.453986, 6.553145);

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

        Match match1 = new Match(match1Players, rolexCoord,
                "Rolex", false, CLASSIC, new Date().getTime(), "bobo");
        Match match2 = new Match(match2Players, BCCoord,
                "BC", false, CLASSIC, new Date(100, 12, 25).getTime(), "");
        Match match3 = new Match(match3Players, COCoord,
                "CO", false, CLASSIC, new Date(120, 1, 1).getTime(), "");
        Match match4 = new Match(match4Players, swissTechCoord,
                "SwissTech", true, CLASSIC, new Date(300, 31, 12).getTime(), "");
        Match match5 = new Match(match5Players, CECoord,
                "CE", false, CLASSIC, new Date(300, 31, 12).getTime(), "");
        Match match6 = new Match(match6Players, flonCoord,
                "Flon", false, CLASSIC, new Date(300, 31, 12).getTime(), "");
        Match match7 = new Match(match7Players, lemanCoord,
                "Cette String est beaucoup trop longue, je me demande si l'affichage va foirer???",
                false, CLASSIC, new Date(300, 31, 12).getTime(), "");

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
