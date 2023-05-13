package by.bsu.wialontransport.crud.dto;

import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Value
@AllArgsConstructor
@Builder
public class Address implements AbstractDto<Long> {
    Long id;
    Geometry boundingBox;
    Point center;
    String cityName;
    String countryName;
    Geometry geometry;
}
