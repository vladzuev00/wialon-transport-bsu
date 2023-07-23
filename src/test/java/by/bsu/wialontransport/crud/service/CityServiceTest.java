package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static by.bsu.wialontransport.util.CollectionTestUtil.extractProperties;
import static by.bsu.wialontransport.util.GeometryTestUtil.createLineString;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.*;

public final class CityServiceTest extends AbstractContextTest {

    @Autowired
    private CityService cityService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    @Sql(statements = "INSERT INTO cities(id, address_id, searching_cities_process_id) VALUES(257, 255, 256)")
    public void cityShouldExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                1, 2, 3, 4, 5, 6
        );

        final boolean exists = this.cityService.isExistByGeometry(givenGeometry);
        assertTrue(exists);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326)"
            + ")")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    public void cityShouldNotExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                1, 2, 3, 4, 5, 6
        );

        final boolean exists = this.cityService.isExistByGeometry(givenGeometry);
        assertFalse(exists);
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
    public void preparedGeometriesIntersectedByLineStringShouldBeFound() {
        final LineString givenLineString = createLineString(
                this.geometryFactory,
                1.5, 1.5, 3.5, 3.5, 4.5, 4.5
        );

        final List<PreparedGeometry> actual = this.cityService.findPreparedGeometriesIntersectedByLineString(
                givenLineString
        );
        final List<Geometry> actualGeometries = extractProperties(actual, PreparedGeometry::getGeometry);

        final List<Geometry> expectedGeometries = List.of(
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
    public void preparedGeometriesIntersectedByLineStringShouldNotBeFound() {
        final LineString givenLineString = createLineString(
                this.geometryFactory,
                1.5, 1.5, 2, 4, 3, 5
        );

        final List<PreparedGeometry> actual = this.cityService.findPreparedGeometriesIntersectedByLineString(
                givenLineString
        );
        assertTrue(actual.isEmpty());
    }
}
