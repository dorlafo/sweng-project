package ch.epfl.sweng.jassatepfl.model;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

@SuppressWarnings({"EqualsBetweenInconvertibleTypes", "EqualsWithItself", "ObjectEqualsNull"})
public final class IDTest {

    private final ID id = new Player.PlayerID("78545465465461");

    @Test
    public void equalsReturnsFalseWhenComparingWithNull() {
        assertFalse(id.equals(null));
    }

    @Test
    public void equalsReturnsFalseWhenComparingWithOtherClass() {
        Long randomLong = Long.valueOf("78545465465461");

        assertFalse(id.equals(randomLong));
    }

    @Test
    public void equalsReturnsFalseWhenComparingDifferentIDs() {
        ID secondID = new Player.PlayerID(78455464);

        assertFalse(id.equals(secondID) || secondID.equals(id));
    }

    @Test
    public void equalsIsReflexive() {
        assertTrue(id.equals(id));
    }

    @Test
    public void equalsReturnsTrueWhenComparingSameIDs() {
        ID clone = new Player.PlayerID(78545465465461L);

        assertNotSame(id, clone);
        assertTrue(id.equals(clone) && clone.equals(id));
    }

    @Test
    public void hashCodeIsTheSameForSameIDs() {
        ID clone = new Player.PlayerID(78545465465461L);

        assertNotSame(id, clone);
        assertEquals(id, clone);
        assertEquals(id.hashCode(), clone.hashCode());
    }

    @Test
    public void hashCodeIsDifferentForDifferentIDs() {
        ID secondID = new Player.PlayerID(78455464);

        assertNotEquals(id.hashCode(), secondID.hashCode());
    }

    @Test
    public void stringRepresentationIsCorrect() {
        assertEquals("78545465465461", id.toString());
    }

}
