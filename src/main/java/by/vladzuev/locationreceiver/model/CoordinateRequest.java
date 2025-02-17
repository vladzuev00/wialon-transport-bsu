package by.vladzuev.locationreceiver.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CoordinateRequest {

//    @NotNull
//    @DecimalMin("-90")
//    @DecimalMax("90")
    Double latitude;

//    @NotNull
//    @DecimalMin("-180")
//    @DecimalMax("180")
    Double longitude;

    @JsonCreator
    public CoordinateRequest(@JsonProperty("latitude") final Double latitude,
                             @JsonProperty("longitude") final Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
