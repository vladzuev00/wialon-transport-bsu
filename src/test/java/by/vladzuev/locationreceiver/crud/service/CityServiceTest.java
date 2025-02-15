package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.util.GeometryTestUtil;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class CityServiceTest extends AbstractSpringBootTest {

    @Autowired
    private CityService cityService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldExistByGeometry() {
        final Geometry givenGeometry = GeometryTestUtil.createPolygon(
                geometryFactory,
                3, 1, 4, 1, 4, 2, 3, 2
        );

        final boolean exists = cityService.isExistByGeometry(givenGeometry);
        assertTrue(exists);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldNotExistByGeometry() {
        final Geometry givenGeometry = GeometryTestUtil.createPolygon(
                geometryFactory,
                1, 2, 3, 4, 5, 6
        );

        final boolean exists = cityService.isExistByGeometry(givenGeometry);
        assertFalse(exists);
    }
}
