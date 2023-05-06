package by.bsu.wialontransport.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
public class AreaCoordinate {

    @NotNull
    @Valid
    Coordinate leftBottom;

    @NotNull
    @Valid
    Coordinate rightUpper;

    @JsonCreator
    public AreaCoordinate(@JsonProperty("leftBottom") final Coordinate leftBottom,
                          @JsonProperty("rightUpper") final Coordinate rightUpper) {
        this.leftBottom = leftBottom;
        this.rightUpper = rightUpper;
    }
}