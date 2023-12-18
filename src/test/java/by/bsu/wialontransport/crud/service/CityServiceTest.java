package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class CityServiceTest extends AbstractContextTest {

    @Autowired
    private CityService cityService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                3, 1, 4, 1, 4, 2, 3, 2
        );

        final boolean exists = this.cityService.isExistByGeometry(givenGeometry);
        assertTrue(exists);
    }

    @Test
    @Sql("classpath:sql/cities/insert-cities.sql")
    public void cityShouldNotExistByGeometry() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                1, 2, 3, 4, 5, 6
        );

        final boolean exists = this.cityService.isExistByGeometry(givenGeometry);
        assertFalse(exists);
    }
}
