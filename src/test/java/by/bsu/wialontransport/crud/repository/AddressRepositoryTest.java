package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.junit.Test;
import org.locationtech.jts.geom.*;
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
            + "(id, bounding_box, center, city_name, country_name) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 2, 3 4, 5 6, 6 7, 1 2))', 4326), "
            + "ST_SetSRID(ST_POINT(53.050286, 24.873635), 4326), 'city', 'country')")
    public void addressShouldBeFoundById() {
        super.startQueryCount();
        final AddressEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final AddressEntity expected = AddressEntity.builder()
                .id(255L)
                .boundingBox(this.createPolygon(1, 2, 3, 4, 5, 6, 6, 7))
                .center(this.createPoint(53.050286, 24.873635))
                .cityName("city")
                .countryName("country")
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void addressShouldBeSaved() {
        final AddressEntity givenAddress = AddressEntity.builder()
                .boundingBox(this.createPolygon(2, 3, 4, 5, 6, 7, 8, 9))
                .center(this.createPoint(53.050287, 24.873636))
                .cityName("city")
                .countryName("country")
                .build();

        super.startQueryCount();
        this.repository.save(givenAddress);
        super.checkQueryCount(2);
    }

    @Test
    public void a() {
        throw new RuntimeException();
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

    private static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
        assertEquals(expected.getCenter(), actual.getCenter());
        assertEquals(expected.getCityName(), actual.getCityName());
        assertEquals(expected.getCountryName(), actual.getCountryName());
    }
}
