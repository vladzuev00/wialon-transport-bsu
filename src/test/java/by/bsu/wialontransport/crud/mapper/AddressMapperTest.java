package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

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
                .build();

        final AddressEntity actual = this.mapper.mapToEntity(givenAddress);
        final AddressEntity expected = AddressEntity.builder()
                .id(255L)
                .boundingBox(this.createPolygon(1, 2, 3, 4, 5, 6, 7, 8))
                .center(this.createPoint(4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .build();

        assert actual != null;
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
                .build();

        final Address actual = this.mapper.mapToDto(givenEntity);
        final Address expected = Address.builder()
                .id(255L)
                .boundingBox(this.createPolygon(2, 3, 4, 5, 6, 7, 8, 9))
                .center(this.createPoint(5.5, 6.6))
                .cityName("city")
                .countryName("country")
                .build();
        assertEquals(expected, actual);
    }

    private Point createPoint(final double longitude, final double latitude) {
        final CoordinateXY coordinate = new CoordinateXY(longitude, latitude);
        return this.geometryFactory.createPoint(coordinate);
    }

    private Geometry createPolygon(final double firstX, final double firstY,
                                   final double secondX, final double secondY,
                                   final double thirdX, final double thirdY,
                                   final double fourthX, final double fourthY) {
        return this.geometryFactory.createPolygon(new Coordinate[]{
                        new CoordinateXY(firstX, firstY),
                        new CoordinateXY(secondX, secondY),
                        new CoordinateXY(thirdX, thirdY),
                        new CoordinateXY(fourthX, fourthY),
                        new CoordinateXY(firstX, firstY)
                }
        );
    }

    private static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
        assertEquals(expected.getCenter(), actual.getCenter());
        assertEquals(expected.getCityName(), actual.getCityName());
        assertEquals(expected.getCountryName(), actual.getCountryName());
    }
}