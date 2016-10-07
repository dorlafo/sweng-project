package ch.epfl.sweng.project.model;

/**
 * @author Amaury Combes
 */

public abstract class ID {
    private final long id;

    protected ID(long id) {
        this.id = id;
    }

    public long getID() {
        return id;
    }
}
