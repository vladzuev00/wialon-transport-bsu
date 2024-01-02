package by.bsu.wialontransport.service.nominatim.model;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.Address;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import by.bsu.wialontransport.util.GeometryTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.geojson.Polygon;
import org.wololo.jts2geojson.GeoJSONWriter;

import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

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
                geometryFactory,
                3, 4, 5, 6, 7, 8
        );
        final NominatimReverseResponse givenResponse = NominatimReverseResponse.builder()
                .centerLatitude(4.4)
                .centerLongitude(5.5)
                .address(createAddress("city", "country"))
                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
                .geometry(geoJSONWriter.write(givenGeometry))
                .extraTags(new ExtraTags("place", "yes"))
                .build();

        final String actual = objectMapper.writeValueAsString(givenResponse);
        final String expected = """
                {
                  "address": {
                    "cityName": "city",
                    "countryName": "country"
                  },
                  "centerLatitude": 4.4,
                  "centerLongitude": 5.5,
                  "boundingBoxCoordinates": [
                    3.3,
                    4.4,
                    5.5,
                    6.6
                  ],
                  "geometry": {
                    "type": "Polygon",
                    "coordinates": [
                      [
                        [
                          3,
                          4
                        ],
                        [
                          5,
                          6
                        ],
                        [
                          7,
                          8
                        ],
                        [
                          3,
                          4
                        ]
                      ]
                    ]
                  },
                  "extraTags": {
                    "place": "place",
                    "capital": "yes"
                  }
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToJson()
            throws Exception {
        final String givenJson = """
                {
                  "address": {
                    "cityName": "city",
                    "countryName": "country"
                  },
                  "centerLatitude": 4.4,
                  "centerLongitude": 5.5,
                  "boundingBoxCoordinates": [
                    3.3,
                    4.4,
                    5.5,
                    6.6
                  ],
                  "geometry": {
                    "type": "Polygon",
                    "coordinates": [
                      [
                        [
                          3,
                          4
                        ],
                        [
                          5,
                          6
                        ],
                        [
                          7,
                          8
                        ],
                        [
                          3,
                          4
                        ]
                      ]
                    ]
                  },
                  "extraTags": {
                    "place": "place",
                    "capital": "yes"
                  }
                }""";

        final NominatimReverseResponse actual = objectMapper.readValue(givenJson, NominatimReverseResponse.class);
        final Geometry givenGeometry = createPolygon(
                geometryFactory,
                3, 4, 5, 6, 7, 8
        );
        final NominatimReverseResponse expected = NominatimReverseResponse.builder()
                .centerLatitude(4.4)
                .centerLongitude(5.5)
                .address(createAddress("city", "country"))
                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
                .geometry(geoJSONWriter.write(givenGeometry))
                .extraTags(new ExtraTags("place", "yes"))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void addressShouldBeCreatedWithGivenCityName() {
        final String givenCityName = "city";
        final String givenTownName = "town";
        final String givenCountryName = "country";

        final Address actual = new Address(givenCityName, givenTownName, givenCountryName);
        final Address expected = createAddress(givenCityName, givenCountryName);
        assertEquals(expected, actual);
    }

    @Test
    public void addressShouldBeCreatedWithGivenTownName() {
        final String givenTownName = "town";
        final String givenCountryName = "country";

        final Address actual = new Address(null, givenTownName, givenCountryName);
        final Address expected = createAddress(givenTownName, givenCountryName);
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SameParameterValue")
    private static Address createAddress(final String cityName, final String countryName) {
        return Address.builder()
                .cityName(cityName)
                .countryName(countryName)
                .build();
    }

    private static void checkEquals(final NominatimReverseResponse expected, final NominatimReverseResponse actual) {
        assertEquals(expected.getCenterLatitude(), actual.getCenterLatitude(), 0.);
        assertEquals(expected.getCenterLongitude(), actual.getCenterLongitude(), 0.);
        assertEquals(expected.getAddress(), actual.getAddress());
        assertArrayEquals(expected.getBoundingBoxCoordinates(), actual.getBoundingBoxCoordinates(), 0.);
        GeometryTestUtil.checkEquals((Polygon) expected.getGeometry(), (Polygon) actual.getGeometry());
        assertEquals(expected.getExtraTags(), actual.getExtraTags());
    }
}
