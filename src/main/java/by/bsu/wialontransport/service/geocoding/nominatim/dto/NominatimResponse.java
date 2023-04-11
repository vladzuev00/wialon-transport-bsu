package by.bsu.wialontransport.service.geocoding.nominatim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class NominatimResponse {
    double centerLatitude;
    double centerLongitude;
    NominatimAddressResponse address;
    double[] boundingBoxCoordinate;

    public NominatimResponse(@JsonProperty("lat") final double centerLatitude,
                             @JsonProperty("lot") final double centerLongitude,
                             @JsonProperty("address") final NominatimAddressResponse address,
                             @JsonProperty("boundingbox") final double[] boundingBoxCoordinate) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.address = address;
        this.boundingBoxCoordinate = boundingBoxCoordinate;
    }

    @Value
    public static class NominatimAddressResponse {
        String cityName;
        String countryName;

        public NominatimAddressResponse(@JsonProperty("city") final String cityName,
                                        @JsonProperty("country") final String countryName) {
            this.cityName = cityName;
            this.countryName = countryName;
        }
    }
}
