package by.bsu.wialontransport.crud.dto;

import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Address implements AbstractDto<Long> {
    private final Long id;
    private final Geometry boundingBox;
    private final Point center;
    private final String cityName;
    private final String countryName;
    private final Geometry geometry;
}
