package by.bsu.wialontransport.service.geocoding.component.nominatim.dto;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.service.geocoding.component.nominatim.dto.NominatimResponse.NominatimResponseAddress;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class NominatimResponseTest extends AbstractContextTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void responseShouldBeConvertedToJson()
            throws Exception {
        final NominatimResponse givenResponse = NominatimResponse.builder()
                .centerLatitude(4.4)
                .centerLongitude(5.5)
                .address(new NominatimResponseAddress("city", "country"))
                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
                .build();

        final String actual = this.objectMapper.writeValueAsString(givenResponse);
        final String expected = "{\"address\":{\"cityName\":\"city\",\"countryName\":\"country\"},"
                + "\"centerLatitude\":4.4,\"centerLongitude\":5.5,\"boundingBoxCoordinates\":[3.3,4.4,5.5,6.6]}";
        assertEquals(expected, actual);
    }

    @Test
    public void jsonShouldBeConvertedToResponse()
            throws Exception {
        final String givenJson = "{\"address\":{\"cityName\":\"city\",\"countryName\":\"country\"},"
                + "\"centerLatitude\":4.4,\"centerLongitude\":5.5,"
                + "\"boundingBoxCoordinates\":[\"3.3\",\"4.4\",\"5.5\",\"6.6\"]}";

        final NominatimResponse actual = this.objectMapper.readValue(givenJson, NominatimResponse.class);
        final NominatimResponse expected = NominatimResponse.builder()
                .centerLatitude(4.4)
                .centerLongitude(5.5)
                .address(new NominatimResponseAddress("city", "country"))
                .boundingBoxCoordinates(new double[]{3.3, 4.4, 5.5, 6.6})
                .build();
        assertEquals(expected, actual);
    }
}
