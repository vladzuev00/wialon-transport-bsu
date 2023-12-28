package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.GeometryTestUtil.*;
import static by.bsu.wialontransport.util.StreamUtil.isEmpty;
import static by.bsu.wialontransport.util.entity.AddressEntityUtil.checkEquals;
import static by.bsu.wialontransport.util.entity.EntityUtil.mapToIdsSet;
import static org.junit.Assert.*;

public final class AddressRepositoryTest extends AbstractContextTest {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldBeFoundById() {
        startQueryCount();
        final AddressEntity actual = repository.findById(255L).orElseThrow();
        checkQueryCount(1);

        final AddressEntity expected = AddressEntity.builder()
                .id(255L)
                .boundingBox(createPolygon(geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .center(createPoint(geometryFactory, 1.5, 1.5))
                .cityName("first-city")
                .countryName("first-country")
                .geometry(createPolygon(geometryFactory, 1, 1, 1, 2, 2, 2, 2, 1))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void addressShouldBeSaved() {
        final AddressEntity givenAddress = AddressEntity.builder()
                .boundingBox(createPolygon(geometryFactory, 2, 3, 4, 5, 6, 7, 8, 9))
                .center(createPoint(geometryFactory, 53.050287, 24.873636))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();

        startQueryCount();
        repository.save(givenAddress);
        checkQueryCount(2);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldBeFoundByGpsCoordinates() {
        final double givenLatitude = 1;
        final double givenLongitude = 1;

        final Optional<AddressEntity> optionalActual = repository.findByGpsCoordinates(givenLatitude, givenLongitude);
        final Long actualId = optionalActual.map(AddressEntity::getId).orElseThrow();
        final Long expectedId = 255L;
        assertEquals(expectedId, actualId);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldNotBeFoundByGpsCoordinates() {
        final double givenLatitude = 20;
        final double givenLongitude = 20;

        final Optional<AddressEntity> actual = repository.findByGpsCoordinates(givenLatitude, givenLongitude);
        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldBeFoundByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                1, 1, 1, 2, 2, 2, 2, 1
        );

        final Optional<AddressEntity> optionalActual = repository.findByGeometry(givenGeometry);
        final Long actualId = optionalActual.map(AddressEntity::getId).orElseThrow();
        final Long expectedId = 255L;
        assertEquals(expectedId, actualId);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldNotBeFoundByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                10, 15, 15, 16, 16, 17
        );

        final Optional<AddressEntity> optionalActual = repository.findByGeometry(givenGeometry);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                1, 1, 1, 2, 2, 2, 2, 1
        );

        final boolean actual = repository.isExistByGeometry(givenGeometry);
        assertTrue(actual);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldNotExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                10, 15, 15, 16, 16, 17
        );

        final boolean actual = repository.isExistByGeometry(givenGeometry);
        assertFalse(actual);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityAddressesIntersectedByLineStringShouldBeFound() {
        final LineString givenLineString = createLineString(
                geometryFactory,
                1.5, 1.5, 3.5, 3.5, 4.5, 4.5
        );

        startQueryCount();
        try (final Stream<AddressEntity> foundAddresses = repository.findCityAddressesIntersectedByLineString(givenLineString)) {
            checkQueryCount(1);

            final Set<Long> actual = mapToIdsSet(foundAddresses);
            final Set<Long> expected = Set.of(257L);
            assertEquals(expected, actual);
        }
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityAddressesIntersectedByLineStringShouldNotBeFound() {
        final LineString givenLineString = createLineString(
                geometryFactory,
                1.5, 1.5, 2, 4, 3, 5
        );

        startQueryCount();
        final Stream<AddressEntity> actual = repository.findCityAddressesIntersectedByLineString(givenLineString);
        checkQueryCount(1);

        assertTrue(isEmpty(actual));
    }
}
