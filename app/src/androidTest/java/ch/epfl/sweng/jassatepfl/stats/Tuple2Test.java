package ch.epfl.sweng.jassatepfl.stats;

import org.junit.Test;

import ch.epfl.sweng.jassatepfl.model.Player;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class Tuple2Test {

    Player.PlayerID id = new Player.PlayerID("000000");
    Tuple2<Player.PlayerID, Integer> counter1 = new Tuple2<>(id, 0);

    @Test
    public void getKey() {
        assertThat(new Player.PlayerID("000000").equals(counter1.getKey()), is(true));
        assertThat(new Player.PlayerID("000100").equals(counter1.getKey()), is(false));
    }

    @Test
    public void getValue() {
        assertThat(counter1.getValue(), is(0));
        assertThat(counter1.getValue() == 1, is(false));
    }

    @Test
    public void setValue() {
        counter1.setValue(counter1.getValue() + 1);
        assertThat(counter1.getValue() == 0, is(false));
        assertThat(counter1.getValue(), is(1));
    }

}
