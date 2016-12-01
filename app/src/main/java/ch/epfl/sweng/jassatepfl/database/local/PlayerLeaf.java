package ch.epfl.sweng.jassatepfl.database.local;

import ch.epfl.sweng.jassatepfl.model.Player;

/**
 * @author Amaury Combes
 */
public class PlayerLeaf extends Leaf<Player> {
    private LeafField<Player.PlayerID> idLeaf = new LeafField<>("id");
    private LeafField<String> lastNameLeaf = new LeafField<>("lastName");
    private LeafField<String> firstNameLeaf = new LeafField<>("fistName");
    private LeafField<Integer> quoteLeaf = new LeafField<>("quote");

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
            case "quote":
                return quoteLeaf;
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
        quoteLeaf.setData(data.getQuote());
    }
}
