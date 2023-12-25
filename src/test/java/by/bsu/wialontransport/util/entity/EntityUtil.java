package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.util.CollectionUtil;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.CollectionUtil.mapToSet;
import static java.util.stream.Collectors.toSet;

@UtilityClass
public final class EntityUtil {

    public static <ID, ENTITY extends Entity<ID>> Set<ID> mapToIdsSet(final Collection<ENTITY> entities) {
        return mapToSet(entities, ENTITY::getId);
    }

    public static <ID, ENTITY extends Entity<ID>> List<ID> mapToIdsList(final Collection<ENTITY> entities) {
        return CollectionUtil.mapToList(entities, ENTITY::getId);
    }

    public static <ID, ENTITY extends Entity<ID>> List<ID> mapToIdsList(final Stream<ENTITY> entities) {
        return entities.map(ENTITY::getId).toList();
    }

    //TODO:refactor
    public static <ID, ENTITY extends Entity<ID>> Set<ID> mapToIdsSet(final Stream<ENTITY> entities) {
        return entities.map(ENTITY::getId).collect(toSet());
    }
}
