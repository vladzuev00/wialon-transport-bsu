package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static by.bsu.wialontransport.unil.GeometryUtil.createPoint;
import static by.bsu.wialontransport.unil.GeometryUtil.createPolygon;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class AddressRepositoryTest extends AbstractContextTest {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, bounding_box, center, city_name, country_name, geometry) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country', "
            + "ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 1 2))', 4326)"
            + ")")
    public void addressShouldBeFoundById() {
        super.startQueryCount();
        final AddressEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final AddressEntity expected = AddressEntity.builder()
                .id(255L)
                .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                .center(createPoint(this.geometryFactory, 53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void addressShouldBeSaved() {
        final AddressEntity givenAddress = AddressEntity.builder()
                .boundingBox(createPolygon(this.geometryFactory, 2, 3, 4, 5, 6, 7, 8, 9))
                .center(createPoint(this.geometryFactory, 53.050287, 24.873636))
                .cityName("city")
                .countryName("country")
                .geometry(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6))
                .build();

        super.startQueryCount();
        this.repository.save(givenAddress);
        super.checkQueryCount(2);
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
    public void addressShouldBeFoundByGpsCoordinates() {
        final double givenLatitude = 2.5;
        final double givenLongitude = 2.5;

        final Optional<AddressEntity> optionalActual = this.repository.findByGpsCoordinates(
                givenLatitude, givenLongitude
        );
        final long actualId = optionalActual.map(AddressEntity::getId).orElseThrow();
        final long expectedId = 255;
        assertEquals(expectedId, actualId);
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
        final double givenLatitude = 20.;
        final double givenLongitude = 20.;

        final Optional<AddressEntity> actual = this.repository.findByGpsCoordinates(givenLatitude, givenLongitude);
        assertTrue(actual.isEmpty());
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
                this.geometryFactory,
                10, 15, 15, 16, 16, 17
        );

        final Optional<AddressEntity> optionalActual = this.repository.findAddressByGeometry(givenGeometry);
        final long actualId = optionalActual.map(AddressEntity::getId).orElseThrow();
        final long expectedId = 257;
        assertEquals(expectedId, actualId);
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
    public void addressShouldNotBeFoundByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                10, 15, 15, 16, 16, 17
        );

        final Optional<AddressEntity> optionalActual = this.repository.findAddressByGeometry(givenGeometry);
        assertTrue(optionalActual.isEmpty());
    }

    private static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
        assertEquals(expected.getCenter(), actual.getCenter());
        assertEquals(expected.getCityName(), actual.getCityName());
        assertEquals(expected.getCountryName(), actual.getCountryName());
    }
}
