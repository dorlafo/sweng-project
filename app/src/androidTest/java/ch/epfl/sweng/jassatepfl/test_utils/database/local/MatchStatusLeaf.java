package ch.epfl.sweng.jassatepfl.test_utils.database.local;


import java.util.Arrays;
import java.util.List;

/**
 * @author Amaury Combes
 */
public class MatchStatusLeaf extends Leaf<List<Boolean>> {
    private LeafField<Boolean> statusP0 = new LeafField<>("0");
    private LeafField<Boolean> statusP1 = new LeafField<>("1");
    private LeafField<Boolean> statusP2 = new LeafField<>("2");
    private LeafField<Boolean> statusP3 = new LeafField<>("3");

    public MatchStatusLeaf(String id) {
        super(id);
    }

    @Override
    public LeafField getChild(String id) {
        switch (id) {
            case "0":
                return statusP0;
            case "1":
                return statusP1;
            case "2":
                return statusP2;
            case "3":
                return statusP3;
            default:
                throw new IllegalArgumentException("Could not get player number : " + id);
        }

    }

    @Override
    public void setData(List<Boolean> data) {
        this.data = data;
        statusP0.setData(data.get(0));
        statusP1.setData(data.get(1));
        statusP2.setData(data.get(2));
        statusP3.setData(data.get(3));
    }

    @Override
    public List<Boolean> getData() {
        return Arrays.asList(statusP0.getData(), statusP1.getData(), statusP2.getData(), statusP3.getData());
    }
}
