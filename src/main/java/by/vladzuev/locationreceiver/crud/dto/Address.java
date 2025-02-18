package by.vladzuev.locationreceiver.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Value
@Builder
@AllArgsConstructor
public class Address implements Dto<Long> {
    Long id;
    Geometry boundingBox;
    Point center;
    String cityName;
    String countryName;
    Geometry geometry;
}
