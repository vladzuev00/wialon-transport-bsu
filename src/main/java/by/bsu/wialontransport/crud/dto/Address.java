package by.bsu.wialontransport.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.locationtech.jts.geom.Geometry;

@Value
@AllArgsConstructor
@Builder
public class Address implements AbstractDto<Long> {
    Long id;
    Geometry boundaries;
    double centerLatitude;
    double centerLongitude;
    String cityName;
    String countryName;
}
