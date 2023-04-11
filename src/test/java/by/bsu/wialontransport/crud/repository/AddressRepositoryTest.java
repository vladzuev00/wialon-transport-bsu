package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;

public final class AddressRepositoryTest extends AbstractContextTest {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO addresses"
            + "(id, boundaries, center_latitude, center_longitude, city_name, country_name) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), 53.050286, 24.873635, 'city', 'country')")
    public void addressShouldBeFoundById() {
        super.startQueryCount();
        final AddressEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final AddressEntity expected = AddressEntity.builder()
                .id(255L)
                .boundaries(this.createPolygon(1, 2, 3, 4, 5, 6, 6, 7))
                .centerLatitude(53.050286)
                .centerLongitude(24.873635)
                .cityName("city")
                .countryName("country")
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void addressShouldBeSaved() {
        throw new RuntimeException();
    }

    private Geometry createPolygon(final double firstX, final double firstY,
                                   final double secondX, final double secondY,
                                   final double thirdX, final double thirdY,
                                   final double fourthX, final double fourthY) {
        return this.geometryFactory.createPolygon(new Coordinate[]{
                        new CoordinateXY(firstX, firstY),
                        new CoordinateXY(secondX, secondY),
                        new CoordinateXY(thirdX, thirdY),
                        new CoordinateXY(fourthX, fourthY),
                        new CoordinateXY(firstX, firstY)
                }
        );
    }

    private static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBoundaries(), actual.getBoundaries());
        assertEquals(expected.getCenterLatitude(), actual.getCenterLatitude(), 0.);
        assertEquals(expected.getCenterLongitude(), actual.getCenterLongitude(), 0.);
        assertEquals(expected.getCityName(), actual.getCityName());
        assertEquals(expected.getCountryName(), actual.getCountryName());
    }
}
