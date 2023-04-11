package by.bsu.wialontransport.crud.dto;

import lombok.Value;
import org.locationtech.jts.geom.Geometry;

@Value
public class Address implements AbstractDto<Long> {
    Long id;
    Geometry boundaries;
    double centerLatitude;
    double centerLongitude;
    String cityName;
    String countryName;
}
