package by.bsu.wialontransport.service.nominatim.model;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.Address;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import by.bsu.wialontransport.util.GeometryUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.geojson.Polygon;
import org.wololo.jts2geojson.GeoJSONWriter;

import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public final class NominatimReverseResponseTest extends AbstractContextTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    @Test
    public void responseShouldBeConvertedToJson()
            throws Exception {
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                3, 4, 5, 6, 7, 8
        );
        final NominatimReverseResponse givenResponse = NominatimReverseResponse.builder()
                .centerLatitude(4.4)
                .centerLongitude(5.5)
                .address(new Address("city", "country"))
                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
                .geometry(this.geoJSONWriter.write(givenGeometry))
                .extraTags(new ExtraTags("place", "yes"))
                .build();

        final String actual = this.objectMapper.writeValueAsString(givenResponse);
        final String expected = "{\"address\":{\"cityName\":\"city\",\"countryName\":\"country\"},"
                + "\"centerLatitude\":4.4,\"centerLongitude\":5.5,\"boundingBoxCoordinates\":[3.3,4.4,5.5,6.6],"
                + "\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[3.0,4.0],[5.0,6.0],[7.0,8.0],[3.0,4.0]]]},"
                + "\"extraTags\":{\"place\":\"place\",\"capital\":\"yes\"}}";
        assertEquals(expected, actual);
    }

    @Test
    public void jsonShouldBeConvertedToJson()
            throws Exception {
        final String givenJson = "{\"address\":{\"cityName\":\"city\",\"countryName\":\"country\"},"
                + "\"centerLatitude\":4.4,\"centerLongitude\":5.5,\"boundingBoxCoordinates\":[3.3,4.4,5.5,6.6],"
                + "\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[3.0,4.0],[5.0,6.0],[7.0,8.0],[3.0,4.0]]]},"
                + "\"extraTags\":{\"place\":\"place\",\"capital\":\"yes\"}}";

        final NominatimReverseResponse actual = this.objectMapper.readValue(givenJson, NominatimReverseResponse.class);
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                3, 4, 5, 6, 7, 8
        );
        final NominatimReverseResponse expected = NominatimReverseResponse.builder()
                .centerLatitude(4.4)
                .centerLongitude(5.5)
                .address(new Address("city", "country"))
                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
                .geometry(this.geoJSONWriter.write(givenGeometry))
                .extraTags(new ExtraTags("place", "yes"))
                .build();
        checkEquals(expected, actual);
    }

    private static void checkEquals(final NominatimReverseResponse expected, final NominatimReverseResponse actual) {
        assertEquals(expected.getCenterLatitude(), actual.getCenterLatitude(), 0.);
        assertEquals(expected.getCenterLongitude(), actual.getCenterLongitude(), 0.);
        assertEquals(expected.getAddress(), actual.getAddress());
        assertArrayEquals(expected.getBoundingBoxCoordinates(), actual.getBoundingBoxCoordinates(), 0.);
        GeometryUtil.checkEquals((Polygon) expected.getGeometry(), (Polygon) actual.getGeometry());
        assertEquals(expected.getExtraTags(), actual.getExtraTags());
    }
}