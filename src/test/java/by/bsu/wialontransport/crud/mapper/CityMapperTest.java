package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.City;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.CityEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPoint;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static by.bsu.wialontransport.util.entity.CityEntityUtil.checkDeepEquals;
import static org.hibernate.Hibernate.isInitialized;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

public final class CityMapperTest extends AbstractContextTest {

    @Autowired
    private CityMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void entityWithLoadedPropertiesShouldBeMappedToDto() {
        final AddressEntity givenAddress = AddressEntity.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                .center(createPoint(this.geometryFactory, 53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();
        final SearchingCitiesProcessEntity givenProcess = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();
        final CityEntity givenEntity = CityEntity.builder()
                .id(255L)
                .address(givenAddress)
                .searchingCitiesProcess(givenProcess)
                .build();

        final City actual = this.mapper.mapToDto(givenEntity);
        final Address expectedAddress = Address.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                .center(createPoint(this.geometryFactory, 53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();
        final SearchingCitiesProcess expectedProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();
        final City expected = City.builder()
                .id(255L)
                .address(expectedAddress)
                .searchingCitiesProcess(expectedProcess)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void entityWithNotLoadedPropertiesShouldBeMappedToDto() {
        final AddressEntity givenAddress = new AddressEntity();
        final SearchingCitiesProcessEntity givenProcess = new SearchingCitiesProcessEntity();
        try (final MockedStatic<Hibernate> mockedStatic = mockStatic(Hibernate.class)) {
            mockedStatic
                    .when(() -> isInitialized(same(givenAddress)))
                    .thenReturn(false);
            mockedStatic
                    .when(() -> isInitialized(same(givenProcess)))
                    .thenReturn(false);
            final CityEntity givenEntity = CityEntity.builder()
                    .id(255L)
                    .address(givenAddress)
                    .searchingCitiesProcess(givenProcess)
                    .build();

            final City actual = this.mapper.mapToDto(givenEntity);
            final City expected = City.builder()
                    .id(255L)
                    .address(null)
                    .searchingCitiesProcess(null)
                    .build();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Address givenAddress = Address.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                .center(createPoint(this.geometryFactory, 53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();
        final City givenDto = City.builder()
                .id(255L)
                .address(givenAddress)
                .searchingCitiesProcess(givenProcess)
                .build();

        final CityEntity actual = this.mapper.mapToEntity(givenDto);
        final AddressEntity expectedAddress = AddressEntity.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                .center(createPoint(this.geometryFactory, 53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();
        final SearchingCitiesProcessEntity expectedProcess = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();
        final CityEntity expected = CityEntity.builder()
                .id(255L)
                .address(expectedAddress)
                .searchingCitiesProcess(expectedProcess)
                .build();
        assertNotNull(actual);
        checkDeepEquals(expected, actual);
    }
}
