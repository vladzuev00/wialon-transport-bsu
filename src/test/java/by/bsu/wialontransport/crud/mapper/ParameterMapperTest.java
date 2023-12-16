package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.util.entity.ParameterEntityUtil.checkEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
                .data(createDataEntity(256L))
                .build();

        final Parameter actual = this.mapper.mapToDto(givenEntity);
        final Parameter expected = Parameter.builder()
                .id(255L)
                .name("name")
                .type(INTEGER)
                .value("44")
                .data(createDataDto(256L))
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

    private static DataEntity createDataEntity(final Long id) {
        return DataEntity.builder()
                .id(id)
                .build();
    }

    private static Data createDataDto(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }
}
