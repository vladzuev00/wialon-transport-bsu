package by.vladzuev.locationreceiver.controller.searchingcities.model;

import by.vladzuev.locationreceiver.model.AreaCoordinateRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

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
