package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static by.bsu.wialontransport.util.GeometryTestUtil.*;
import static by.bsu.wialontransport.util.CollectionUtil.mapToSet;
import static org.junit.Assert.*;

public final class AddressServiceTest extends AbstractContextTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 1 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(256, ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0.5 0, 0 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0 0))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(257, ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 17 18, 10 15))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 10 15))', 4326)"
            + ")")
    public void addressShouldBeFoundByGpsCoordinates() {
        final double givenLatitude = 2.5;
        final double givenLongitude = 2.5;

        final Optional<Address> optionalActual = this.addressService.findByGpsCoordinates(
                givenLatitude, givenLongitude
        );
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        final Address expected = Address.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .center(createPoint(this.geometryFactory, 53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 1 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(256, ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0.5 0, 0 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0 0))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(257, ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 17 18, 10 15))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 10 15))', 4326)"
            + ")")
    public void addressShouldNotBeFoundByGpsCoordinates() {
        final double givenLatitude = 20;
        final double givenLongitude = 20;

        final Optional<Address> optionalActual = this.addressService.findByGpsCoordinates(
                givenLatitude, givenLongitude
        );
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 1 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(256, ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0.5 0, 0 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0 0))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(257, ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 17 18, 10 15))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 10 15))', 4326)"
            + ")")
    public void addressShouldBeFoundByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory, 10, 15, 15, 16, 16, 17
        );

        final Optional<Address> optionalActual = this.addressService.findByGeometry(givenGeometry);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        final Address expected = Address.builder()
                .id(257L)
                .boundingBox(createPolygon(this.geometryFactory, 10, 15, 15, 16, 16, 17, 17, 18))
                .center(createPoint(this.geometryFactory, 53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 10, 15, 15, 16, 16, 17))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 1 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(256, ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0.5 0, 0 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0 0))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(257, ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 17 18, 10 15))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 10 15))', 4326)"
            + ")")
    public void addressShouldNotBeFoundByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                10, 15, 15, 16, 16, 18
        );

        final Optional<Address> optionalActual = this.addressService.findByGeometry(givenGeometry);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 1 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(256, ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0.5 0, 0 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0 0))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(257, ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 17 18, 10 15))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 10 15))', 4326)"
            + ")")
    public void addressShouldExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                10, 15, 15, 16, 16, 17
        );

        final boolean actual = this.addressService.isExistByGeometry(givenGeometry);
        assertTrue(actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 1 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(256, ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0.5 0, 0 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((0 0, 0 0.5, 0.5 0.5, 0 0))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(257, ST_GeomFromText('POLYGON((10 15, 15 16, 16 17, 17 18, 10 15))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((10.1 15, 15 16, 16 17, 10.1 15))', 4326)"
            + ")")
    public void addressShouldNotExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                10, 15, 15, 16, 16, 17
        );

        final boolean actual = this.addressService.isExistByGeometry(givenGeometry);
        assertFalse(actual);
    }

    @Test
    @Sql(statements = "INSERT INTO searching_cities_processes(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(254, "
            + "ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326), "
            + "1, 36, 36, 'SUCCESS'"
            + ")")
    @Sql(statements = "INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, "
            + "ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), "
            + "'first-city', 'first-country', "
            + "ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(257, "
            + "ST_GeomFromText('POLYGON((3 3, 3 4, 4 4, 4 3, 3 3))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), "
            + "'first-city', 'first-country', "
            + "ST_GeomFromText('POLYGON((3 3, 4 3, 4 4, 3 3))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) "
            + "VALUES(258, 257, 254)")
    @Sql(statements = "INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(259, "
            + "ST_GeomFromText('POLYGON((3 1, 4 1, 4 2, 3 2, 3 1))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), "
            + "'first-city', 'first-country', "
            + "ST_GeomFromText('POLYGON((3 1, 4 1, 4 2, 3 2, 3 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) "
            + "VALUES(260, 259, 254)")
    public void citiesPreparedGeometriesIntersectedByLineStringShouldBeFound() {
        final LineString givenLineString = createLineString(
                this.geometryFactory,
                1.5, 1.5, 3.5, 3.5, 4.5, 4.5
        );

        final List<PreparedGeometry> actual = this.addressService.findCitiesPreparedGeometriesIntersectedByLineString(
                givenLineString
        );
        final Set<Geometry> actualGeometries = mapToSet(actual, PreparedGeometry::getGeometry);
        final Set<Geometry> expectedGeometries = Set.of(
                createPolygon(
                        this.geometryFactory,
                        3, 3, 4, 3, 4, 4
                )
        );
        assertEquals(expectedGeometries, actualGeometries);
    }

    @Test
    @Sql(statements = "INSERT INTO searching_cities_processes(id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(254, "
            + "ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326), "
            + "1, 36, 36, 'SUCCESS'"
            + ")")
    @Sql(statements = "INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, "
            + "ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), "
            + "'first-city', 'first-country', "
            + "ST_GeomFromText('POLYGON((1 1, 1 2, 2 2, 2 1, 1 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(257, "
            + "ST_GeomFromText('POLYGON((3 3, 3 4, 4 4, 4 3, 3 3))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), "
            + "'first-city', 'first-country', "
            + "ST_GeomFromText('POLYGON((3 3, 4 3, 4 4, 3 3))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) "
            + "VALUES(258, 257, 254)")
    @Sql(statements = "INSERT INTO addresses(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(259, "
            + "ST_GeomFromText('POLYGON((3 1, 4 1, 4 2, 3 2, 3 1))', 4326), "
            + "ST_SetSRID(ST_POINT(1.5, 1.5), 4326), "
            + "'first-city', 'first-country', "
            + "ST_GeomFromText('POLYGON((3 1, 4 1, 4 2, 3 2, 3 1))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) "
            + "VALUES(260, 259, 254)")
    public void citiesPreparedGeometriesIntersectedByLineStringShouldNotBeFound() {
        final LineString givenLineString = createLineString(
                this.geometryFactory,
                1.5, 1.5, 2, 4, 3, 5
        );

        final List<PreparedGeometry> actual = this.addressService.findCitiesPreparedGeometriesIntersectedByLineString(
                givenLineString
        );
        assertTrue(actual.isEmpty());
    }
}
