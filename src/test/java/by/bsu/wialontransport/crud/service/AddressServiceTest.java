package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.model.GpsCoordinate;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.Set;

import static by.bsu.wialontransport.util.CollectionUtil.mapToSet;
import static by.bsu.wialontransport.util.GeometryTestUtil.createLineString;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.*;

public final class AddressServiceTest extends AbstractSpringBootTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldBeFoundByGpsCoordinates() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(1, 1);

        final Optional<Address> optionalActual = addressService.findByGpsCoordinates(givenCoordinate);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();

        final Long actualId = actual.getId();
        final Long expectedId = 255L;
        assertEquals(expectedId, actualId);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldNotBeFoundByGpsCoordinates() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(20, 20);

        final Optional<Address> optionalActual = addressService.findByGpsCoordinates(givenCoordinate);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldBeFoundByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                1, 1, 1, 2, 2, 2, 2, 1
        );

        final Optional<Address> optionalActual = addressService.findByGeometry(givenGeometry);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();

        final Long actualId = actual.getId();
        final Long expectedId = 255L;
        assertEquals(expectedId, actualId);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldNotBeFoundByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                10, 15, 15, 16, 16, 18
        );

        final Optional<Address> optionalActual = addressService.findByGeometry(givenGeometry);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                1, 1, 1, 2, 2, 2, 2, 1
        );

        final boolean actual = addressService.isExistByGeometry(givenGeometry);
        assertTrue(actual);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void addressShouldNotExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                10, 15, 15, 16, 16, 17
        );

        final boolean actual = addressService.isExistByGeometry(givenGeometry);
        assertFalse(actual);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void citiesPreparedGeometriesIntersectedByLineStringShouldBeFound() {
        final LineString givenLineString = createLineString(
                geometryFactory,
                1.5, 1.5, 3.5, 3.5, 4.5, 4.5
        );

        final Set<PreparedGeometry> actual = addressService.findCitiesPreparedGeometriesIntersectedByLineString(
                givenLineString
        );
        final Set<Geometry> actualGeometries = mapToSet(actual, PreparedGeometry::getGeometry);
        final Set<Geometry> expectedGeometries = Set.of(
                createPolygon(
                        geometryFactory,
                        3, 3, 4, 3, 4, 4
                )
        );
        assertEquals(expectedGeometries, actualGeometries);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void citiesPreparedGeometriesIntersectedByLineStringShouldNotBeFound() {
        final LineString givenLineString = createLineString(
                geometryFactory,
                1.5, 1.5, 2, 4, 3, 5
        );

        final Set<PreparedGeometry> actual = addressService.findCitiesPreparedGeometriesIntersectedByLineString(
                givenLineString
        );
        assertTrue(actual.isEmpty());
    }
}
