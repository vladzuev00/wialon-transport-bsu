package by.bsu.wialontransport.crud.dto;

import static java.util.Objects.isNull;

@FunctionalInterface
public interface Dto<ID> {
    ID getId();

    default boolean isNew() {
        return isNull(getId());
    }
}
