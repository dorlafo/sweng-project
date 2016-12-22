package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.stats.Tuple2;
import ch.epfl.sweng.jassatepfl.stats.UserStats;
import ch.epfl.sweng.jassatepfl.stats.trueskill.Rank;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

public class UserStatsLeafTest extends LeafTest<UserStats> {
    private LeafFieldTest<Rank> rankLeaf = new LeafFieldTest<Rank>(DatabaseUtils.DATABASE_USERSTATS_RANK, this);
    private LeafFieldTest<Map<String, Integer>> partners =  new LeafFieldTest<>(DatabaseUtils.DATABASE_USERSTATS_PARTNERS, this);
    private LeafFieldTest<List<Tuple2<Long, Integer>>> playedByDate = new LeafFieldTest<>(DatabaseUtils.DATABASE_USERSTATS_PLAYED_BY_DATE, this);
    private LeafFieldTest<Integer> playedMatches = new LeafFieldTest<>(DatabaseUtils.DATABASE_USERSTATS_PLAYED_MATCHES, this);
    private LeafFieldTest<Player.PlayerID> playerId = new LeafFieldTest<>(DatabaseUtils.DATABASE_USERSTATS_PLAYERID, this);
    private LeafFieldTest<List<Tuple2<Long, Integer>>> quoteByDate = new LeafFieldTest<>(DatabaseUtils.DATABASE_USERSTATS_QUOTE_BY_DATE, this);
    private LeafFieldTest<Map<String, Integer>> variants =  new LeafFieldTest<>(DatabaseUtils.DATABASE_USERSTATS_VARIANTS, this);
    private LeafFieldTest<List<Tuple2<Long, Integer>>> wonByDate = new LeafFieldTest<>(DatabaseUtils.DATABASE_USERSTATS_WON_BY_DATE, this);
    private LeafFieldTest<Integer> wonMatches = new LeafFieldTest<>(DatabaseUtils.DATABASE_USERSTATS_WON_MATCHES, this);
    private LeafFieldTest<Map<String, Integer>> wonWith = new LeafFieldTest<>(DatabaseUtils.DATABASE_USERSTATS_WON_WITH, this);

    /**
     * Constructor of the LeafTest class
     *
     * @param id the id of the LeafTest that is created
     */
    public UserStatsLeafTest(String id, NodeTest parent) {
        super(id, parent);
    }

    @Override
    public LeafFieldTest getChild(String id) {
        switch (id) {
            case DatabaseUtils.DATABASE_USERSTATS_RANK:
                return rankLeaf;
            case DatabaseUtils.DATABASE_USERSTATS_PARTNERS:
                return partners;
            case DatabaseUtils.DATABASE_USERSTATS_PLAYED_BY_DATE:
                return playedByDate;
            case DatabaseUtils.DATABASE_USERSTATS_PLAYED_MATCHES:
                return playedMatches;
            case DatabaseUtils.DATABASE_USERSTATS_PLAYERID:
                return playerId;
            case DatabaseUtils.DATABASE_USERSTATS_QUOTE_BY_DATE:
                return quoteByDate;
            case DatabaseUtils.DATABASE_USERSTATS_VARIANTS:
                return variants;
            case DatabaseUtils.DATABASE_USERSTATS_WON_BY_DATE:
                return wonByDate;
            case DatabaseUtils.DATABASE_USERSTATS_WON_MATCHES:
                return wonMatches;
            case DatabaseUtils.DATABASE_USERSTATS_WON_WITH:
                return wonWith;
            default:
                throw new IllegalArgumentException("UserStats class does not have a field named : " + id);
        }
    }

    /**
     * Setter for the data of the current leaf
     *
     * @param data the data that we need to add
     */
    @Override
    public void setData(UserStats data) {
        if(isDeleted) {
            isDeleted = true;
            //throw new UnsupportedOperationException("Cannot setData on a deleted PendingMatchLeafTest");
        }
        this.data = data;
        rankLeaf.setData(data.getRank());
        partners.setData(data.getPartners());
        playedByDate.setData(data.getPlayedByDate());
        playedMatches.setData(data.getPlayedMatches());
        playerId.setData(data.getPlayerId());
        quoteByDate.setData(data.getQuoteByDate());
        variants.setData(data.getVariants());
        wonByDate.setData(data.getWonByDate());
        wonMatches.setData(data.getWonMatches());
        wonWith.setData(data.getWonWith());
        this.setChanged();
        this.notifyObservers(this);
    }

    @Override
    public void initialize() {

    }
}
