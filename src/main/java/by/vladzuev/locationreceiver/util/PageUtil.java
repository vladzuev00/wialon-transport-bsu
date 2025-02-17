package by.vladzuev.locationreceiver.util;

import by.vladzuev.locationreceiver.crud.entity.AbstractEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;

@UtilityClass
public final class PageUtil {

    public static <ID, ENTITY extends AbstractEntity<ID>> List<ID> mapToIds(final Page<ENTITY> entities) {
        return entities.stream()
                .map(ENTITY::getId)
                .toList();
    }
}
