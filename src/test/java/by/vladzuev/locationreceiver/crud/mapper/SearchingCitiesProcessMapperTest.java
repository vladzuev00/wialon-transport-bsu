package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity;
import by.vladzuev.locationreceiver.util.GeometryTestUtil;
import by.vladzuev.locationreceiver.util.entity.SearchingCitiesProcessEntityUtil;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class SearchingCitiesProcessMapperTest extends AbstractSpringBootTest {

    @Autowired
    private SearchingCitiesProcessMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void entityShouldBeMappedToDto() {
        final SearchingCitiesProcessEntity givenEntity = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(GeometryTestUtil.createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(SearchingCitiesProcessEntity.Status.HANDLING)
                .build();

        final SearchingCitiesProcess actual = mapper.mapToDto(givenEntity);
        final SearchingCitiesProcess expected = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(GeometryTestUtil.createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(SearchingCitiesProcessEntity.Status.HANDLING)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final SearchingCitiesProcess givenDto = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(GeometryTestUtil.createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(SearchingCitiesProcessEntity.Status.HANDLING)
                .build();

        final SearchingCitiesProcessEntity actual = mapper.mapToEntity(givenDto);
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(GeometryTestUtil.createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(SearchingCitiesProcessEntity.Status.HANDLING)
                .build();
        assertNotNull(actual);
        SearchingCitiesProcessEntityUtil.checkEquals(expected, actual);
    }
}
