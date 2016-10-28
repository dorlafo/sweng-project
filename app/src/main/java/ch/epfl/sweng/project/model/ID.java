package ch.epfl.sweng.project.model;


import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

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
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other == null || other.getClass() != this.getClass()) {
            return false;
        }

        ID otherID = (ID) other;
        return this.id == otherID.id;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return Long.toString(id);
    }

}
