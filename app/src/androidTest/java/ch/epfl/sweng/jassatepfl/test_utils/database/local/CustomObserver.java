package ch.epfl.sweng.jassatepfl.test_utils.database.local;

/**
 * A class can implement the <code>CustomObserver</code> interface when it
 * wants to be informed of changes in custom observable objects.
 *
 * @see     CustomObservable
 */
public interface CustomObserver {
    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>CustomObservable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * custom observers notified of the change.
     *
     * @param   o     the custom observable object.
     * @param   arg   an argument passed to the <code>notifyObservers</code>
     *                 method.
     * @param   oType  the type of CustomObservable : SingleValue, Child or Value
     * @param   cType  the type of change made : Added, Deleted, Changed
     */
    void update(CustomObservable o, NodeTest arg, ObserverType oType, ChangeType cType);
}
