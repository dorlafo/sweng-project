package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.List;

import ch.epfl.sweng.jassatepfl.model.GPSPoint;
import ch.epfl.sweng.jassatepfl.model.Match;
import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.model.Rank;


/**
 * @author Amaury Combes
 */
public class MatchLeaf extends Leaf<Match> {
    private LeafField<List<Player>> playersLeaf = new LeafField<>("players");
    private LeafField<GPSPoint> locationLeaf = new LeafField<>("location");
    private LeafField<String> descriptionLeaf = new LeafField<>("description");
    private LeafField<Rank> rankLeaf = new LeafField<>("rank");
    private LeafField<Boolean> privateMatchLeaf = new LeafField<>("privateMatch");
    private LeafField<Match.GameVariant> gameVariantLeaf = new LeafField<>("gameVariant");
    private LeafField<Integer> maxPlayerNumberLeaf = new LeafField<>("maxPlayerNumber");
    private LeafField<Long> expirationTimeLeaf = new LeafField<>("expirationTime");
    private LeafField<String> matchIDLeaf = new LeafField<>("matchID");

    /**
     * Constructor of the Leaf class
     *
     * @param id the id of the Leaf that is created
     */
    public MatchLeaf(String id) {
        super(id);
    }

    @Override
    public LeafField getChild(String id) {
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
