package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.GPSPoint;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

public class MatchLeafTest extends LeafTest<Match> {
    private LeafFieldTest<List<Player>> playersLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_PLAYERS, this);
    private LeafFieldTest<GPSPoint> locationLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_LOCATION, this);
    private LeafFieldTest<String> descriptionLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_DESCRIPTION, this);
    private LeafFieldTest<Integer> quoteLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_QUOTE, this);
    private LeafFieldTest<Boolean> privateMatchLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_PRIVATE, this);
    private LeafFieldTest<Match.GameVariant> gameVariantLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_GAME_VARIANT, this);
    private LeafFieldTest<Integer> maxPlayerNumberLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_MAX_NB_PLAYER, this);
    private LeafFieldTest<Long> timeLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_TIME, this);
    private LeafFieldTest<String> matchIDLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_MATCH_ID, this);
    private LeafFieldTest<Map<String, Boolean>> hasCardsLeaf =  new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_HAS_CARDS, this);
    private LeafFieldTest<Map<String, List<String>>> teamsLeaf = new LeafFieldTest<>(DatabaseUtils.DATABASE_MATCHES_TEAMS, this);

    /**
     * Constructor of the LeafTest class
     *
     * @param id the id of the LeafTest that is created
     */
    public MatchLeafTest(String id, NodeTest parent) {
        super(id, parent);
    }

    @Override
    public LeafFieldTest getChild(String id) {
        switch (id) {
            case DatabaseUtils.DATABASE_MATCHES_PLAYERS:
                return playersLeaf;
            case DatabaseUtils.DATABASE_MATCHES_LOCATION:
                return locationLeaf;
            case DatabaseUtils.DATABASE_MATCHES_DESCRIPTION:
                return descriptionLeaf;
            case DatabaseUtils.DATABASE_MATCHES_QUOTE:
                return quoteLeaf;
            case DatabaseUtils.DATABASE_MATCHES_PRIVATE:
                return privateMatchLeaf;
            case DatabaseUtils.DATABASE_MATCHES_GAME_VARIANT:
                return gameVariantLeaf;
            case DatabaseUtils.DATABASE_MATCHES_MAX_NB_PLAYER:
                return maxPlayerNumberLeaf;
            case DatabaseUtils.DATABASE_MATCHES_TIME:
                return timeLeaf;
            case DatabaseUtils.DATABASE_MATCHES_MATCH_ID:
                return matchIDLeaf;
            case DatabaseUtils.DATABASE_MATCHES_HAS_CARDS:
                return hasCardsLeaf;
            case DatabaseUtils.DATABASE_MATCHES_TEAMS:
                return teamsLeaf;
            default:
                throw new IllegalArgumentException("Match class does not have a field named : " + id);
        }
    }

    /**
     * Setter for the data of the current leaf
     *
     * @param data the data that we need to add
     */
    @Override
    public void setData(Match data) {
        if(isDeleted) {
            this.isDeleted = false;
            //throw new UnsupportedOperationException("Cannot set data on a deleted MatchLeafTest");
        }
        this.data = data;
        playersLeaf.setData(data.getPlayers());
        locationLeaf.setData(data.getLocation());
        descriptionLeaf.setData(data.getDescription());
        quoteLeaf.setData(data.getQuote());
        privateMatchLeaf.setData(data.isPrivateMatch());
        gameVariantLeaf.setData(data.getGameVariant());
        maxPlayerNumberLeaf.setData(data.getMaxPlayerNumber());
        timeLeaf.setData(data.getTime());
        matchIDLeaf.setData(data.getMatchID());
        teamsLeaf.setData(data.getTeams());
        hasCardsLeaf.setData(data.getHasCards());
        this.setChanged();
        this.notifyObservers(this);
    }

    @Override
    public void initialize() {

    }
}
