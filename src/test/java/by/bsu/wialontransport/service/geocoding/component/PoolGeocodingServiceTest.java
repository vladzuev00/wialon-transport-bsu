package by.bsu.wialontransport.service.geocoding.component;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static by.bsu.wialontransport.unil.GeometryUtil.createPoint;
import static by.bsu.wialontransport.unil.GeometryUtil.createPolygon;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class PoolGeocodingServiceTest extends AbstractContextTest {

    @Autowired
    private PoolGeocodingService poolGeocodingService;

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
    public void addressShouldBeReceived() {
        final double givenLatitude = 2.5;
        final double givenLongitude = 2.5;

        final Optional<Address> optionalActual = this.poolGeocodingService.receive(
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
    public void addressShouldNotBeReceived() {
        final double givenLatitude = 20;
        final double givenLongitude = 20;

        final Optional<Address> optionalActual = this.poolGeocodingService.receive(
                givenLatitude, givenLongitude
        );
        assertTrue(optionalActual.isEmpty());
    }
}
