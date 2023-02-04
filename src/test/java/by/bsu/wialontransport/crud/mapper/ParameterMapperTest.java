package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static org.junit.Assert.*;

public final class ParameterMapperTest extends AbstractContextTest {

    @Autowired
    private ParameterMapper mapper;

    @Test
    public void entityShouldBeMappedToDto() {
        final ParameterEntity givenEntity = ParameterEntity.builder()
                .id(255L)
                .name("name")
                .type(INTEGER)
                .value("44")
                .build();

        final Parameter actual = this.mapper.mapToDto(givenEntity);
        final Parameter expected = Parameter.builder()
                .id(255L)
                .name("name")
                .type(INTEGER)
                .value("44")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Parameter givenDto = Parameter.builder()
                .id(255L)
                .name("name")
                .type(INTEGER)
                .value("44")
                .build();

        final ParameterEntity actual = this.mapper.mapToEntity(givenDto);
        final ParameterEntity expected = ParameterEntity.builder()
                .id(255L)
                .name("name")
                .type(INTEGER)
                .value("44")
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    private static void checkEquals(final ParameterEntity expected, final ParameterEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertSame(expected.getType(), actual.getType());
        assertEquals(expected.getValue(), actual.getValue());
    }
}
