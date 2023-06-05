package by.bsu.wialontransport.service.nominatim.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.wololo.geojson.Geometry;

//TODO: refactor tests
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
    public NominatimReverseResponse(@JsonProperty(value = "lat", required = true) final double centerLatitude,
                                    @JsonProperty(value = "lon", required = true) final double centerLongitude,
                                    @JsonProperty(value = "address", required = true) final Address address,
                                    @JsonProperty(value = "boundingbox", required = true) final double[] boundingBoxCoordinates,
                                    @JsonProperty(value = "geojson", required = true) final Geometry geometry,
                                    @JsonProperty(value = "extratags", required = true) final ExtraTags extraTags) {
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
        public Address(@JsonProperty(value = "city", defaultValue = "not defined") final String cityName,
                       @JsonProperty(value = "country", defaultValue = "not defined") final String countryName) {
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
        public ExtraTags(@JsonProperty(value = "place", defaultValue = "not defined") final String place,
                         @JsonProperty(value = "capital", defaultValue = "not defined") final String capital) {
            this.place = place;
            this.capital = capital;
        }
    }
}
