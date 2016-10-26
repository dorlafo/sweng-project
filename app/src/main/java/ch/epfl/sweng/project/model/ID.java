package ch.epfl.sweng.project.model;


/**
 * @author Amaury Combes
 */
abstract class ID {

    private long id;

    public ID() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    public ID(long id) {
        this.id = id;
    }

    public long getID() {
        return id;
    }

    @Override
    public String toString() {
        return Long.toString(id);
    }

}
