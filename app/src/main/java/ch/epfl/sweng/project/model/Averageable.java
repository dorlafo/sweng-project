package ch.epfl.sweng.project.model;

import java.util.List;

/**
 * Averageable is an interface that provides a method to compute the average of a list of elements and send it to
 * another type
 * @param <T> the type of the input
 * @param <E> the type of the output
 */
public interface Averageable<T, E> {

    /**
     * Computes the average of some elements type T and send it to the type E
     * @param elems the list of elements needed to compute the average
     * @return returns the average of elements
     */
    E average(List<T> elems);

}
