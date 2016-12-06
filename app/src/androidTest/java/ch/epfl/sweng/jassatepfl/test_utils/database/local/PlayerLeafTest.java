
package ch.epfl.sweng.jassatepfl.test_utils.database.local;
import ch.epfl.sweng.jassatepfl.model.Player;

/**
 * @author Amaury Combes
 */
public class PlayerLeafTest extends LeafTest<Player> {
    private LeafFieldTest<Player.PlayerID> idLeaf = new LeafFieldTest<>("id", this);
    private LeafFieldTest<String> lastNameLeaf = new LeafFieldTest<>("lastName", this);
    private LeafFieldTest<String> firstNameLeaf = new LeafFieldTest<>("fistName", this);
    private LeafFieldTest<Integer> quoteLeaf = new LeafFieldTest<>("quote", this);

    /**
     * Constructor of the LeafTest class
     *
     * @param id the id of the LeafTest that is created
     */
    public PlayerLeafTest(String id, NodeTest parent) {
        super(id, parent);
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
        if(deleted) {
            throw new UnsupportedOperationException("Cannot setData on a deleted PlayerLeafTest");
        }
        this.data = data;
        idLeaf.setData(data.getID());
        firstNameLeaf.setData(data.getFirstName());
        lastNameLeaf.setData(data.getLastName());
        quoteLeaf.setData(data.getQuote());
        setChanged();
        notifyObservers(this);
    }
}
