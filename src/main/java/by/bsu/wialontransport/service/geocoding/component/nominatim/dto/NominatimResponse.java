package by.bsu.wialontransport.service.geocoding.component.nominatim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NominatimResponse {
    double centerLatitude;
    double centerLongitude;
    NominatimResponseAddress address;
    double[] boundingBoxCoordinates;

    public NominatimResponse(@JsonProperty("lat") final double centerLatitude,
                             @JsonProperty("lot") final double centerLongitude,
                             @JsonProperty("address") final NominatimResponseAddress address,
                             @JsonProperty("boundingbox") final double[] boundingBoxCoordinates) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.address = address;
        this.boundingBoxCoordinates = boundingBoxCoordinates;
    }

    @Value
    public static class NominatimResponseAddress {
        String cityName;
        String countryName;

        public NominatimResponseAddress(@JsonProperty("city") final String cityName,
                                        @JsonProperty("country") final String countryName) {
            this.cityName = cityName;
            this.countryName = countryName;
        }
    }
}
