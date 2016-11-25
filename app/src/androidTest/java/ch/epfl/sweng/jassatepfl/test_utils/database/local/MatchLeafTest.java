package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.List;

import ch.epfl.sweng.jassatepfl.model.GPSPoint;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.model.Rank;


/**
 * @author Amaury Combes
 */
public class MatchLeafTest extends LeafTest<Match> {
    private LeafFieldTest<List<Player>> playersLeaf = new LeafFieldTest<>("players");
    private LeafFieldTest<GPSPoint> locationLeaf = new LeafFieldTest<>("location");
    private LeafFieldTest<String> descriptionLeaf = new LeafFieldTest<>("description");
    private LeafFieldTest<Rank> rankLeaf = new LeafFieldTest<>("rank");
    private LeafFieldTest<Boolean> privateMatchLeaf = new LeafFieldTest<>("privateMatch");
    private LeafFieldTest<Match.GameVariant> gameVariantLeaf = new LeafFieldTest<>("gameVariant");
    private LeafFieldTest<Integer> maxPlayerNumberLeaf = new LeafFieldTest<>("maxPlayerNumber");
    private LeafFieldTest<Long> expirationTimeLeaf = new LeafFieldTest<>("expirationTime");
    private LeafFieldTest<String> matchIDLeaf = new LeafFieldTest<>("matchID");

    /**
     * Constructor of the LeafTest class
     *
     * @param id the id of the LeafTest that is created
     */
    public MatchLeafTest(String id) {
        super(id);
    }

    @Override
    public LeafFieldTest getChild(String id) {
        switch (id) {
            case "players":
                return playersLeaf;
            case "location":
                return locationLeaf;
            case "description":
                return descriptionLeaf;
            case "rank":
                return rankLeaf;
            case "privateMatch":
                return privateMatchLeaf;
            case "gameVariant":
                return gameVariantLeaf;
            case "maxPlayerNumber":
                return maxPlayerNumberLeaf;
            case "expirationTime":
                return expirationTimeLeaf;
            case "matchID":
                return matchIDLeaf;
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
        this.data = data;
        playersLeaf.setData(data.getPlayers());
        locationLeaf.setData(data.getLocation());
        descriptionLeaf.setData(data.getDescription());
        rankLeaf.setData(data.getRank());
        privateMatchLeaf.setData(data.isPrivateMatch());
        gameVariantLeaf.setData(data.getGameVariant());
        maxPlayerNumberLeaf.setData(data.getMaxPlayerNumber());
        expirationTimeLeaf.setData(data.getExpirationTime());
        matchIDLeaf.setData(data.getMatchID());
    }


    @Override
    public void initialize() {

    }
}
