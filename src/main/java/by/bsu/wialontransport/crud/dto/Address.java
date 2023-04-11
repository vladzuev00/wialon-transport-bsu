package by.bsu.wialontransport.crud.dto;

import org.locationtech.jts.geom.Geometry;

public final class Address implements AbstractDto<Long> {
    Long id;
    Geometry boundaries;
    double centerLatitude;
    double centerLongitude;
    String cityName;
    String countryName;
}
