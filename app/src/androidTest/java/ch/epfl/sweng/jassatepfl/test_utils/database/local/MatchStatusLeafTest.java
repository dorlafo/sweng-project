package ch.epfl.sweng.jassatepfl.test_utils.database.local;


import java.util.Arrays;
import java.util.List;

/**
 * @author Amaury Combes
 */
public class MatchStatusLeafTest extends LeafTest<List<Boolean>> {
    private LeafFieldTest<Boolean> statusP0 = new LeafFieldTest<>("0");
    private LeafFieldTest<Boolean> statusP1 = new LeafFieldTest<>("1");
    private LeafFieldTest<Boolean> statusP2 = new LeafFieldTest<>("2");
    private LeafFieldTest<Boolean> statusP3 = new LeafFieldTest<>("3");

    public MatchStatusLeafTest(String id) {
        super(id);
    }

    @Override
    public LeafFieldTest getChild(String id) {
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
