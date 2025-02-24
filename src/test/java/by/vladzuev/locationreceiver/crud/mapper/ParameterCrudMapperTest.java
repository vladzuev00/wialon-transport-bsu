package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Parameter;
import by.vladzuev.locationreceiver.crud.entity.ParameterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.vladzuev.locationreceiver.crud.enumeration.ParameterType.INTEGER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ParameterCrudMapperTest extends AbstractSpringBootTest {

    @Autowired
    private ParameterCrudMapper mapper;

    @Test
    public void first() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void second() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final ParameterEntity givenEntity = entityManager.find(ParameterEntity.class, 257L);
        final Parameter actual = mapper.mapEntity(givenEntity);
        final Parameter expected = new Parameter(257L, "", INTEGER, "4", Location.builder().build());
        assertEquals(expected, actual);
    }
}
