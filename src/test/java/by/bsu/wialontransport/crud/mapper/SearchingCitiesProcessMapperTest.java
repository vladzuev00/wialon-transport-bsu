package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.EntityTestUtil.checkEquals;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class SearchingCitiesProcessMapperTest extends AbstractContextTest {

    @Autowired
    private SearchingCitiesProcessMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void entityShouldBeMappedToDto() {
        final SearchingCitiesProcessEntity givenEntity = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();

        final SearchingCitiesProcess actual = this.mapper.mapToDto(givenEntity);
        final SearchingCitiesProcess expected = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final SearchingCitiesProcess givenDto = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();

        final SearchingCitiesProcessEntity actual = this.mapper.mapToEntity(givenDto);
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();
        assertNotNull(actual);
        checkEquals(expected, actual);
    }
}
