package by.bsu.wialontransport.service.nominatim.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse;
import by.bsu.wialontransport.service.nominatim.model.NominatimReverseResponse.ExtraTags;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.jts2geojson.GeoJSONWriter;

import static by.bsu.wialontransport.util.GeometryTestUtil.createPoint;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertEquals;

public final class ReverseResponseToAddressMapperTest extends AbstractContextTest {

    @Autowired
    private ReverseResponseToAddressMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    @Test
    public void responseShouldBeMapped() {
        final double givenCenterLatitude = 8.8;
        final double givenCenterLongitude = 7.7;
        final String givenCityName = "city";
        final String givenCountryName = "country";
        final String givenPlace = "place";
        final String givenCapital = "capital";
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                4.4, 5.5, 6.6, 7.7, 8.8, 9.9
        );
        final NominatimReverseResponse givenResponse = NominatimReverseResponse.builder()
                .centerLatitude(givenCenterLatitude)
                .centerLongitude(givenCenterLongitude)
                .address(new NominatimReverseResponse.Address(givenCityName, givenCountryName))
                .boundingBoxCoordinates(new double[]{5.5, 9.9, 4.4, 8.8})
                .geometry(this.geoJSONWriter.write(givenGeometry))
                .extraTags(new ExtraTags(givenPlace, givenCapital))
                .build();

        final Address actual = this.mapper.map(givenResponse);

        final Geometry expectedBoundingBox = createPolygon(
                this.geometryFactory,
                4.4, 5.5, 8.8, 5.5, 8.8, 9.9, 4.4, 9.9
        );
        final Point expectedCenter = createPoint(this.geometryFactory, givenCenterLongitude, givenCenterLatitude);
        final Address expected = Address.builder()
                .boundingBox(expectedBoundingBox)
                .center(expectedCenter)
                .cityName(givenCityName)
                .countryName(givenCountryName)
                .geometry(givenGeometry)
                .build();

        assertEquals(expected, actual);
    }

    @Test
    public void responseWithNullAsCityNameShouldBeMapped() {
        final double givenCenterLatitude = 8.8;
        final double givenCenterLongitude = 7.7;
        final String givenCountryName = "country";
        final String givenPlace = "place";
        final String givenCapital = "capital";
        final Geometry givenGeometry = createPolygon(
                this.geometryFactory,
                4.4, 5.5, 6.6, 7.7, 8.8, 9.9
        );
        final NominatimReverseResponse givenResponse = NominatimReverseResponse.builder()
                .centerLatitude(givenCenterLatitude)
                .centerLongitude(givenCenterLongitude)
                .address(new NominatimReverseResponse.Address(null, givenCountryName))
                .boundingBoxCoordinates(new double[]{5.5, 9.9, 4.4, 8.8})
                .geometry(this.geoJSONWriter.write(givenGeometry))
                .extraTags(new ExtraTags(givenPlace, givenCapital))
                .build();

        final Address actual = this.mapper.map(givenResponse);

        final Geometry expectedBoundingBox = createPolygon(
                this.geometryFactory,
                4.4, 5.5, 8.8, 5.5, 8.8, 9.9, 4.4, 9.9
        );
        final Point expectedCenter = createPoint(this.geometryFactory, givenCenterLongitude, givenCenterLatitude);
        final Address expected = Address.builder()
                .boundingBox(expectedBoundingBox)
                .center(expectedCenter)
                .cityName("not defined")
                .countryName(givenCountryName)
                .geometry(givenGeometry)
                .build();

        assertEquals(expected, actual);
    }

}
