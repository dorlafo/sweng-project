package ch.epfl.sweng.project.model;


import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import ch.epfl.sweng.project.model.GPSPoint;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

public final class GPSPointTest {

    private final GPSPoint firstPoint = new GPSPoint(46, 6);
    private final GPSPoint secondPoint = new GPSPoint(47, 7);

    @Test
    public void equalsReturnsFalseWhenComparingWithNull() {
        assertFalse(firstPoint.equals(null));
    }

    @Test
    public void equalsReturnsFalseWhenComparingWithOtherClass() {
        LatLng gps = new LatLng(46, 6);

        assertFalse(firstPoint.equals(gps));
    }

    @Test
    public void equalsReturnsFalseWhenComparingPointsWithDifferentCoordinates() {
        assertFalse(firstPoint.equals(secondPoint) || secondPoint.equals(firstPoint));
    }

    @Test
    public void equalsIsReflexive() {
        assertTrue(firstPoint.equals(firstPoint));
    }

    @Test
    public void equalsReturnsTrueWhenComparingPointsWithSameCoordinates() {
        GPSPoint newPoint = new GPSPoint(46, 6);

        assertNotSame(firstPoint, newPoint);
        assertTrue(firstPoint.equals(newPoint) && newPoint.equals(firstPoint));
    }

    @Test
    public void hashCodeIsTheSameForEqualPoints() {
        GPSPoint newPoint = new GPSPoint(46, 6);

        assertNotSame(firstPoint, newPoint);
        assertEquals(firstPoint, newPoint);
        assertEquals(firstPoint.hashCode(), newPoint.hashCode());
    }

    @Test
    public void hashCodeIsDifferentForDifferentPoints() {
        assertNotEquals(firstPoint.hashCode(), secondPoint.hashCode());
    }

    @Test
    public void correctlyConvertsToLatLng() {
        LatLng newPoint = firstPoint.toLatLng();

        assertEquals(firstPoint.getLatitude(), newPoint.latitude);
        assertEquals(firstPoint.getLongitude(), newPoint.longitude);
    }

}
