package by.bsu.wialontransport.crud.entity;

import org.hibernate.Hibernate;

import java.util.Objects;

import static java.util.Objects.hash;

public abstract class Entity<IdType> {

    public abstract void setId(final IdType id);

    public abstract IdType getId();

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
        final Entity<IdType> other = (Entity<IdType>) otherObject;
        return Objects.equals(getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return hash(getId());
    }
}
