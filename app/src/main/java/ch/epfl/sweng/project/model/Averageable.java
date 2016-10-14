package ch.epfl.sweng.project.model;

import java.util.List;

public interface Averageable<T, E> {

    public E average(List<T> elem);

}
