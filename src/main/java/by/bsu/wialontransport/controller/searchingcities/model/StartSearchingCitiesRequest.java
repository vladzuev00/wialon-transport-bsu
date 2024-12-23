package by.bsu.wialontransport.controller.searchingcities.model;

import by.bsu.wialontransport.model.AreaCoordinateRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Value
public class StartSearchingCitiesRequest {

    @NotNull
    @Valid
    AreaCoordinateRequest areaCoordinate;

    @NotNull
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "5")
    Double searchStep;

    @Builder
    @JsonCreator
    public StartSearchingCitiesRequest(@JsonProperty(value = "areaCoordinate") final AreaCoordinateRequest areaCoordinate,
                                       @JsonProperty(value = "searchStep") final Double searchStep) {
        this.areaCoordinate = areaCoordinate;
        this.searchStep = searchStep;
    }
}
