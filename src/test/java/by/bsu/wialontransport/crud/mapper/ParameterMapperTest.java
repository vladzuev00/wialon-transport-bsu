package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.util.entity.ParameterEntityUtil.checkEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class ParameterMapperTest extends AbstractSpringBootTest {

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

        final Parameter actual = mapper.mapToDto(givenEntity);
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

        final ParameterEntity actual = mapper.mapToEntity(givenDto);
        final ParameterEntity expected = ParameterEntity.builder()
                .id(255L)
                .name("name")
                .type(INTEGER)
                .value("44")
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }
}
