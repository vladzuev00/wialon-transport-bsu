package by.bsu.wialontransport.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
public class AreaCoordinateRequest {

    @NotNull
    @Valid
    RequestCoordinate leftBottom;

    @NotNull
    @Valid
    RequestCoordinate rightUpper;

    @JsonCreator
    public AreaCoordinateRequest(@JsonProperty("leftBottom") final RequestCoordinate leftBottom,
                                 @JsonProperty("rightUpper") final RequestCoordinate rightUpper) {
        this.leftBottom = leftBottom;
        this.rightUpper = rightUpper;
    }
}