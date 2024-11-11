package by.bsu.wialontransport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class GpsCoordinate {
    double latitude;
    double longitude;
}
