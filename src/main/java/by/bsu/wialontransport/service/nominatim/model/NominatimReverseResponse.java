package by.bsu.wialontransport.service.nominatim.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.wololo.geojson.Geometry;

@Value
@Builder
public class NominatimReverseResponse {
    double centerLatitude;
    double centerLongitude;
    Address address;
    double[] boundingBoxCoordinates;
    Geometry geometry;
    ExtraTags extraTags;

    @JsonCreator
    public NominatimReverseResponse(@JsonProperty("lat") final double centerLatitude,
                                    @JsonProperty("lot") final double centerLongitude,
                                    @JsonProperty("address") final Address address,
                                    @JsonProperty("boundingbox") final double[] boundingBoxCoordinates,
                                    @JsonProperty("geojson") final Geometry geometry,
                                    @JsonProperty("extratags") final ExtraTags extraTags) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.address = address;
        this.boundingBoxCoordinates = boundingBoxCoordinates;
        this.geometry = geometry;
        this.extraTags = extraTags;
    }

    @Value
    public static class Address {
        String cityName;
        String countryName;

        @JsonCreator
        public Address(@JsonProperty("city") final String cityName,
                       @JsonProperty("country") final String countryName) {
            this.cityName = cityName;
            this.countryName = countryName;
        }
    }

    @Value
    @Builder
    public static class ExtraTags {
        String place;
        String capital;

        @JsonCreator
        public ExtraTags(@JsonProperty("place") final String place,
                         @JsonProperty("capital") final String capital) {
            this.place = place;
            this.capital = capital;
        }
    }
}
