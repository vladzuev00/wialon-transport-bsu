package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static java.util.Arrays.copyOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
                .boundingBox(this.createPolygon(1, 1, 1, 4, 4, 4, 4, 1))
                .center(this.createPoint(53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(this.createPolygon(1, 1, 1, 4, 4, 4))
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
        final Geometry givenGeometry = this.createPolygon(
                10, 15, 15, 16, 16, 17
        );

        final Optional<Address> optionalActual = this.addressService.findAddressByGeometry(givenGeometry);
        assertTrue(optionalActual.isPresent());
        final Address actual = optionalActual.get();
        final Address expected = Address.builder()
                .id(257L)
                .boundingBox(this.createPolygon(10, 15, 15, 16, 16, 17, 17, 18))
                .center(this.createPoint(53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(this.createPolygon(10, 15, 15, 16, 16, 17))
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
        final Geometry givenGeometry = this.createPolygon(
                10, 15, 15, 16, 16, 18
        );

        final Optional<Address> optionalActual = this.addressService.findAddressByGeometry(givenGeometry);
        assertTrue(optionalActual.isEmpty());
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
}
