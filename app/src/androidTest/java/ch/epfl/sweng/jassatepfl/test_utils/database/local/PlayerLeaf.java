package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.model.Rank;

/**
 * @author Amaury Combes
 */
public class PlayerLeaf extends Leaf<Player> {
    private LeafField<Player.PlayerID> idLeaf = new LeafField<>("id");
    private LeafField<String> lastNameLeaf = new LeafField<>("lastName");
    private LeafField<String> firstNameLeaf = new LeafField<>("fistName");
    private LeafField<Rank> rankLeaf = new LeafField<>("rank");

    /**
     * Constructor of the Leaf class
     *
     * @param id the id of the Leaf that is created
     */
    public PlayerLeaf(String id) {
        super(id);
    }

    @Override
    public LeafField getChild(String id) {
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
