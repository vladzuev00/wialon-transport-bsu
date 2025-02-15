package by.vladzuev.locationreceiver.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
public class AreaCoordinateRequest {

    @NotNull
    @Valid
    CoordinateRequest leftBottom;

    @NotNull
    @Valid
    CoordinateRequest rightUpper;

    @JsonCreator
    public AreaCoordinateRequest(@JsonProperty("leftBottom") final CoordinateRequest leftBottom,
                                 @JsonProperty("rightUpper") final CoordinateRequest rightUpper) {
        this.leftBottom = leftBottom;
        this.rightUpper = rightUpper;
    }
}
