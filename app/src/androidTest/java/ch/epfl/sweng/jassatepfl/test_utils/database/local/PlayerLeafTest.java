package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.model.Rank;

/**
 * @author Amaury Combes
 */
public class PlayerLeafTest extends LeafTest<Player> {
    private LeafFieldTest<Player.PlayerID> idLeaf = new LeafFieldTest<>("id");
    private LeafFieldTest<String> lastNameLeaf = new LeafFieldTest<>("lastName");
    private LeafFieldTest<String> firstNameLeaf = new LeafFieldTest<>("fistName");
    private LeafFieldTest<Rank> rankLeaf = new LeafFieldTest<>("rank");

    /**
     * Constructor of the LeafTest class
     *
     * @param id the id of the LeafTest that is created
     */
    public PlayerLeafTest(String id) {
        super(id);
    }

    @Override
    public LeafFieldTest getChild(String id) {
        switch (id) {
            case "id":
                return idLeaf;
            case "firstName":
                return firstNameLeaf;
            case "lastName":
                return lastNameLeaf;
            case "rank":
                return rankLeaf;
            default:
                throw new IllegalArgumentException("Player class does not have a field named : " + id);
        }
    }

    /**
     * Setter for the data of the current leaf
     *
     * @param data the data that we need to add
     */
    @Override
    public void setData(Player data) {
        this.data = data;
        idLeaf.setData(data.getID());
        firstNameLeaf.setData(data.getFirstName());
        lastNameLeaf.setData(data.getLastName());
        rankLeaf.setData(data.getRank());
    }
}
