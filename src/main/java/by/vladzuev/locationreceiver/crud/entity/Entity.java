package by.vladzuev.locationreceiver.crud.entity;

import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.hash;

public abstract class Entity<ID> implements Serializable {

    public abstract void setId(final ID id);

    public abstract ID getId();

    @Override
    @SuppressWarnings({"unchecked", "EqualsWhichDoesntCheckParameterClass"})
    public final boolean equals(final Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (Hibernate.getClass(this) != Hibernate.getClass(otherObject)) {
            return false;
        }
        final Entity<ID> other = (Entity<ID>) otherObject;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return hash(getId());
    }
}
