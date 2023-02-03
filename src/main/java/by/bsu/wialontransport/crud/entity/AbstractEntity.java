package by.bsu.wialontransport.crud.entity;

import java.util.Objects;

import static java.util.Objects.hash;

public abstract class AbstractEntity<IdType> {

    public abstract void setId(final IdType id);

    public abstract IdType getId();

    @Override
    @SuppressWarnings("unchecked")
    public final boolean equals(final Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (this.getClass() != otherObject.getClass()) {
            return false;
        }
        final AbstractEntity<IdType> other = (AbstractEntity<IdType>) otherObject;
        return Objects.equals(this.getId(), other.getId());
    }

    @Override
    public final int hashCode() {
        return hash(this.getId());
    }
}
