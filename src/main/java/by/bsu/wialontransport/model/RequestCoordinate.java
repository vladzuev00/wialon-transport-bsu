package by.bsu.wialontransport.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Value
public class RequestCoordinate {

    @NotNull
    @DecimalMin("-90")
    @DecimalMax("90")
    Double latitude;

    @NotNull
    @DecimalMin("-180")
    @DecimalMax("180")
    Double longitude;

    @JsonCreator
    public RequestCoordinate(@JsonProperty("latitude") final Double latitude,
                             @JsonProperty("longitude") final Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
