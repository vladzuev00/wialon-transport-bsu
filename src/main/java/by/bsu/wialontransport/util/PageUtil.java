package by.bsu.wialontransport.util;

import by.bsu.wialontransport.crud.entity.Entity;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@UtilityClass
public final class PageUtil {

    public static <ID, ENTITY extends Entity<ID>> Set<ID> mapToIds(final Page<ENTITY> entities) {
        return entities.stream()
                .map(ENTITY::getId)
                .collect(toSet());
    }
}
