package ch.epfl.sweng.jassatepfl.stats;

import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;

/**
 * Created by vinz on 11/23/16.
 */

public class DumbGenerator {
    static Player.PlayerID id = new Player.PlayerID("000000");
    static StatsUpdate update = new StatsUpdate.Builder()
            .addLosers(new Player.PlayerID("111111"), new Player.PlayerID("222222"))
            .addWinners(new Player.PlayerID("333333"), new Player.PlayerID("000000"))
            .setTimestamp(1478908800)
            .setWinScore(700)
            .setLoseScore(200)
            .setGameVariant(Match.GameVariant.CHIBRE)
            .setMatchId("lol")
            .build();

    static StatsUpdate update1 = new StatsUpdate.Builder()
            .addWinners(new Player.PlayerID("111111"), new Player.PlayerID("222222"))
            .addLosers(new Player.PlayerID("4444"), new Player.PlayerID("000000"))
            .setTimestamp(1479893140)
            .setWinScore(900)
            .setLoseScore(200)
            .setGameVariant(Match.GameVariant.CHICANE)
            .setMatchId("kek")
            .build();

    static StatsUpdate update2 = new StatsUpdate.Builder()
            .addWinners(new Player.PlayerID("111111"), new Player.PlayerID("222222"))
            .addLosers(new Player.PlayerID("4444"), new Player.PlayerID("000000"))
            .setTimestamp(1479044690)
            .setWinScore(900)
            .setLoseScore(200)
            .setGameVariant(Match.GameVariant.CHICANE)
            .setMatchId("kek")
            .build();

    static UserStats stats = new UserStats(id).update(update).update(update1).update(update2);

    public static UserStats getStuff() {
        return stats;
    }
}
