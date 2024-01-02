package by.bsu.wialontransport.service.nominatim.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.wololo.geojson.Geometry;

@Value
public class NominatimReverseResponse {
    double centerLatitude;
    double centerLongitude;
    Address address;
    double[] boundingBoxCoordinates;
    Geometry geometry;
    ExtraTags extraTags;

    @Builder
    @JsonCreator
    public NominatimReverseResponse(@JsonProperty(value = "lat") final double centerLatitude,
                                    @JsonProperty(value = "lon") final double centerLongitude,
                                    @JsonProperty(value = "address") final Address address,
                                    @JsonProperty(value = "boundingbox") final double[] boundingBoxCoordinates,
                                    @JsonProperty(value = "geojson") final Geometry geometry,
                                    @JsonProperty(value = "extratags") final ExtraTags extraTags) {
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

        @Builder
        @JsonCreator
        public Address(@JsonProperty(value = "city") final String cityName,
                       @JsonProperty(value = "town", defaultValue = "not defined") final String townName,
                       @JsonProperty(value = "country", defaultValue = "not defined") final String countryName) {
            this.cityName = cityName != null ? cityName : townName;
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
