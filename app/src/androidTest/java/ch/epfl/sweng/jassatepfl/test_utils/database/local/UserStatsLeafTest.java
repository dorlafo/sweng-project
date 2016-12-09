package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import ch.epfl.sweng.jassatepfl.stats.UserStats;
import ch.epfl.sweng.jassatepfl.stats.trueskill.Rank;
import ch.epfl.sweng.jassatepfl.tools.DatabaseUtils;

/**
 * Created by Amaury on 07/12/2016.
 */

public class UserStatsLeafTest extends LeafTest<UserStats> {
    private LeafFieldTest<Rank> rankLeaf = new LeafFieldTest<Rank>(DatabaseUtils.DATABASE_USERSTATS_RANK, this);

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
            default:
                throw new IllegalArgumentException("UserStatsTest class does not have a field named : " + id);
        }
    }

    /**
     * Setter for the data of the current leaf
     *
     * @param data the data that we need to add
     */
    @Override
    public void setData(UserStats data) {
        this.data = data;
        rankLeaf.setData(data.getRank());
    }

    @Override
    public void initialize() {

    }
}
