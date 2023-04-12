package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;

import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class AddressServiceTest extends AbstractContextTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name) "
            + "VALUES(256, ST_GeomFromText('POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name) "
            + "VALUES(257, ST_GeomFromText('POLYGON((2.5 0, 2.5 2.5, 5 2.5, 5 0, 2.5 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050287, 24.873636), 4326), 'city', 'country')")
    public void addressesShouldBeFoundByGpsCoordinates() {
        final double givenLatitude = 2.5;
        final double givenLongitude = 4;

        final List<Address> actual = this.addressService.findByGpsCoordinates(givenLatitude, givenLongitude);
        final List<Address> expected = List.of(
                Address.builder()
                        .id(255L)
                        .boundingBox(this.createPolygon(1, 1, 1, 4, 4, 4, 4, 1))
                        .center(this.createPoint(53.050286, 24.873635))
                        .cityName("city")
                        .countryName("country")
                        .build(),
                Address.builder()
                        .id(257L)
                        .boundingBox(this.createPolygon(2.5, 0, 2.5, 2.5, 5, 2.5, 5, 0))
                        .center(this.createPoint(53.050287, 24.873636))
                        .cityName("city")
                        .countryName("country")
                        .build()
        );
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name) "
            + "VALUES(256, ST_GeomFromText('POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name) "
            + "VALUES(257, ST_GeomFromText('POLYGON((2.5 0, 2.5 2.5, 5 2.5, 5 0, 2.5 0))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050287, 24.873636), 4326), 'city', 'country')")
    public void addressesShouldNotBeFoundByGpsCoordinates() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 5;

        final List<Address> actual = this.addressService.findByGpsCoordinates(givenLatitude, givenLongitude);
        assertTrue(actual.isEmpty());
    }

    private Point createPoint(final double longitude, final double latitude) {
        final CoordinateXY coordinate = new CoordinateXY(longitude, latitude);
        return this.geometryFactory.createPoint(coordinate);
    }

    private Geometry createPolygon(final double firstLongitude, final double firstLatitude,
                                   final double secondLongitude, final double secondLatitude,
                                   final double thirdLongitude, final double thirdLatitude,
                                   final double fourthLongitude, final double fourthLatitude) {
        return this.geometryFactory.createPolygon(new Coordinate[]{
                        new CoordinateXY(firstLongitude, firstLatitude),
                        new CoordinateXY(secondLongitude, secondLatitude),
                        new CoordinateXY(thirdLongitude, thirdLatitude),
                        new CoordinateXY(fourthLongitude, fourthLatitude),
                        new CoordinateXY(firstLongitude, firstLatitude)
                }
        );
    }
}
