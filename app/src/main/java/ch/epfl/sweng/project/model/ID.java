package ch.epfl.sweng.project.model;

/**
 * @author Amaury Combes
 */

abstract class ID {
    private long id;

    public ID() {
        // Default constructor required for calls to DataSnapshot.getValue
    }

    ID(long id) {
        this.id = id;
    }

    public long getID() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ID && ((ID) other).getID() == this.getID();
    }
}
