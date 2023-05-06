package by.bsu.wialontransport.service.nominatim.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.City;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.util.GeometryUtil.createPoint;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static org.junit.Assert.assertEquals;

public final class ReverseResponseToCityMapperTest extends AbstractContextTest {

    @Autowired
    private ReverseResponseToCityMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void cityShouldBeCreated() {
        final Geometry givenBoundingBox = createPolygon(
                this.geometryFactory,
                4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.1, 11.2
        );
        final Point givenCenter = createPoint(this.geometryFactory, 7.7, 8.8);
        final String givenCityName = "city";
        final String givenCountryName = "country";
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                4.4, 5.5, 6.6, 7.7, 8.8, 9.9
        );

        final City actual = this.mapper.createAddress(
                givenBoundingBox, givenCenter, givenCityName, givenCountryName, givenGeometry
        );
        final City expected = City.cityBuilder()
                .boundingBox(givenBoundingBox)
                .center(givenCenter)
                .cityName(givenCityName)
                .countryName(givenCountryName)
                .geometry(givenGeometry)
                .build();
        assertEquals(expected, actual);
    }

}
