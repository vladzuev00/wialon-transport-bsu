package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.Entity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.mapToList;

@UtilityClass
public final class EntityUtil {

    public static <ID, ENTITY extends Entity<ID>> List<ID> findEntityIds(final List<ENTITY> entities) {
        return mapToList(entities, ENTITY::getId);
    }

}
