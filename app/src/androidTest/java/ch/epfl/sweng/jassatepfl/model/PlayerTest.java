package ch.epfl.sweng.jassatepfl.model;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import ch.epfl.sweng.jassatepfl.model.Player.PlayerID;

import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.amaury;
import static ch.epfl.sweng.jassatepfl.test_utils.DummyDataTest.vincenzo;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

@SuppressWarnings({"EqualsBetweenInconvertibleTypes", "EqualsWithItself", "ObjectEqualsNull"})
public final class PlayerTest {

    @Test
    public void equalsReturnsFalseWhenComparingWithNull() {
        assertFalse(amaury.equals(null));
    }

    @Test
    public void equalsReturnsFalseWhenComparingWithOtherClass() {
        LatLng gps = new LatLng(46, 6);

        assertFalse(amaury.equals(gps));
    }

    @Test
    public void equalsReturnsFalseWhenComparingDifferentPlayers() {
        assertFalse(amaury.equals(vincenzo) || vincenzo.equals(amaury));
    }

    @Test
    public void equalsIsReflexive() {
        assertTrue(amaury.equals(amaury));
    }

    @Test
    public void equalsReturnsTrueWhenComparingSamePlayers() {
        Player clone = new Player(new PlayerID(235400), "Combes", "Amaury", new Rank(123));

        assertNotSame(amaury, clone);
        assertTrue(amaury.equals(clone) && clone.equals(amaury));
    }

    @Test
    public void hashCodeIsTheSameForSamePlayers() {
        Player clone = new Player(new PlayerID(235400), "Combes", "Amaury", new Rank(123));

        assertNotSame(amaury, clone);
        assertEquals(amaury, clone);
        assertEquals(amaury.hashCode(), clone.hashCode());
    }

    @Test
    public void hashCodeIsDifferentForDifferentPlayers() {
        assertNotEquals(amaury.hashCode(), vincenzo.hashCode());
    }

    @Test
    public void correctlySetsRank() {
        assertEquals(123, amaury.getRank().getRank());
        amaury.setRank(new Rank(1999));
        assertEquals(1999, amaury.getRank().getRank());
    }

    @Test
    public void toStringReturnsFullName() {
        String fullName = amaury.getFirstName() + ' ' + amaury.getLastName();
        assertTrue(fullName.equals(amaury.toString()));
    }

}
