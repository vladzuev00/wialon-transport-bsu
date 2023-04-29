package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.unil.GeometryUtil.createPoint;
import static by.bsu.wialontransport.unil.GeometryUtil.createPolygon;

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
                .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 7, 8))
                .center(createPoint(this.geometryFactory, 4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();

        final AddressEntity actual = this.mapper.mapToEntity(givenAddress);
        final AddressEntity expected = AddressEntity.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 7, 8))
                .center(createPoint(this.geometryFactory, 4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final AddressEntity givenEntity = AddressEntity.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 2, 3, 4, 5, 6, 7, 8, 9))
                .center(createPoint(this.geometryFactory, 5.5, 6.6))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 2, 3, 4, 5, 6, 7))
                .build();

        final Address actual = this.mapper.mapToDto(givenEntity);
        final Address expected = Address.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 2, 3, 4, 5, 6, 7, 8, 9))
                .center(createPoint(this.geometryFactory, 5.5, 6.6))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 2, 3, 4, 5, 6, 7))
                .build();
        assertEquals(expected, actual);
    }

    private static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
        assertEquals(expected.getCenter(), actual.getCenter());
        assertEquals(expected.getCityName(), actual.getCityName());
        assertEquals(expected.getCountryName(), actual.getCountryName());
    }
}
