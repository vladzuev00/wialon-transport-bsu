package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.copyOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class AddressMapperTest extends AbstractContextTest {

    @Autowired
    private AddressMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Address givenAddress = Address.builder()
                .id(255L)
                .boundingBox(this.createPolygon(1, 2, 3, 4, 5, 6, 7, 8))
                .center(this.createPoint(4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .geometry(this.createPolygon(1, 2, 3, 4, 5, 6))
                .build();

        final AddressEntity actual = this.mapper.mapToEntity(givenAddress);
        final AddressEntity expected = AddressEntity.builder()
                .id(255L)
                .boundingBox(this.createPolygon(1, 2, 3, 4, 5, 6, 7, 8))
                .center(this.createPoint(4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .geometry(this.createPolygon(1, 2, 3, 4, 5, 6))
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final AddressEntity givenEntity = AddressEntity.builder()
                .id(255L)
                .boundingBox(this.createPolygon(2, 3, 4, 5, 6, 7, 8, 9))
                .center(this.createPoint(5.5, 6.6))
                .cityName("city")
                .countryName("country")
                .geometry(this.createPolygon(2, 3, 4, 5, 6, 7))
                .build();

        final Address actual = this.mapper.mapToDto(givenEntity);
        final Address expected = Address.builder()
                .id(255L)
                .boundingBox(this.createPolygon(2, 3, 4, 5, 6, 7, 8, 9))
                .center(this.createPoint(5.5, 6.6))
                .cityName("city")
                .countryName("country")
                .geometry(this.createPolygon(2, 3, 4, 5, 6, 7))
                .build();
        assertEquals(expected, actual);
    }

    private Point createPoint(final double longitude, final double latitude) {
        final CoordinateXY coordinate = new CoordinateXY(longitude, latitude);
        return this.geometryFactory.createPoint(coordinate);
    }

    private Geometry createPolygon(final double firstLongitude, final double firstLatitude,
                                   final double secondLongitude, final double secondLatitude,
                                   final double thirdLongitude, final double thirdLatitude) {
        return this.createPolygon(
                new CoordinateXY(firstLongitude, firstLatitude),
                new CoordinateXY(secondLongitude, secondLatitude),
                new CoordinateXY(thirdLongitude, thirdLatitude)
        );
    }

    private Geometry createPolygon(final double firstLongitude, final double firstLatitude,
                                   final double secondLongitude, final double secondLatitude,
                                   final double thirdLongitude, final double thirdLatitude,
                                   final double fourthLongitude, final double fourthLatitude) {
        return this.createPolygon(
                new CoordinateXY(firstLongitude, firstLatitude),
                new CoordinateXY(secondLongitude, secondLatitude),
                new CoordinateXY(thirdLongitude, thirdLatitude),
                new CoordinateXY(fourthLongitude, fourthLatitude)
        );
    }

    private Geometry createPolygon(final CoordinateXY... coordinates) {
        final CoordinateXY[] boundedCoordinates = copyOf(coordinates, coordinates.length + 1);
        boundedCoordinates[boundedCoordinates.length - 1] = coordinates[0];
        return this.geometryFactory.createPolygon(boundedCoordinates);
    }

    private static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
        assertEquals(expected.getCenter(), actual.getCenter());
        assertEquals(expected.getCityName(), actual.getCityName());
        assertEquals(expected.getCountryName(), actual.getCountryName());
    }
}
