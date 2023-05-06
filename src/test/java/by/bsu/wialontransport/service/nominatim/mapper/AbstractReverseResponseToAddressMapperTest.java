package by.bsu.wialontransport.service.nominatim.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import static by.bsu.wialontransport.util.GeometryUtil.createPoint;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static org.junit.Assert.assertEquals;

public final class AbstractReverseResponseToAddressMapperTest extends AbstractContextTest {

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONReader geoJSONReader;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    private TestReverseResponseToAddressMapper mapper;

    @Before
    public void initializeMapper() {
        this.mapper = new TestReverseResponseToAddressMapper(this.geometryFactory, this.geoJSONReader);
    }

    @Test
    public void reverseResponseShouldBeMappedToAddress() {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                11.1, 12.2, 13.3, 14.4, 15.5, 16.6
        );
        final NominatimReverseResponse givenReverseResponse = NominatimReverseResponse.builder()
                .centerLatitude(5.5)
                .centerLongitude(6.6)
                .address(new NominatimReverseResponse.Address("city", "country"))
                .boundingBoxCoordinates(new double[]{7.7, 8.8, 9.9, 10.1})
                .geometry(this.geoJSONWriter.write(givenGeometry))
                .extraTags(new ExtraTags("place", "capital"))
                .build();

        final Address actual = this.mapper.map(givenReverseResponse);
        final Address expected = Address.builder()
                .boundingBox(createPolygon(
                        this.geometryFactory,
                        7.7, 9.9, 7.7, 10.1, 8.8, 10.1, 8.8, 9.9))
                .center(createPoint(this.geometryFactory, 5.5, 6.6))
                .cityName("city")
                .countryName("country")
                .geometry(givenGeometry)
                .build();
        assertEquals(expected, actual);
    }

    private static final class TestReverseResponseToAddressMapper
            extends AbstractReverseResponseToAddressMapper<Address> {

        public TestReverseResponseToAddressMapper(final GeometryFactory geometryFactory,
                                                  final GeoJSONReader geoJSONReader) {
            super(geometryFactory, geoJSONReader);
        }

        @Override
        protected Address createAddress(final Geometry boundingBox,
                                        final Point center,
                                        final String cityName,
                                        final String countryName,
                                        final Geometry geometry) {
            return Address.builder()
                    .boundingBox(boundingBox)
                    .center(center)
                    .cityName(cityName)
                    .countryName(countryName)
                    .geometry(geometry)
                    .build();
        }
    }
}
