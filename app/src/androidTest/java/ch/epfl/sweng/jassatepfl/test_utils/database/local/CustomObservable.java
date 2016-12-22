package ch.epfl.sweng.jassatepfl.test_utils.database.local;

import java.util.ArrayList;

public class CustomObservable {
    private final static String TAG = CustomObservable.class.getSimpleName();
    private boolean changed = false;
    private boolean added = false;
    private boolean deleted = false;
    private final ArrayList<CustomObserver> singleValueObservers;
    private final ArrayList<CustomObserver> valueObservers;
    private final ArrayList<CustomObserver> childObservers;

    /**
     * Construct an Observable with zero Observers.
     */
    public CustomObservable() {
        //Log.d(TAG, "creating a new CustomObservable");
        singleValueObservers = new ArrayList<>();
        valueObservers = new ArrayList<>();
        childObservers = new ArrayList<>();
    }

    /**
     * Adds an observer to the set of single value observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param   o   an observer to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
    public synchronized void addSingleValueObserver(CustomObserver o) {
        if (o == null)
            throw new NullPointerException();
        if (!singleValueObservers.contains(o)) {
            //Log.d(TAG, "adding a new SingleValueObserver for o:" + ((DBRefWrapTest)o).getCurrentNode().getId());
            singleValueObservers.add(o);
        }
    }

    /**
     * Adds an observer to the set of value observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param   o   an observer to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
    public synchronized void addValueObserver(CustomObserver o) {
        if (o == null)
            throw new NullPointerException();
        if (!valueObservers.contains(o)) {
            //Log.d(TAG, "adding a new ValueObserver for o:" + ((DBRefWrapTest)o).getCurrentNode().getId());
            valueObservers.add(o);
        }
    }

    /**
     * Adds an observer to the set of child observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param   o   an observer to be added.
     * @throws NullPointerException   if the parameter o is null.
     */
    public synchronized void addChildObserver(CustomObserver o) {
        if (o == null)
            throw new NullPointerException();
        if (!childObservers.contains(o)) {
            //Log.d(TAG, "adding a new ChildObserver for o :" + ((DBRefWrapTest)o).getCurrentNode().getId());
            childObservers.add(o);
        }
    }

    /**
     * Deletes an observer from the set of single value observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   o   the observer to be deleted.
     */
    public synchronized void deleteSingleValueObserver(CustomObserver o) {
        //Log.d(TAG, "deleting SingleValueObserver:" + ((DBRefWrapTest)o).getCurrentNode().getId());
        singleValueObservers.remove(o);
    }

    /**
     * Deletes an observer from the set of value observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   o   the observer to be deleted.
     */
    public synchronized void deleteValueObserver(CustomObserver o) {
        //Log.d(TAG, "deleting ValueObserver:" + ((DBRefWrapTest)o).getCurrentNode().getId());
        valueObservers.remove(o);
    }

    /**
     * Deletes an observer from the set of child observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     * @param   o   the observer to be deleted.
     */
    public synchronized void deleteChildObserver(CustomObserver o) {
        //Log.d(TAG, "deleting ChildObserver:" + ((DBRefWrapTest)o).getCurrentNode().getId());
        childObservers.remove(o);
    }

    public void notifyObservers(NodeTest arg) {
        //Log.d(TAG, "In notifyObservers with arg:" + arg.getId());
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        CustomObserver[] arrLocalSingleValue = null;
        CustomObserver[] arrLocalValue = null;
        CustomObserver[] arrLocalChild = null;
        ChangeType localChangeType = ChangeType.CHANGED;


        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary Observables while holding its own Monitor.
             * The code where we extract each Observable from
             * the ArrayList and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             *
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!hasChanged() && !wasAdded() && !wasDeleted())
                return;

            if (hasChanged()) {
                //Log.d(TAG, "In notifyObservers something has changed!");
                clearChanged();
            }
            if (wasDeleted()) {
                //Log.d(TAG, "In notifyObservers something was deleted!");
                clearDeleted();
                localChangeType = ChangeType.DELETED;
            }
            if (wasAdded()) {
                //Log.d(TAG, "In notifyObservers something was added!");
                clearAdded();
                localChangeType = ChangeType.ADDED;
            }

            arrLocalChild = childObservers.toArray(new CustomObserver[childObservers.size()]);
            arrLocalSingleValue = singleValueObservers.toArray(new CustomObserver[singleValueObservers.size()]);
            arrLocalValue = valueObservers.toArray(new CustomObserver[valueObservers.size()]);
        }

        for(int i = arrLocalChild.length-1; i >= 0; i--) {
            arrLocalChild[i].update(this, arg, ObserverType.CHILD, localChangeType);
        }

        for (int i = arrLocalSingleValue.length-1; i >= 0; i--) {
            arrLocalSingleValue[i].update(this, arg, ObserverType.FOR_SINGLE_VALUE, localChangeType);
        }

        for (int i = arrLocalValue.length-1; i >= 0; i--) {
            arrLocalValue[i].update(this, arg, ObserverType.VALUE, localChangeType);
        }
    }

    /**
     * Clears all the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteAllObservers() {
        //Log.d(TAG, "Deleting all observers!");

        childObservers.clear();
        singleValueObservers.clear();
        valueObservers.clear();
    }

    /**
     * Clears the single value observer list so that this object no longer has any observers for single value.
     */
    public synchronized void deleteSingleValueObservers() {
        //Log.d(TAG, "Deleting singleValue observers!");
        singleValueObservers.clear();
    }

    /**
     * Clears the value observer list so that this object no longer has any observers for value.
     */
    public synchronized void deleteValueObservers() {
        //Log.d(TAG, "Deleting value observers!");
        valueObservers.clear();
    }

    /**
     * Clears the child observer list so that this object no longer has any observers for child.
     */
    public synchronized void deleteChildObservers() {
        //Log.d(TAG, "Deleting child observers!");
        childObservers.clear();
    }

    /**
     * Marks this <tt>Observable</tt> object as having been changed; the
     * <tt>hasChanged</tt> method will now return <tt>true</tt>.
     */
    protected synchronized void setChanged() {
        //Log.d(TAG, "Changed set to true !");
        changed = true;
    }

    /**
     * Marks this <tt>Observable</tt> object as having been added; the
     * <tt>wasAdded</tt> method will now return <tt>true</tt>.
     */
    protected synchronized void setAdded() {
        //Log.d(TAG, "Added set to true !");
        added = true;
    }

    /**
     * Marks this <tt>Observable</tt> object as having been deleted; the
     * <tt>wasDeleted</tt> method will now return <tt>true</tt>.
     */
    protected synchronized void setDeleted() {
        //Log.d(TAG, "Deleted set to true !");
        deleted = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the <tt>hasChanged</tt> method will now return <tt>false</tt>.
     * This method is called automatically by the
     * <code>notifyObservers</code> methods.
     *
     * @see     CustomObservable#notifyObservers(NodeTest)
     */
    protected synchronized void clearChanged() {
        //Log.d(TAG, "Changed set to false !");
        changed = false;
    }

    /**
     * Indicates that this object is no longer added, or that it has
     * already notified all of its observers of its most recent addition,
     * so that the <tt>wasAdded</tt> method will now return <tt>false</tt>.
     * This method is called automatically by the
     * <code>notifyObservers</code> methods.
     *
     * @see     CustomObservable#notifyObservers(NodeTest)
     */
    protected synchronized void clearAdded() {
        //Log.d(TAG, "Added set to false !");
        added = false;
    }

    /**
     * Indicates that this object is no longer deleted, or that it has
     * already notified all of its observers of its most recent deletion,
     * so that the <tt>wasDeleted</tt> method will now return <tt>false</tt>.
     * This method is called automatically by the
     * <code>notifyObservers</code> methods.
     *
     * @see     CustomObservable#notifyObservers(NodeTest)
     */
    protected synchronized void clearDeleted() {
        //Log.d(TAG, "Deleted set to false !");
        deleted = false;
    }

    /**
     * Tests if this object has changed.
     *
     * @return  <code>true</code> if and only if the <code>setChanged</code>
     *          method has been called more recently than the
     *          <code>clearChanged</code> method on this object;
     *          <code>false</code> otherwise.
     * @see     CustomObservable#clearChanged()
     * @see     CustomObservable#setChanged()
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * Tests if this object was added.
     *
     * @return  <code>true</code> if and only if the <code>setAdded</code>
     *          method has been called more recently than the
     *          <code>clearAdded</code> method on this object;
     *          <code>false</code> otherwise.
     * @see     CustomObservable#clearAdded()
     * @see     CustomObservable#setAdded()
     */
    public synchronized boolean wasAdded() {
        return added;
    }

    /**
     * Tests if this object was deleted.
     *
     * @return  <code>true</code> if and only if the <code>setDeleted</code>
     *          method has been called more recently than the
     *          <code>clearDeleted</code> method on this object;
     *          <code>false</code> otherwise.
     * @see     CustomObservable#clearDeleted()
     * @see     CustomObservable#setDeleted() ()
     */
    public synchronized boolean wasDeleted() {
        return deleted;
    }

    /**
     * Returns the number of observers of this <tt>Observable</tt> object.
     *
     * @return  the number of observers of this object.
     */
    public synchronized int countObservers() {
        return childObservers.size() + valueObservers.size() + childObservers.size();
    }
}
