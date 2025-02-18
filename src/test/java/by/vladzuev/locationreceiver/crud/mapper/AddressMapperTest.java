package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
import by.vladzuev.locationreceiver.util.GeometryTestUtil;
import by.vladzuev.locationreceiver.util.entity.AddressEntityUtil;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;

import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class AddressMapperTest extends AbstractSpringBootTest {

    @Autowired
    private AddressMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Address givenAddress = Address.builder()
                .id(255L)
                .boundingBox(GeometryTestUtil.createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6, 7, 8))
                .center(GeometryTestUtil.createPoint(geometryFactory, 4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .geometry(GeometryTestUtil.createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();

        final AddressEntity actual = mapper.mapToEntity(givenAddress);
        final AddressEntity expected = AddressEntity.builder()
                .id(255L)
                .boundingBox(GeometryTestUtil.createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6, 7, 8))
                .center(GeometryTestUtil.createPoint(geometryFactory, 4.4, 5.5))
                .cityName("city")
                .countryName("country")
                .geometry(GeometryTestUtil.createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();
        assertNotNull(actual);
        AddressEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final AddressEntity givenEntity = AddressEntity.builder()
                .id(255L)
                .boundingBox(GeometryTestUtil.createPolygon(geometryFactory, 2, 3, 4, 5, 6, 7, 8, 9))
                .center(GeometryTestUtil.createPoint(geometryFactory, 5.5, 6.6))
                .cityName("city")
                .countryName("country")
                .geometry(GeometryTestUtil.createPolygon(geometryFactory, 2, 3, 4, 5, 6, 7))
                .build();

        final Address actual = mapper.mapToDto(givenEntity);
        final Address expected = Address.builder()
                .id(255L)
                .boundingBox(GeometryTestUtil.createPolygon(geometryFactory, 2, 3, 4, 5, 6, 7, 8, 9))
                .center(GeometryTestUtil.createPoint(geometryFactory, 5.5, 6.6))
                .cityName("city")
                .countryName("country")
                .geometry(GeometryTestUtil.createPolygon(geometryFactory, 2, 3, 4, 5, 6, 7))
                .build();
        assertEquals(expected, actual);
    }
}
